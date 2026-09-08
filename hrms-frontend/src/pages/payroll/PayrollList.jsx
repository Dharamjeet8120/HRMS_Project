import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deletePayroll, holdPayroll, markAsPaid, searchPayrolls } from "../../api/payrollApi";
import { getAllEmployees } from "../../api/employeeApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import DataTable from "../../components/common/DataTable";
import StatusBadge from "../../components/common/StatusBadge";
import ConfirmDialog from "../../components/common/ConfirmDialog";
import { MONTHS, PAYROLL_STATUSES } from "../../utils/constants";
import { formatCurrency, formatDate } from "../../utils/format";
import { useAuth } from "../../hooks/useAuth";

export default function PayrollList() {
  const { isManager } = useAuth();

  const [payrolls, setPayrolls] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [actionError, setActionError] = useState("");
  const [pendingDelete, setPendingDelete] = useState(null);
  const [holdTarget, setHoldTarget] = useState(null);
  const [holdRemarks, setHoldRemarks] = useState("");

  const [filters, setFilters] = useState({ employeeId: "", month: "", year: "", status: "" });

  useEffect(() => {
    getAllEmployees().then(setEmployees).catch(() => {});
  }, []);

  async function runSearch(activeFilters = filters) {
    setLoading(true);
    setError("");
    try {
      const data = await searchPayrolls(activeFilters);
      setPayrolls(data);
    } catch (err) {
      setError(extractErrorMessage(err, "Could not load payroll records."));
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
    const cleared = { employeeId: "", month: "", year: "", status: "" };
    setFilters(cleared);
    runSearch(cleared);
  }

  function replacePayroll(updated) {
    setPayrolls((list) => list.map((p) => (p.payrollId === updated.payrollId ? updated : p)));
  }

  async function handleMarkPaid(row) {
    setActionError("");
    try {
      const updated = await markAsPaid(row.payrollId);
      replacePayroll(updated);
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not mark this payroll as paid."));
    }
  }

  async function submitHold() {
    if (!holdTarget) return;
    setActionError("");
    try {
      const updated = await holdPayroll(holdTarget.payrollId, holdRemarks || undefined);
      replacePayroll(updated);
      setHoldTarget(null);
      setHoldRemarks("");
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not put this payroll on hold."));
    }
  }

  async function confirmDelete() {
    if (!pendingDelete) return;
    setActionError("");
    try {
      await deletePayroll(pendingDelete.payrollId);
      setPayrolls((list) => list.filter((p) => p.payrollId !== pendingDelete.payrollId));
      setPendingDelete(null);
    } catch (err) {
      setActionError(extractErrorMessage(err, "Could not delete this payroll record."));
    }
  }

  const columns = [
    { key: "employeeName", header: "Employee", render: (r) => <span className="cell-primary">{r.employeeName}</span> },
    { key: "period", header: "Period", render: (r) => `${r.payMonth} ${r.payYear}` },
    { key: "basicSalary", header: "Basic", align: "right", render: (r) => formatCurrency(r.basicSalary) },
    { key: "grossSalary", header: "Gross", align: "right", render: (r) => formatCurrency(r.grossSalary) },
    { key: "netSalary", header: "Net", align: "right", render: (r) => <strong>{formatCurrency(r.netSalary)}</strong> },
    { key: "presentDays", header: "Present", align: "right", render: (r) => `${r.presentDays ?? "—"}/${r.totalWorkingDays ?? "—"}` },
    { key: "status", header: "Status", render: (r) => <StatusBadge status={r.status} /> },
    { key: "paymentDate", header: "Paid on", render: (r) => formatDate(r.paymentDate) },
  ];

  if (isManager) {
    columns.push({
      key: "actions",
      header: "",
      align: "right",
      render: (r) => (
        <div className="cell-actions">
          {r.status !== "PAID" && (
            <button className="btn btn-outline btn-sm" onClick={() => handleMarkPaid(r)}>
              Mark paid
            </button>
          )}
          {r.status !== "ON_HOLD" && r.status !== "PAID" && (
            <button className="btn btn-ghost btn-sm" onClick={() => setHoldTarget(r)}>
              Hold
            </button>
          )}
          <button className="btn btn-danger btn-sm" onClick={() => setPendingDelete(r)}>
            Delete
          </button>
        </div>
      ),
    });
  }

  return (
    <div>
      <PageHeader
        title="Payroll"
        subtitle={`${payrolls.length} record${payrolls.length === 1 ? "" : "s"}`}
        actions={
          isManager && (
            <Link className="btn btn-primary" to="/payroll/new">
              + Generate payroll
            </Link>
          )
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
          <label htmlFor="f-month">Month</label>
          <select id="f-month" value={filters.month} onChange={(e) => setFilters((f) => ({ ...f, month: e.target.value }))}>
            <option value="">Any month</option>
            {MONTHS.map((m) => (
              <option key={m} value={m}>
                {m}
              </option>
            ))}
          </select>
        </div>
        <div className="field">
          <label htmlFor="f-year">Year</label>
          <input
            id="f-year"
            type="number"
            placeholder="e.g. 2026"
            value={filters.year}
            onChange={(e) => setFilters((f) => ({ ...f, year: e.target.value }))}
          />
        </div>
        <div className="field">
          <label htmlFor="f-status">Status</label>
          <select id="f-status" value={filters.status} onChange={(e) => setFilters((f) => ({ ...f, status: e.target.value }))}>
            <option value="">Any status</option>
            {PAYROLL_STATUSES.map((s) => (
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
        rows={payrolls}
        rowKey={(r) => r.payrollId}
        loading={loading}
        emptyTitle="No payroll records match this search"
        emptyHint="Try clearing the filters, or generate a new pay run."
      />

      <ConfirmDialog
        open={Boolean(pendingDelete)}
        title="Delete payroll record?"
        message={pendingDelete && `This removes ${pendingDelete.employeeName}'s ${pendingDelete.payMonth} ${pendingDelete.payYear} payroll.`}
        confirmLabel="Delete"
        danger
        onConfirm={confirmDelete}
        onCancel={() => setPendingDelete(null)}
      />

      {holdTarget && (
        <div className="modal-backdrop" onClick={() => setHoldTarget(null)}>
          <div className="modal-box" onClick={(e) => e.stopPropagation()}>
            <h3>Hold payroll</h3>
            <p>
              Putting {holdTarget.employeeName}'s {holdTarget.payMonth} {holdTarget.payYear} payroll on hold.
            </p>
            <div className="field field-full" style={{ marginBottom: 16 }}>
              <label htmlFor="holdRemarks">Remarks (optional)</label>
              <textarea id="holdRemarks" rows={3} value={holdRemarks} onChange={(e) => setHoldRemarks(e.target.value)} />
            </div>
            <div className="modal-actions">
              <button className="btn btn-outline btn-sm" onClick={() => setHoldTarget(null)}>
                Cancel
              </button>
              <button className="btn btn-primary btn-sm" onClick={submitHold}>
                Put on hold
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
