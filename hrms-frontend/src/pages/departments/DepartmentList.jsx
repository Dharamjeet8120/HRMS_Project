import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { deleteDepartment, searchDepartments } from "../../api/departmentApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import DataTable from "../../components/common/DataTable";
import StatusBadge from "../../components/common/StatusBadge";
import ConfirmDialog from "../../components/common/ConfirmDialog";
import { useAuth } from "../../hooks/useAuth";

export default function DepartmentList() {
  const { isManager } = useAuth();

  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [filters, setFilters] = useState({ name: "", code: "", head: "" });
  const [pendingDelete, setPendingDelete] = useState(null);
  const [deleteError, setDeleteError] = useState("");

  async function runSearch(activeFilters = filters) {
    setLoading(true);
    setError("");
    try {
      const data = await searchDepartments(activeFilters);
      setDepartments(data);
    } catch (err) {
      setError(extractErrorMessage(err, "Could not load departments."));
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
    const cleared = { name: "", code: "", head: "" };
    setFilters(cleared);
    runSearch(cleared);
  }

  async function confirmDelete() {
    if (!pendingDelete) return;
    setDeleteError("");
    try {
      await deleteDepartment(pendingDelete.departmentId);
      setDepartments((list) => list.filter((d) => d.departmentId !== pendingDelete.departmentId));
      setPendingDelete(null);
    } catch (err) {
      setDeleteError(extractErrorMessage(err, "Could not delete this department."));
    }
  }

  const columns = [
    { key: "departmentCode", header: "Code", render: (r) => <span className="cell-code">{r.departmentCode}</span> },
    { key: "departmentName", header: "Department", render: (r) => <span className="cell-primary">{r.departmentName}</span> },
    { key: "departmentHeadName", header: "Head", render: (r) => r.departmentHeadName || "—" },
    { key: "location", header: "Location" },
    { key: "email", header: "Email" },
    { key: "status", header: "Status", render: (r) => <StatusBadge status={r.status} /> },
  ];

  if (isManager) {
    columns.push({
      key: "actions",
      header: "",
      align: "right",
      render: (r) => (
        <div className="cell-actions">
          <Link className="btn btn-outline btn-sm" to={`/departments/${r.departmentId}/edit`}>
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
        title="Departments"
        subtitle={`${departments.length} record${departments.length === 1 ? "" : "s"}`}
        actions={
          isManager && (
            <Link className="btn btn-primary" to="/departments/new">
              + Add department
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
            onChange={(e) => setFilters((f) => ({ ...f, name: e.target.value }))}
          />
        </div>
        <div className="field">
          <label htmlFor="f-code">Code</label>
          <input
            id="f-code"
            placeholder="e.g. ENG"
            value={filters.code}
            onChange={(e) => setFilters((f) => ({ ...f, code: e.target.value }))}
          />
        </div>
        <div className="field">
          <label htmlFor="f-head">Department head</label>
          <input
            id="f-head"
            placeholder="Search by head's name"
            value={filters.head}
            onChange={(e) => setFilters((f) => ({ ...f, head: e.target.value }))}
          />
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
        rows={departments}
        rowKey={(r) => r.departmentId}
        loading={loading}
        emptyTitle="No departments match this search"
        emptyHint="Try clearing the filters, or add a new department."
      />

      <ConfirmDialog
        open={Boolean(pendingDelete)}
        title="Delete department?"
        message={pendingDelete && `This removes "${pendingDelete.departmentName}" from the records. This can't be undone.`}
        confirmLabel="Delete"
        danger
        onConfirm={confirmDelete}
        onCancel={() => setPendingDelete(null)}
      />
    </div>
  );
}
