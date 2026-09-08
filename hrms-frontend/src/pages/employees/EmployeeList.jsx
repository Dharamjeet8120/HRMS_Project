import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deleteEmployee, searchEmployees } from "../../api/employeeApi";
import { getAllDepartments } from "../../api/departmentApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import DataTable from "../../components/common/DataTable";
import StatusBadge from "../../components/common/StatusBadge";
import ConfirmDialog from "../../components/common/ConfirmDialog";
import { EMPLOYEE_STATUSES } from "../../utils/constants";
import { formatDate } from "../../utils/format";
import { useAuth } from "../../hooks/useAuth";

export default function EmployeeList() {
  const { isManager } = useAuth();

  const [employees, setEmployees] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [filters, setFilters] = useState({ name: "", departmentId: "", designation: "", status: "" });
  const [pendingDelete, setPendingDelete] = useState(null);
  const [deleteError, setDeleteError] = useState("");

  useEffect(() => {
    getAllDepartments().then(setDepartments).catch(() => {});
  }, []);

  async function runSearch(activeFilters = filters) {
    setLoading(true);
    setError("");
    try {
      const data = await searchEmployees(activeFilters);
      setEmployees(data);
    } catch (err) {
      setError(extractErrorMessage(err, "Could not load employees."));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    runSearch();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function handleFilterChange(field, value) {
    setFilters((f) => ({ ...f, [field]: value }));
  }

  function handleFilterSubmit(e) {
    e.preventDefault();
    runSearch(filters);
  }

  function resetFilters() {
    const cleared = { name: "", departmentId: "", designation: "", status: "" };
    setFilters(cleared);
    runSearch(cleared);
  }

  async function confirmDelete() {
    if (!pendingDelete) return;
    setDeleteError("");
    try {
      await deleteEmployee(pendingDelete.employeeId);
      setEmployees((list) => list.filter((e) => e.employeeId !== pendingDelete.employeeId));
      setPendingDelete(null);
    } catch (err) {
      setDeleteError(extractErrorMessage(err, "Could not delete this employee."));
    }
  }

  const columns = [
    { key: "employeeCode", header: "Code", render: (r) => <span className="cell-code">{r.employeeCode}</span> },
    {
      key: "name",
      header: "Name",
      render: (r) => (
        <span className="cell-primary">
          {r.firstName} {r.lastName}
        </span>
      ),
    },
    { key: "email", header: "Email" },
    { key: "departmentName", header: "Department" },
    { key: "designation", header: "Designation" },
    { key: "dateOfJoining", header: "Joined", render: (r) => formatDate(r.dateOfJoining) },
    { key: "status", header: "Status", render: (r) => <StatusBadge status={r.status} /> },
  ];

  if (isManager) {
    columns.push({
      key: "actions",
      header: "",
      align: "right",
      render: (r) => (
        <div className="cell-actions">
          <Link className="btn btn-outline btn-sm" to={`/employees/${r.employeeId}/edit`}>
            Edit
          </Link>
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
        title="Employees"
        subtitle={`${employees.length} record${employees.length === 1 ? "" : "s"}`}
        actions={
          isManager && (
            <Link className="btn btn-primary" to="/employees/new">
              + Add employee
            </Link>
          )
        }
      />

      <form className="filter-bar" onSubmit={handleFilterSubmit}>
        <div className="field">
          <label htmlFor="f-name">Name</label>
          <input
            id="f-name"
            placeholder="Search by name"
            value={filters.name}
            onChange={(e) => handleFilterChange("name", e.target.value)}
          />
        </div>
        <div className="field">
          <label htmlFor="f-dept">Department</label>
          <select
            id="f-dept"
            value={filters.departmentId}
            onChange={(e) => handleFilterChange("departmentId", e.target.value)}
          >
            <option value="">All departments</option>
            {departments.map((d) => (
              <option key={d.departmentId} value={d.departmentId}>
                {d.departmentName}
              </option>
            ))}
          </select>
        </div>
        <div className="field">
          <label htmlFor="f-desig">Designation</label>
          <input
            id="f-desig"
            placeholder="e.g. Software Engineer"
            value={filters.designation}
            onChange={(e) => handleFilterChange("designation", e.target.value)}
          />
        </div>
        <div className="field">
          <label htmlFor="f-status">Status</label>
          <select id="f-status" value={filters.status} onChange={(e) => handleFilterChange("status", e.target.value)}>
            <option value="">Any status</option>
            {EMPLOYEE_STATUSES.map((s) => (
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
      {deleteError && <div className="state-banner error">{deleteError}</div>}

      <DataTable
        columns={columns}
        rows={employees}
        rowKey={(r) => r.employeeId}
        loading={loading}
        emptyTitle="No employees match this search"
        emptyHint="Try clearing the filters, or add a new employee."
      />

      <ConfirmDialog
        open={Boolean(pendingDelete)}
        title="Delete employee?"
        message={
          pendingDelete && `This removes ${pendingDelete.firstName} ${pendingDelete.lastName} from the records. This can't be undone.`
        }
        confirmLabel="Delete"
        danger
        onConfirm={confirmDelete}
        onCancel={() => setPendingDelete(null)}
      />
    </div>
  );
}
