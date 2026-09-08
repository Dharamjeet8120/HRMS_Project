import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { generatePayroll } from "../../api/payrollApi";
import { getAllEmployees } from "../../api/employeeApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import { MONTHS } from "../../utils/constants";

const currentYear = new Date().getFullYear();
const currentMonth = MONTHS[new Date().getMonth()];

const emptyForm = {
  employeeId: "",
  payMonth: currentMonth,
  payYear: String(currentYear),
  allowances: "0",
  otherDeductions: "0",
  totalWorkingDays: "26",
};

export default function GeneratePayroll() {
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
    setSaving(true);

    try {
      await generatePayroll({
        employeeId: Number(form.employeeId),
        payMonth: form.payMonth,
        payYear: Number(form.payYear),
        allowances: Number(form.allowances || 0),
        otherDeductions: Number(form.otherDeductions || 0),
        totalWorkingDays: Number(form.totalWorkingDays),
      });
      navigate("/payroll");
    } catch (err) {
      setError(extractErrorMessage(err, "Could not generate this payroll."));
    } finally {
      setSaving(false);
    }
  }

  return (
    <div>
      <PageHeader title="Generate payroll" subtitle="Calculates gross and net salary from the employee's base pay and attendance" />

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
            <label htmlFor="payMonth">Pay month</label>
            <select id="payMonth" value={form.payMonth} onChange={(e) => update("payMonth", e.target.value)}>
              {MONTHS.map((m) => (
                <option key={m} value={m}>
                  {m}
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label htmlFor="payYear">Pay year</label>
            <input id="payYear" type="number" value={form.payYear} onChange={(e) => update("payYear", e.target.value)} required />
          </div>

          <div className="field">
            <label htmlFor="totalWorkingDays">Total working days</label>
            <input
              id="totalWorkingDays"
              type="number"
              min="1"
              value={form.totalWorkingDays}
              onChange={(e) => update("totalWorkingDays", e.target.value)}
              required
            />
          </div>
          <div />

          <div className="field">
            <label htmlFor="allowances">Allowances</label>
            <input id="allowances" type="number" min="0" step="0.01" value={form.allowances} onChange={(e) => update("allowances", e.target.value)} />
          </div>
          <div className="field">
            <label htmlFor="otherDeductions">Other deductions</label>
            <input
              id="otherDeductions"
              type="number"
              min="0"
              step="0.01"
              value={form.otherDeductions}
              onChange={(e) => update("otherDeductions", e.target.value)}
            />
          </div>
        </div>

        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={saving}>
            {saving ? "Generating…" : "Generate payroll"}
          </button>
          <button className="btn btn-outline" type="button" onClick={() => navigate("/payroll")}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
