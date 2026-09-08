import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getAttendanceById, markAttendance, updateAttendance } from "../../api/attendanceApi";
import { getAllEmployees } from "../../api/employeeApi";
import { extractErrorMessage } from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import Loader from "../../components/common/Loader";
import { ATTENDANCE_STATUSES } from "../../utils/constants";

const emptyForm = {
  employeeId: "",
  attendanceDate: new Date().toISOString().slice(0, 10),
  checkInTime: "",
  checkOutTime: "",
  status: "PRESENT",
  remarks: "",
};

export default function MarkAttendance() {
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
    getAttendanceById(id)
      .then((att) => {
        setForm({
          employeeId: att.employeeId ? String(att.employeeId) : "",
          attendanceDate: att.attendanceDate || "",
          checkInTime: att.checkInTime || "",
          checkOutTime: att.checkOutTime || "",
          status: att.status || "PRESENT",
          remarks: att.remarks || "",
        });
      })
      .catch((err) => setError(extractErrorMessage(err, "Could not load this attendance record.")))
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
      employeeId: Number(form.employeeId),
      attendanceDate: form.attendanceDate,
      checkInTime: form.checkInTime || null,
      checkOutTime: form.checkOutTime || null,
      status: form.status,
      remarks: form.remarks || null,
    };

    try {
      if (isEdit) {
        await updateAttendance(id, payload);
      } else {
        await markAttendance(payload);
      }
      navigate("/attendance");
    } catch (err) {
      setError(extractErrorMessage(err, "Could not save this attendance record."));
    } finally {
      setSaving(false);
    }
  }

  if (loading) return <Loader label="Loading record…" />;

  return (
    <div>
      <PageHeader title={isEdit ? "Edit attendance" : "Mark attendance"} />

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
            <label htmlFor="attendanceDate">Date</label>
            <input id="attendanceDate" type="date" value={form.attendanceDate} onChange={(e) => update("attendanceDate", e.target.value)} required />
          </div>
          <div className="field">
            <label htmlFor="status">Status</label>
            <select id="status" value={form.status} onChange={(e) => update("status", e.target.value)}>
              {ATTENDANCE_STATUSES.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </div>

          <div className="field">
            <label htmlFor="checkInTime">Check-in time</label>
            <input id="checkInTime" type="time" value={form.checkInTime} onChange={(e) => update("checkInTime", e.target.value)} />
          </div>
          <div className="field">
            <label htmlFor="checkOutTime">Check-out time</label>
            <input id="checkOutTime" type="time" value={form.checkOutTime} onChange={(e) => update("checkOutTime", e.target.value)} />
          </div>

          <div className="field field-full">
            <label htmlFor="remarks">Remarks</label>
            <textarea id="remarks" rows={2} value={form.remarks} onChange={(e) => update("remarks", e.target.value)} />
          </div>
        </div>

        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={saving}>
            {saving ? "Saving…" : isEdit ? "Save changes" : "Mark attendance"}
          </button>
          <button className="btn btn-outline" type="button" onClick={() => navigate("/attendance")}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
