import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { checkOut, deleteAttendance, searchAttendance } from "../../api/attendanceApi";
import { getAllEmployees } from "../../api/employeeApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import DataTable from "../../components/common/DataTable";
import StatusBadge from "../../components/common/StatusBadge";
import ConfirmDialog from "../../components/common/ConfirmDialog";
import { ATTENDANCE_STATUSES } from "../../utils/constants";
import { formatDate } from "../../utils/format";
import { useAuth } from "../../hooks/useAuth";

const today = () => new Date().toISOString().slice(0, 10);

export default function AttendanceList() {
  const { isManager } = useAuth();

  const [records, setRecords] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [actionError, setActionError] = useState("");
  const [pendingDelete, setPendingDelete] = useState(null);

  const [filters, setFilters] = useState({
    employeeId: "",
    date: "",
    startDate: "",
    endDate: "",
    status: "",
  });

  useEffect(() => {
    getAllEmployees().then(setEmployees).catch(() => {});
  }, []);

  async function runSearch(activeFilters = filters) {
    setLoading(true);
    setError("");
    try {
      const data = await searchAttendance(activeFilters);
      setRecords(data);
    } catch (err) {
      setError(extractErrorMessage(err, "Could not load attendance records."));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    runSearch({ date: today() });
    setFilters((f) => ({ ...f, date: today() }));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function handleFilterSubmit(e) {
    e.preventDefault();
    runSearch(filters);
  }

  function resetFilters() {
    const cleared = { employeeId: "", date: "", startDate: "", endDate: "", status: "" };
    setFilters(cleared);
    runSearch(cleared);
  }

  async function handleCheckOut(row) {
    setActionError("");
    try {
      const updated = await checkOut(row.employeeId, row.attendanceDate);
      setRecords((list) => list.map((r) => (r.attendanceId === updated.attendanceId ? updated : r)));
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not check this employee out."));
    }
  }

  async function confirmDelete() {
    if (!pendingDelete) return;
    setActionError("");
    try {
      await deleteAttendance(pendingDelete.attendanceId);
      setRecords((list) => list.filter((r) => r.attendanceId !== pendingDelete.attendanceId));
      setPendingDelete(null);
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not delete this record."));
    }
  }

  const columns = [
    { key: "employeeName", header: "Employee", render: (r) => <span className="cell-primary">{r.employeeName}</span> },
    { key: "attendanceDate", header: "Date", render: (r) => formatDate(r.attendanceDate) },
    { key: "checkInTime", header: "Check-in", render: (r) => r.checkInTime || "—" },
    { key: "checkOutTime", header: "Check-out", render: (r) => r.checkOutTime || "—" },
    { key: "workingHours", header: "Hours", render: (r) => (r.workingHours != null ? r.workingHours.toFixed(1) : "—") },
    { key: "status", header: "Status", render: (r) => <StatusBadge status={r.status} /> },
    { key: "remarks", header: "Remarks", render: (r) => r.remarks || "—" },
    {
      key: "actions",
      header: "",
      align: "right",
      render: (r) => (
        <div className="cell-actions">
          {r.checkInTime && !r.checkOutTime && (
            <button className="btn btn-outline btn-sm" onClick={() => handleCheckOut(r)}>
              Check out
            </button>
          )}
          {isManager && (
            <>
              <Link className="btn btn-outline btn-sm" to={`/attendance/${r.attendanceId}/edit`}>
                Edit
              </Link>
              <button className="btn btn-danger btn-sm" onClick={() => setPendingDelete(r)}>
                Delete
              </button>
            </>
          )}
        </div>
      ),
    },
  ];

  return (
    <div>
      <PageHeader
        title="Attendance"
        subtitle={`${records.length} record${records.length === 1 ? "" : "s"}`}
        actions={
          <Link className="btn btn-primary" to="/attendance/new">
            + Mark attendance
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
          <label htmlFor="f-date">Date</label>
          <input id="f-date" type="date" value={filters.date} onChange={(e) => setFilters((f) => ({ ...f, date: e.target.value, startDate: "", endDate: "" }))} />
        </div>
        <div className="field">
          <label htmlFor="f-start">From</label>
          <input id="f-start" type="date" value={filters.startDate} onChange={(e) => setFilters((f) => ({ ...f, startDate: e.target.value, date: "" }))} />
        </div>
        <div className="field">
          <label htmlFor="f-end">To</label>
          <input id="f-end" type="date" value={filters.endDate} onChange={(e) => setFilters((f) => ({ ...f, endDate: e.target.value, date: "" }))} />
        </div>
        <div className="field">
          <label htmlFor="f-status">Status</label>
          <select id="f-status" value={filters.status} onChange={(e) => setFilters((f) => ({ ...f, status: e.target.value }))}>
            <option value="">Any status</option>
            {ATTENDANCE_STATUSES.map((s) => (
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
        rows={records}
        rowKey={(r) => r.attendanceId}
        loading={loading}
        emptyTitle="No attendance records match this search"
        emptyHint="Try clearing the filters, or mark attendance for today."
      />

      <ConfirmDialog
        open={Boolean(pendingDelete)}
        title="Delete attendance record?"
        message={pendingDelete && `This removes the record for ${pendingDelete.employeeName} on ${formatDate(pendingDelete.attendanceDate)}.`}
        confirmLabel="Delete"
        danger
        onConfirm={confirmDelete}
        onCancel={() => setPendingDelete(null)}
      />
    </div>
  );
}
