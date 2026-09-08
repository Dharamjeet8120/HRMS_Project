import { toneFor } from "../../utils/constants";
import { formatStatusLabel } from "../../utils/format";

export default function StatusBadge({ status }) {
  if (!status) return <span>—</span>;
  return <span className={`badge badge-${toneFor(status)}`}>{formatStatusLabel(status)}</span>;
}
