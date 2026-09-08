import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { applyLeave } from "../../api/leaveApi";
import { getAllEmployees } from "../../api/employeeApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import { LEAVE_TYPES } from "../../utils/constants";

const emptyForm = {
  employeeId: "",
  leaveType: "CASUAL",
  startDate: "",
  endDate: "",
  reason: "",
};

export default function ApplyLeave() {
  const navigate = useNavigate();

  const [form, setForm] = useState(emptyForm);
  const [employees, setEmployees] = useState([]);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    getAllEmployees().then(setEmployees).catch(() => {});
  }, []);

  function update(field, value) {
    setForm((f) => ({ ...f, [field]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    if (form.endDate < form.startDate) {
      setError("End date can't be before the start date.");
      return;
    }

    setSaving(true);
    try {
      await applyLeave({
        employeeId: Number(form.employeeId),
        leaveType: form.leaveType,
        startDate: form.startDate,
        endDate: form.endDate,
        reason: form.reason,
      });
      navigate("/leaves");
    } catch (err) {
      setError(extractErrorMessage(err, "Could not submit this leave request."));
    } finally {
      setSaving(false);
    }
  }

  return (
    <div>
      <PageHeader title="Apply for leave" />

      {error && <div className="state-banner error">{error}</div>}

      <form className="form-card" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="field field-full">
            <label htmlFor="employeeId">Employee</label>
            <select id="employeeId" value={form.employeeId} onChange={(e) => update("employeeId", e.target.value)} required>
              <option value="">Select an employee</option>
              {employees.map((e) => (
                <option key={e.employeeId} value={e.employeeId}>
                  {e.firstName} {e.lastName} ({e.employeeCode})
                </option>
              ))}
            </select>
          </div>

          <div className="field">
            <label htmlFor="leaveType">Leave type</label>
            <select id="leaveType" value={form.leaveType} onChange={(e) => update("leaveType", e.target.value)}>
              {LEAVE_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
          </div>
          <div />

          <div className="field">
            <label htmlFor="startDate">Start date</label>
            <input id="startDate" type="date" value={form.startDate} onChange={(e) => update("startDate", e.target.value)} required />
          </div>
          <div className="field">
            <label htmlFor="endDate">End date</label>
            <input id="endDate" type="date" value={form.endDate} onChange={(e) => update("endDate", e.target.value)} required />
          </div>

          <div className="field field-full">
            <label htmlFor="reason">Reason</label>
            <textarea id="reason" rows={3} value={form.reason} onChange={(e) => update("reason", e.target.value)} required />
          </div>
        </div>

        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={saving}>
            {saving ? "Submitting…" : "Submit request"}
          </button>
          <button className="btn btn-outline" type="button" onClick={() => navigate("/leaves")}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
