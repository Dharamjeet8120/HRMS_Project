import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { approveLeave, cancelLeave, deleteLeave, rejectLeave, searchLeaves } from "../../api/leaveApi";
import { getAllEmployees } from "../../api/employeeApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import DataTable from "../../components/common/DataTable";
import StatusBadge from "../../components/common/StatusBadge";
import ConfirmDialog from "../../components/common/ConfirmDialog";
import { LEAVE_STATUSES } from "../../utils/constants";
import { formatDate } from "../../utils/format";
import { useAuth } from "../../hooks/useAuth";

export default function LeaveList() {
  const { user, isManager } = useAuth();

  const [leaves, setLeaves] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [actionError, setActionError] = useState("");

  const [filters, setFilters] = useState({ employeeId: "", status: "" });
  const [pendingDelete, setPendingDelete] = useState(null);
  const [rejectTarget, setRejectTarget] = useState(null);
  const [rejectReason, setRejectReason] = useState("");

  useEffect(() => {
    getAllEmployees().then(setEmployees).catch(() => {});
  }, []);

  async function runSearch(activeFilters = filters) {
    setLoading(true);
    setError("");
    try {
      const data = await searchLeaves(activeFilters);
      setLeaves(data);
    } catch (err) {
      setError(extractErrorMessage(err, "Could not load leave requests."));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    runSearch();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function handleFilterSubmit(e) {
    e.preventDefault();
    runSearch(filters);
  }

  function resetFilters() {
    const cleared = { employeeId: "", status: "" };
    setFilters(cleared);
    runSearch(cleared);
  }

  function replaceLeave(updated) {
    setLeaves((list) => list.map((l) => (l.leaveId === updated.leaveId ? updated : l)));
  }

  async function handleApprove(row) {
    setActionError("");
    try {
      const updated = await approveLeave(row.leaveId, user?.username || "admin");
      replaceLeave(updated);
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not approve this request."));
    }
  }

  async function submitReject() {
    if (!rejectTarget) return;
    setActionError("");
    try {
      const updated = await rejectLeave(rejectTarget.leaveId, user?.username || "admin", rejectReason || undefined);
      replaceLeave(updated);
      setRejectTarget(null);
      setRejectReason("");
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not reject this request."));
    }
  }

  async function handleCancel(row) {
    setActionError("");
    try {
      const updated = await cancelLeave(row.leaveId);
      replaceLeave(updated);
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not cancel this request."));
    }
  }

  async function confirmDelete() {
    if (!pendingDelete) return;
    setActionError("");
    try {
      await deleteLeave(pendingDelete.leaveId);
      setLeaves((list) => list.filter((l) => l.leaveId !== pendingDelete.leaveId));
      setPendingDelete(null);
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not delete this request."));
    }
  }

  const columns = [
    { key: "employeeName", header: "Employee", render: (r) => <span className="cell-primary">{r.employeeName}</span> },
    { key: "leaveType", header: "Type" },
    { key: "startDate", header: "From", render: (r) => formatDate(r.startDate) },
    { key: "endDate", header: "To", render: (r) => formatDate(r.endDate) },
    { key: "totalDays", header: "Days", align: "right" },
    { key: "reason", header: "Reason" },
    { key: "status", header: "Status", render: (r) => <StatusBadge status={r.status} /> },
    {
      key: "actions",
      header: "",
      align: "right",
      render: (r) => (
        <div className="cell-actions">
          {r.status === "PENDING" && isManager && (
            <>
              <button className="btn btn-outline btn-sm" onClick={() => handleApprove(r)}>
                Approve
              </button>
              <button className="btn btn-danger btn-sm" onClick={() => setRejectTarget(r)}>
                Reject
              </button>
            </>
          )}
          {(r.status === "PENDING" || r.status === "APPROVED") && (
            <button className="btn btn-ghost btn-sm" onClick={() => handleCancel(r)}>
              Cancel
            </button>
          )}
          {isManager && (
            <button className="btn btn-danger btn-sm" onClick={() => setPendingDelete(r)}>
              Delete
            </button>
          )}
        </div>
      ),
    },
  ];

  return (
    <div>
      <PageHeader
        title="Leave requests"
        subtitle={`${leaves.length} request${leaves.length === 1 ? "" : "s"}`}
        actions={
          <Link className="btn btn-primary" to="/leaves/new">
            + Apply for leave
          </Link>
        }
      />

      <form className="filter-bar" onSubmit={handleFilterSubmit}>
        <div className="field">
          <label htmlFor="f-emp">Employee</label>
          <select id="f-emp" value={filters.employeeId} onChange={(e) => setFilters((f) => ({ ...f, employeeId: e.target.value }))}>
            <option value="">All employees</option>
            {employees.map((e) => (
              <option key={e.employeeId} value={e.employeeId}>
                {e.firstName} {e.lastName}
              </option>
            ))}
          </select>
        </div>
        <div className="field">
          <label htmlFor="f-status">Status</label>
          <select id="f-status" value={filters.status} onChange={(e) => setFilters((f) => ({ ...f, status: e.target.value }))}>
            <option value="">Any status</option>
            {LEAVE_STATUSES.map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </div>
        <button className="btn btn-primary btn-sm" type="submit">
          Search
        </button>
        <button className="btn btn-ghost btn-sm" type="button" onClick={resetFilters}>
          Reset
        </button>
      </form>

      {error && <div className="state-banner error">{error}</div>}
      {actionError && <div className="state-banner error">{actionError}</div>}

      <DataTable
        columns={columns}
        rows={leaves}
        rowKey={(r) => r.leaveId}
        loading={loading}
        emptyTitle="No leave requests match this search"
        emptyHint="Try clearing the filters, or apply for leave."
      />

      <ConfirmDialog
        open={Boolean(pendingDelete)}
        title="Delete leave request?"
        message={pendingDelete && `This removes ${pendingDelete.employeeName}'s ${pendingDelete.leaveType.toLowerCase()} leave request.`}
        confirmLabel="Delete"
        danger
        onConfirm={confirmDelete}
        onCancel={() => setPendingDelete(null)}
      />

      {rejectTarget && (
        <div className="modal-backdrop" onClick={() => setRejectTarget(null)}>
          <div className="modal-box" onClick={(e) => e.stopPropagation()}>
            <h3>Reject leave request</h3>
            <p>
              Rejecting {rejectTarget.employeeName}'s request for {rejectTarget.totalDays} day
              {rejectTarget.totalDays === 1 ? "" : "s"} of {rejectTarget.leaveType.toLowerCase()} leave.
            </p>
            <div className="field field-full" style={{ marginBottom: 16 }}>
              <label htmlFor="rejectReason">Reason (optional)</label>
              <textarea
                id="rejectReason"
                rows={3}
                value={rejectReason}
                onChange={(e) => setRejectReason(e.target.value)}
                placeholder="Let the employee know why"
              />
            </div>
            <div className="modal-actions">
              <button className="btn btn-outline btn-sm" onClick={() => setRejectTarget(null)}>
                Cancel
              </button>
              <button className="btn btn-danger btn-sm" onClick={submitReject}>
                Reject request
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
