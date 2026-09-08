export const EMPLOYEE_STATUSES = ["ACTIVE", "INACTIVE", "ON_LEAVE", "TERMINATED"];
export const DEPARTMENT_STATUSES = ["ACTIVE", "INACTIVE"];
export const ATTENDANCE_STATUSES = ["PRESENT", "ABSENT", "HALF_DAY", "ON_LEAVE", "HOLIDAY"];
export const LEAVE_TYPES = ["CASUAL", "SICK", "EARNED", "MATERNITY", "PATERNITY", "UNPAID"];
export const LEAVE_STATUSES = ["PENDING", "APPROVED", "REJECTED", "CANCELLED"];
export const PAYROLL_STATUSES = ["GENERATED", "PAID", "ON_HOLD"];
export const ROLE_OPTIONS = ["ROLE_ADMIN", "ROLE_HR", "ROLE_EMPLOYEE"];

export const MONTHS = [
  "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
  "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER",
];

// Maps a status string to one of the four badge colour tokens defined
// in index.css, grouped by what the status broadly signals.
export const STATUS_TONE = {
  ACTIVE: "forest",
  PRESENT: "forest",
  APPROVED: "forest",
  PAID: "forest",
  GENERATED: "brass",
  PENDING: "brass",
  HALF_DAY: "brass",
  ON_HOLD: "brass",
  ON_LEAVE: "brass",
  INACTIVE: "slate",
  HOLIDAY: "slate",
  CANCELLED: "slate",
  ABSENT: "rust",
  REJECTED: "rust",
  TERMINATED: "rust",
};

export function toneFor(status) {
  return STATUS_TONE[status] || "slate";
}
