/* ============================================================
   opositOS — Main App
   Auth state + bottom navigation + screen routing
   ============================================================ */

/* ==================== LOGIN / REGISTER ==================== */
function AuthScreen({ onAuthenticated }) {
  const [mode, setMode] = React.useState('login'); // login | register
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [name, setName] = React.useState('');
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');

  const submit = () => {
    setError('');
    if (!email || !password) { setError('Rellena todos los campos'); return; }
    if (mode === 'register' && !name) { setError('El nombre es obligatorio'); return; }
    if (password.length < 8) { setError('La contraseña debe tener al menos 8 caracteres'); return; }

    setLoading(true);
    const call = mode === 'login'
      ? API.auth.login(email, password)
      : API.auth.register(email, password, name);

    call.then(onAuthenticated)
      .catch(e => { setError(e.message || 'Error al conectar'); setLoading(false); });
  };

  return (
    <div className="auth-screen">
      <div className="auth-hero">
        <div className="auth-logo">O</div>
        <div className="auth-title">opositOS <span className="acid">/</span><br/>
          <span className="acid" style={{ fontStyle: 'italic' }}>tu academia</span></div>
        <div className="auth-sub">Prepara tu oposición con IA · v2.0</div>
      </div>

      <div className="auth-form">
        <div className="auth-form-title">
          {mode === 'login' ? 'Iniciar sesión' : 'Crear cuenta'}
        </div>

        {error && <div className="auth-error">{error}</div>}

        {mode === 'register' && (
          <div className="field">
            <label>Nombre completo</label>
            <input placeholder="María García López"
              value={name} onChange={e => setName(e.target.value)} />
          </div>
        )}

        <div className="field">
          <label>Email</label>
          <input type="email" placeholder="tu@correo.es"
            value={email} onChange={e => setEmail(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && submit()} />
        </div>

        <div className="field">
          <label>Contraseña</label>
          <input type="password" placeholder="Mínimo 8 caracteres"
            value={password} onChange={e => setPassword(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && submit()} />
        </div>

        <button className="btn btn-primary btn-lg" onClick={submit} disabled={loading}
          style={{ marginTop: 8 }}>
          {loading ? 'Conectando…' : mode === 'login' ? 'Entrar →' : 'Crear cuenta →'}
        </button>

        <div className="auth-toggle">
          {mode === 'login'
            ? <>¿Sin cuenta? <span onClick={() => { setMode('register'); setError(''); }}>Regístrate gratis</span></>
            : <>¿Ya tienes cuenta? <span onClick={() => { setMode('login'); setError(''); }}>Iniciar sesión</span></>
          }
        </div>
      </div>
    </div>
  );
}

/* ==================== MAIN APP ==================== */
function App() {
  const [authed, setAuthed] = React.useState(API.auth.isLoggedIn());
  const [tab, setTab] = React.useState('hoy');

  // Listen for logout events (e.g. 401 from API)
  React.useEffect(() => {
    const handler = () => setAuthed(false);
    window.addEventListener('opositOS:logout', handler);
    return () => window.removeEventListener('opositOS:logout', handler);
  }, []);

  if (!authed) {
    return <AuthScreen onAuthenticated={() => setAuthed(true)} />;
  }

  const tabs = [
    { id: 'hoy',       label: 'HOY',   icon: '◉' },
    { id: 'test',      label: 'TEST',  icon: '✎' },
    { id: 'flash',     label: 'FLASH', icon: '⬡' },
    { id: 'plan',      label: 'PLAN',  icon: '◈' },
    { id: 'oposiciones', label: 'YO',  icon: '◎' },
  ];

  const renderTab = () => {
    switch (tab) {
      case 'hoy':        return <Dashboard onNavigate={setTab} />;
      case 'test':       return <TestsView />;
      case 'flash':      return <FlashcardsView />;
      case 'plan':       return <PlanView />;
      case 'oposiciones': return <OposicionesView onNavigate={setTab} />;
      default:           return null;
    }
  };

  const currentUser = API.auth.getUser();

  return (
    <div className="app-shell">
      {/* Top bar */}
      <div className="topbar">
        <span className="topbar-logo">opositOS</span>
        <div className="topbar-actions">
          {currentUser && (
            <span style={{ fontSize: 12, color: 'var(--ink-dim)', fontFamily: 'var(--font-mono)' }}>
              {currentUser.name?.split(' ')[0]}
            </span>
          )}
          <button className="btn btn-ghost" style={{ padding: '5px 10px', fontSize: 12 }}
            onClick={() => { API.auth.logout(); setAuthed(false); }}>
            Salir
          </button>
        </div>
      </div>

      {/* Main content */}
      <div className="page">
        {renderTab()}
      </div>

      {/* Bottom navigation */}
      <nav className="bottom-nav">
        {tabs.map(t => (
          <div key={t.id}
            className={`nav-tab${tab === t.id ? ' active' : ''}`}
            onClick={() => setTab(t.id)}>
            <span className="nav-tab-icon">{t.icon}</span>
            <span className="nav-tab-label">{t.label}</span>
          </div>
        ))}
      </nav>
    </div>
  );
}

// Mount
ReactDOM.createRoot(document.getElementById('root')).render(<App />);
