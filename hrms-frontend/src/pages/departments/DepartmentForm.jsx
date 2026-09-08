import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createDepartment, getDepartmentById, updateDepartment } from "../../api/departmentApi";
import { getAllEmployees } from "../../api/employeeApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import Loader from "../../components/common/Loader";
import { DEPARTMENT_STATUSES } from "../../utils/constants";

const emptyForm = {
  departmentName: "",
  departmentCode: "",
  departmentHeadId: "",
  description: "",
  location: "",
  contactNumber: "",
  email: "",
  status: "ACTIVE",
};

export default function DepartmentForm() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [form, setForm] = useState(emptyForm);
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    getAllEmployees().then(setEmployees).catch(() => {});
  }, []);

  useEffect(() => {
    if (!isEdit) return;
    setLoading(true);
    getDepartmentById(id)
      .then((dept) => {
        setForm({
          departmentName: dept.departmentName || "",
          departmentCode: dept.departmentCode || "",
          departmentHeadId: dept.departmentHeadId ? String(dept.departmentHeadId) : "",
          description: dept.description || "",
          location: dept.location || "",
          contactNumber: dept.contactNumber || "",
          email: dept.email || "",
          status: dept.status || "ACTIVE",
        });
      })
      .catch((err) => setError(extractErrorMessage(err, "Could not load this department.")))
      .finally(() => setLoading(false));
  }, [id, isEdit]);

  function update(field, value) {
    setForm((f) => ({ ...f, [field]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSaving(true);

    const payload = {
      departmentName: form.departmentName,
      departmentCode: form.departmentCode,
      departmentHeadId: form.departmentHeadId ? Number(form.departmentHeadId) : null,
      description: form.description || null,
      location: form.location || null,
      contactNumber: form.contactNumber || null,
      email: form.email || null,
      status: form.status,
    };

    try {
      if (isEdit) {
        await updateDepartment(id, payload);
      } else {
        await createDepartment(payload);
      }
      navigate("/departments");
    } catch (err) {
      setError(extractErrorMessage(err, "Could not save this department."));
    } finally {
      setSaving(false);
    }
  }

  if (loading) return <Loader label="Loading department…" />;

  return (
    <div>
      <PageHeader title={isEdit ? "Edit department" : "Add department"} />

      {error && <div className="state-banner error">{error}</div>}

      <form className="form-card" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="field">
            <label htmlFor="departmentName">Department name</label>
            <input
              id="departmentName"
              value={form.departmentName}
              onChange={(e) => update("departmentName", e.target.value)}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="departmentCode">Code</label>
            <input
              id="departmentCode"
              maxLength={10}
              value={form.departmentCode}
              onChange={(e) => update("departmentCode", e.target.value.toUpperCase())}
              required
            />
          </div>

          <div className="field">
            <label htmlFor="departmentHeadId">Department head</label>
            <select id="departmentHeadId" value={form.departmentHeadId} onChange={(e) => update("departmentHeadId", e.target.value)}>
              <option value="">No head assigned</option>
              {employees.map((e) => (
                <option key={e.employeeId} value={e.employeeId}>
                  {e.firstName} {e.lastName} ({e.employeeCode})
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label htmlFor="location">Location</label>
            <input id="location" value={form.location} onChange={(e) => update("location", e.target.value)} />
          </div>

          <div className="field">
            <label htmlFor="contactNumber">Contact number</label>
            <input id="contactNumber" value={form.contactNumber} onChange={(e) => update("contactNumber", e.target.value)} placeholder="+91 98765 43210" />
          </div>
          <div className="field">
            <label htmlFor="email">Email</label>
            <input id="email" type="email" value={form.email} onChange={(e) => update("email", e.target.value)} />
          </div>

          <div className="field">
            <label htmlFor="status">Status</label>
            <select id="status" value={form.status} onChange={(e) => update("status", e.target.value)}>
              {DEPARTMENT_STATUSES.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </div>

          <div className="field field-full">
            <label htmlFor="description">Description</label>
            <textarea id="description" rows={3} value={form.description} onChange={(e) => update("description", e.target.value)} />
          </div>
        </div>

        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={saving}>
            {saving ? "Saving…" : isEdit ? "Save changes" : "Create department"}
          </button>
          <button className="btn btn-outline" type="button" onClick={() => navigate("/departments")}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
