import { createContext, useContext, useEffect, useState } from 'react';
import { api } from '../api';
const AuthContext = createContext(null);
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  useEffect(() => { if (localStorage.getItem('tablebook_token')) api('/auth/me').then(setUser).catch(() => { localStorage.removeItem('tablebook_token'); localStorage.removeItem('tablebook_user'); }).finally(() => setLoading(false)); else setLoading(false); }, []);
  const login = async (username, password) => { const data = await api('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) }); localStorage.setItem('tablebook_token', data.token); localStorage.setItem('tablebook_user', JSON.stringify(data.user)); setUser(data.user); return data.user; };
  const register = async (payload) => api('/auth/register', { method: 'POST', body: JSON.stringify(payload) });
  const logout = async () => { try { await api('/auth/logout', { method: 'POST', body: '{}' }); } catch {} localStorage.removeItem('tablebook_token'); localStorage.removeItem('tablebook_user'); setUser(null); };
  return <AuthContext.Provider value={{ user, loading, login, register, logout, setUser }}>{children}</AuthContext.Provider>;
}
export function useAuth() { return useContext(AuthContext); }
