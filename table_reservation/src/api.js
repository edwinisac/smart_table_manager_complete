const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/TableBook/api';
export async function api(path, options = {}) {
  const token = localStorage.getItem('tablebook_token');
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  if (token) headers.Authorization = `Bearer ${token}`;
  const response = await fetch(`${API_BASE}${path}`, { ...options, headers });
  const payload = response.status === 204 ? null : await response.json().catch(() => ({}));
  if (!response.ok) throw new Error(payload?.error || 'Request failed');
  return payload;
}
