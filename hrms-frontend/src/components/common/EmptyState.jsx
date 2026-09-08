export default function EmptyState({ title = "Nothing here yet", hint, action }) {
  return (
    <div className="empty-state">
      <h3>{title}</h3>
      {hint && <p>{hint}</p>}
      {action}
    </div>
  );
}
