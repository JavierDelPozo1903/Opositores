/* ============================================================
   opositOS — API client
   Connects to Spring Boot backend at /api/*
   ============================================================ */

const API_BASE = '';

// ---------- Token storage ----------
const token = {
  get: () => localStorage.getItem('opositOS_token'),
  set: (t) => localStorage.setItem('opositOS_token', t),
  clear: () => localStorage.removeItem('opositOS_token'),
};
const user = {
  get: () => { try { return JSON.parse(localStorage.getItem('opositOS_user')); } catch { return null; } },
  set: (u) => localStorage.setItem('opositOS_user', JSON.stringify(u)),
  clear: () => localStorage.removeItem('opositOS_user'),
};

// ---------- Base fetch ----------
async function req(path, opts = {}) {
  const headers = { 'Content-Type': 'application/json', ...opts.headers };
  const t = token.get();
  if (t) headers['Authorization'] = `Bearer ${t}`;

  const res = await fetch(`${API_BASE}${path}`, { ...opts, headers });

  if (res.status === 401) {
    token.clear();
    user.clear();
    window.dispatchEvent(new Event('opositOS:logout'));
    throw new Error('Sesión expirada');
  }
  if (!res.ok) {
    let msg = `Error ${res.status}`;
    try { const d = await res.json(); msg = d.message || d.error || msg; } catch {}
    throw new Error(msg);
  }
  if (res.status === 204) return null;
  return res.json();
}

const get  = (path)       => req(path, { method: 'GET' });
const post = (path, body) => req(path, { method: 'POST', body: JSON.stringify(body) });
const put  = (path, body) => req(path, { method: 'PUT',  body: JSON.stringify(body) });
const patch = (path, body) => req(path, { method: 'PATCH', body: JSON.stringify(body) });
const del  = (path)       => req(path, { method: 'DELETE' });

// ---------- Auth ----------
const auth = {
  async login(email, password) {
    const data = await post('/auth/login', { email, password });
    token.set(data.token);
    user.set({ email: data.email, name: data.name, plan: data.subscriptionPlan });
    return data;
  },
  async register(email, password, name) {
    const data = await post('/auth/register', { email, password, name });
    token.set(data.token);
    user.set({ email: data.email, name: data.name, plan: data.subscriptionPlan });
    return data;
  },
  logout() {
    token.clear();
    user.clear();
    window.dispatchEvent(new Event('opositOS:logout'));
  },
  isLoggedIn: () => !!token.get(),
  getUser: () => user.get(),
};

// ---------- Oppositions ----------
const oppositions = {
  list: ()         => get('/oppositions'),
  get:  (id)       => get(`/oppositions/${id}`),
  create: (body)   => post('/oppositions', body),
  update: (id, b)  => put(`/oppositions/${id}`, b),
  delete: (id)     => del(`/oppositions/${id}`),
};

// ---------- Blocks ----------
const blocks = {
  list:   (oppId)       => get(`/oppositions/${oppId}/blocks`),
  create: (oppId, body) => post(`/oppositions/${oppId}/blocks`, body),
};

// ---------- Topics ----------
const topics = {
  list:   (blockId)        => get(`/blocks/${blockId}/topics`),
  create: (blockId, body)  => post(`/blocks/${blockId}/topics`, body),
  update: (topicId, body)  => put(`/topics/${topicId}`, body),
};

// ---------- Flashcards ----------
const flashcards = {
  list:     (topicId)      => get(`/topics/${topicId}/flashcards`),
  generate: (docId, n)     => post('/ai/flashcards', { documentId: docId, numCards: n }),
};

// ---------- Tests ----------
const tests = {
  create: (body)          => post('/tests', body),
  get:    (id)            => get(`/tests/${id}`),
  submit: (id, answers)   => post(`/tests/${id}/submit`, { answers }),
};

// ---------- Planning ----------
const planning = {
  generate: (oppId, body)  => post(`/oppositions/${oppId}/plan`, body),
  list: (oppId, from, to)  => get(`/oppositions/${oppId}/plan?from=${from}&to=${to}`),
  updateSession: (sid, status) => patch(`/study-sessions/${sid}`, { status }),
};

// ---------- Stats ----------
const stats = {
  overview: ()    => get('/stats/overview'),
  opposition: (id) => get(`/stats/opposition/${id}`),
};

// ---------- AI ----------
const ai = {
  summary:   (documentId, length = 'medium')     => post('/ai/summary', { documentId, length }),
  flashcards: (documentId, numCards = 10)         => post('/ai/flashcards', { documentId, numCards }),
  questions:  (documentId, numQuestions = 10, difficulty = 3) =>
                post('/ai/questions/mcq', { documentId, numQuestions, difficulty }),
  // Free-text prompt (calls summary with a prompt text approach)
  ask: (prompt) => post('/ai/summary', { prompt, length: 'long' }),
};

// ---------- Documents ----------
const documents = {
  list:   (topicId) => get(`/topics/${topicId}/documents`),
  upload: (topicId, file) => {
    const form = new FormData();
    form.append('file', file);
    const headers = {};
    const t = token.get();
    if (t) headers['Authorization'] = `Bearer ${t}`;
    return fetch(`${API_BASE}/topics/${topicId}/documents`, {
      method: 'POST', headers, body: form,
    }).then(r => r.json());
  },
};

// Expose globally (used by JSX loaded via Babel CDN)
window.API = { auth, oppositions, blocks, topics, flashcards, tests, planning, stats, ai, documents };
