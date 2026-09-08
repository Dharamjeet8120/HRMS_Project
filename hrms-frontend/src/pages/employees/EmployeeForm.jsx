import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createEmployee, getEmployeeById, updateEmployee } from "../../api/employeeApi";
import { getAllDepartments } from "../../api/departmentApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import Loader from "../../components/common/Loader";
import { EMPLOYEE_STATUSES } from "../../utils/constants";

const emptyForm = {
  firstName: "",
  lastName: "",
  email: "",
  phoneNumber: "",
  employeeCode: "",
  designation: "",
  dateOfJoining: "",
  dateOfBirth: "",
  gender: "",
  address: "",
  salary: "",
  status: "ACTIVE",
  departmentId: "",
};

export default function EmployeeForm() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [form, setForm] = useState(emptyForm);
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    getAllDepartments().then(setDepartments).catch(() => {});
  }, []);

  useEffect(() => {
    if (!isEdit) return;
    setLoading(true);
    getEmployeeById(id)
      .then((emp) => {
        setForm({
          firstName: emp.firstName || "",
          lastName: emp.lastName || "",
          email: emp.email || "",
          phoneNumber: emp.phoneNumber || "",
          employeeCode: emp.employeeCode || "",
          designation: emp.designation || "",
          dateOfJoining: emp.dateOfJoining || "",
          dateOfBirth: emp.dateOfBirth || "",
          gender: emp.gender || "",
          address: emp.address || "",
          salary: emp.salary ?? "",
          status: emp.status || "ACTIVE",
          departmentId: emp.departmentId ? String(emp.departmentId) : "",
        });
      })
      .catch((err) => setError(extractErrorMessage(err, "Could not load this employee.")))
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
      firstName: form.firstName,
      lastName: form.lastName,
      email: form.email,
      phoneNumber: form.phoneNumber || null,
      employeeCode: form.employeeCode,
      designation: form.designation || null,
      dateOfJoining: form.dateOfJoining,
      dateOfBirth: form.dateOfBirth || null,
      gender: form.gender || null,
      address: form.address || null,
      salary: form.salary === "" ? null : Number(form.salary),
      status: form.status,
      departmentId: Number(form.departmentId),
    };

    try {
      if (isEdit) {
        await updateEmployee(id, payload);
      } else {
        await createEmployee(payload);
      }
      navigate("/employees");
    } catch (err) {
      setError(extractErrorMessage(err, "Could not save this employee."));
    } finally {
      setSaving(false);
    }
  }

  if (loading) return <Loader label="Loading employee…" />;

  return (
    <div>
      <PageHeader title={isEdit ? "Edit employee" : "Add employee"} />

      {error && <div className="state-banner error">{error}</div>}

      <form className="form-card" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="field">
            <label htmlFor="firstName">First name</label>
            <input id="firstName" value={form.firstName} onChange={(e) => update("firstName", e.target.value)} required />
          </div>
          <div className="field">
            <label htmlFor="lastName">Last name</label>
            <input id="lastName" value={form.lastName} onChange={(e) => update("lastName", e.target.value)} required />
          </div>

          <div className="field">
            <label htmlFor="email">Email</label>
            <input id="email" type="email" value={form.email} onChange={(e) => update("email", e.target.value)} required />
          </div>
          <div className="field">
            <label htmlFor="phoneNumber">Phone number</label>
            <input id="phoneNumber" value={form.phoneNumber} onChange={(e) => update("phoneNumber", e.target.value)} placeholder="+91 98765 43210" />
          </div>

          <div className="field">
            <label htmlFor="employeeCode">Employee code</label>
            <input id="employeeCode" value={form.employeeCode} onChange={(e) => update("employeeCode", e.target.value)} required />
          </div>
          <div className="field">
            <label htmlFor="designation">Designation</label>
            <input id="designation" value={form.designation} onChange={(e) => update("designation", e.target.value)} />
          </div>

          <div className="field">
            <label htmlFor="dateOfJoining">Date of joining</label>
            <input
              id="dateOfJoining"
              type="date"
              value={form.dateOfJoining}
              onChange={(e) => update("dateOfJoining", e.target.value)}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="dateOfBirth">Date of birth</label>
            <input id="dateOfBirth" type="date" value={form.dateOfBirth} onChange={(e) => update("dateOfBirth", e.target.value)} />
          </div>

          <div className="field">
            <label htmlFor="gender">Gender</label>
            <select id="gender" value={form.gender} onChange={(e) => update("gender", e.target.value)}>
              <option value="">Not specified</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
          </div>
          <div className="field">
            <label htmlFor="salary">Salary (monthly)</label>
            <input id="salary" type="number" min="0" step="0.01" value={form.salary} onChange={(e) => update("salary", e.target.value)} />
          </div>

          <div className="field">
            <label htmlFor="departmentId">Department</label>
            <select id="departmentId" value={form.departmentId} onChange={(e) => update("departmentId", e.target.value)} required>
              <option value="">Select a department</option>
              {departments.map((d) => (
                <option key={d.departmentId} value={d.departmentId}>
                  {d.departmentName}
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label htmlFor="status">Status</label>
            <select id="status" value={form.status} onChange={(e) => update("status", e.target.value)}>
              {EMPLOYEE_STATUSES.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </div>

          <div className="field field-full">
            <label htmlFor="address">Address</label>
            <textarea id="address" rows={3} value={form.address} onChange={(e) => update("address", e.target.value)} />
          </div>
        </div>

        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={saving}>
            {saving ? "Saving…" : isEdit ? "Save changes" : "Create employee"}
          </button>
          <button className="btn btn-outline" type="button" onClick={() => navigate("/employees")}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
