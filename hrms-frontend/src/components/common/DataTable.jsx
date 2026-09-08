import Loader from "./Loader";
import EmptyState from "./EmptyState";

/**
 * A plain ledger-style table.
 *
 * columns: [{ key, header, render?: (row) => node, align?: "right" }]
 * rows: array of data objects
 * rowKey: (row) => string | number
 */
export default function DataTable({ columns, rows, rowKey, loading, emptyTitle, emptyHint }) {
  if (loading) return <Loader />;

  if (!rows || rows.length === 0) {
    return (
      <div className="ledger-wrap">
        <EmptyState title={emptyTitle || "No records found"} hint={emptyHint} />
      </div>
    );
  }

  return (
    <div className="ledger-wrap">
      <table className="ledger">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.key} style={col.align === "right" ? { textAlign: "right" } : undefined}>
                {col.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr key={rowKey(row)}>
              {columns.map((col) => (
                <td key={col.key} style={col.align === "right" ? { textAlign: "right" } : undefined}>
                  {col.render ? col.render(row) : row[col.key] ?? "—"}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
