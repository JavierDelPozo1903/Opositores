/* ============================================================
   opositOS — Dashboard (HOY screen)
   Shows greeting, today's session, streak, progress, exam countdown
   ============================================================ */

function Dashboard({ onNavigate }) {
  const [overview, setOverview] = React.useState(null);
  const [oppositions, setOppositions] = React.useState([]);
  const [todaySessions, setTodaySessions] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const currentUser = API.auth.getUser();

  // Get week days for the strip
  const weekDays = React.useMemo(() => {
    const today = new Date();
    const dayOfWeek = today.getDay(); // 0=Sun
    const monday = new Date(today);
    monday.setDate(today.getDate() - ((dayOfWeek + 6) % 7));
    const labels = ['L','M','X','J','V','S','D'];
    return Array.from({ length: 7 }, (_, i) => {
      const d = new Date(monday);
      d.setDate(monday.getDate() + i);
      return {
        letter: labels[i],
        num: d.getDate(),
        isToday: d.toDateString() === today.toDateString(),
        date: d,
      };
    });
  }, []);

  React.useEffect(() => {
    const today = new Date().toISOString().split('T')[0];
    const nextWeek = new Date(Date.now() + 7 * 86400000).toISOString().split('T')[0];

    Promise.all([
      API.stats.overview(),
      API.oppositions.list(),
    ]).then(([ov, opps]) => {
      setOverview(ov);
      setOppositions(opps);
      // Load plan sessions for today from the first opposition
      if (opps.length > 0) {
        return API.planning.list(opps[0].id, today, nextWeek);
      }
      return [];
    }).then(sessions => {
      const today = new Date().toDateString();
      setTodaySessions((sessions || []).filter(s =>
        new Date(s.date).toDateString() === today
      ));
    }).catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  // Get first name only
  const firstName = currentUser?.name?.split(' ')[0]?.toLowerCase() || 'opositor';

  // Get closest exam
  const closestExam = oppositions.reduce((best, op) => {
    if (!op.targetExamDate) return best;
    const d = new Date(op.targetExamDate);
    if (!best || d < new Date(best.targetExamDate)) return op;
    return best;
  }, null);

  const daysUntilExam = closestExam
    ? Math.max(0, Math.ceil((new Date(closestExam.targetExamDate) - Date.now()) / 86400000))
    : null;

  const progressPercent = overview && overview.totalTopics > 0
    ? Math.round((overview.studiedTopics / overview.totalTopics) * 100)
    : 0;

  const todaySession = todaySessions[0];
  const greetings = [
    'hoy toca romper el día.',
    'el examen no se prepara solo.',
    'un tema más y ya.',
    'foco total, hoy sí.',
  ];
  const todayGreeting = greetings[new Date().getDay() % greetings.length];

  if (loading) {
    return (
      <div className="loading-wrap fadein">
        <div className="spinner"></div>
        <span className="loading-text">Cargando tu dashboard…</span>
      </div>
    );
  }

  return (
    <div>
      {/* Header greeting */}
      <div className="hoy-header fadein-1">
        <div className="hoy-greeting-sub">[ {new Date().toLocaleDateString('es-ES', { weekday:'long', day:'numeric', month:'long' }).toUpperCase()} ]</div>
        <div className="hoy-greeting">
          hola, <span className="name">{firstName}.</span><br/>
          <span className="verb">{todayGreeting}</span>
        </div>
      </div>

      {/* Week strip */}
      <div className="week-strip fadein-1">
        {weekDays.map((d, i) => (
          <div key={i} className={`day-pill${d.isToday ? ' today' : ''}`}>
            <span className="day-letter">{d.letter}</span>
            <span className="day-num">{d.num}</span>
            <span className="day-dot"></span>
          </div>
        ))}
      </div>

      {/* Today session */}
      {todaySession ? (
        <div className="session-card fadein-2">
          <div className="session-card-inner">
            <div className="session-tag">SESIÓN · {todaySession.plannedMinutes} MIN</div>
            <div className="session-title">{todaySession.topicTitle || 'Sesión de estudio'}</div>
            <div className="session-sub">
              {todaySession.status === 'COMPLETED'
                ? '✓ Completada hoy'
                : 'Sesión planificada para hoy'}
            </div>
            <div className="session-actions">
              {todaySession.status !== 'COMPLETED' && (
                <button
                  className="btn btn-primary"
                  onClick={() => {
                    API.planning.updateSession(todaySession.id, 'COMPLETED')
                      .then(() => setTodaySessions(ss =>
                        ss.map(s => s.id === todaySession.id ? { ...s, status: 'COMPLETED' } : s)
                      ));
                  }}>
                  ▶ Empezar
                </button>
              )}
              <button className="btn btn-ghost" onClick={() => onNavigate('plan')}>
                Ver plan →
              </button>
            </div>
          </div>
        </div>
      ) : oppositions.length === 0 ? (
        <div className="session-card fadein-2">
          <div className="session-card-inner">
            <div className="session-tag">EMPEZAR</div>
            <div className="session-title">Crea tu primera oposición</div>
            <div className="session-sub">Añade tu oposición y genera un plan de estudio personalizado con IA.</div>
            <div className="session-actions">
              <button className="btn btn-primary" onClick={() => onNavigate('oposiciones')}>
                ＋ Nueva oposición →
              </button>
            </div>
          </div>
        </div>
      ) : (
        <div className="session-card fadein-2">
          <div className="session-card-inner">
            <div className="session-tag">SIN SESIÓN HOY</div>
            <div className="session-title">Genera tu plan de estudio</div>
            <div className="session-sub">Deja que la IA cree tu plan semanal personalizado.</div>
            <div className="session-actions">
              <button className="btn btn-primary" onClick={() => onNavigate('plan')}>
                ⟳ Generar plan →
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Stats row */}
      {overview && (
        <div className="stats-row fadein-2">
          <div className="stat-card">
            <div className="stat-label">PROGRESO</div>
            <div className="stat-value">{progressPercent}%</div>
            <div className="stat-sub">{overview.studiedTopics} / {overview.totalTopics} TEMAS</div>
          </div>
          <div className="stat-card">
            <div className="stat-label">MEDIA TESTS</div>
            <div className="stat-value" style={{ color: 'var(--cyan)' }}>
              {overview.averageScore ? Math.round(overview.averageScore) : '—'}
            </div>
            <div className="stat-sub">{overview.totalTests} TEST{overview.totalTests !== 1 ? 'S' : ''} REALIZADOS</div>
          </div>
        </div>
      )}

      {/* Exam countdown */}
      {closestExam && daysUntilExam !== null && (
        <div className="exam-banner fadein-3">
          <div className="exam-banner-left">
            <strong>{closestExam.name}</strong>
            {new Date(closestExam.targetExamDate).toLocaleDateString('es-ES', { day: 'numeric', month: 'short', year: 'numeric' })}
          </div>
          <div style={{ textAlign: 'right' }}>
            <div className="exam-days">{daysUntilExam}</div>
            <div className="exam-days-label">DÍAS</div>
          </div>
        </div>
      )}

      {/* Oppositions quick list */}
      {oppositions.length > 0 && (
        <div>
          <div className="section-header fadein-3">
            <div>
              <div className="section-sub">MIS OPOSICIONES</div>
              <div className="section-title">En <span className="acid">curso</span></div>
            </div>
            <button className="btn btn-ghost" onClick={() => onNavigate('oposiciones')}>Ver todas →</button>
          </div>
          {oppositions.slice(0, 2).map(op => (
            <OppositionCard key={op.id} op={op} onClick={() => onNavigate('oposiciones')} />
          ))}
        </div>
      )}
    </div>
  );
}

function OppositionCard({ op, onClick }) {
  const scopeColors = {
    AGE: 'tag-acid', CCAA: 'tag-cyan', LOCAL: 'tag-violet',
    JUSTICIA: 'tag-orange', EDUCACION: 'tag-magenta',
    SANIDAD: 'tag-cyan', POLICIA: 'tag-orange', OTROS: 'tag-dim',
  };

  return (
    <div className="opp-card fadein" onClick={onClick}>
      <div className="opp-card-header">
        <span className={`tag ${scopeColors[op.scope] || 'tag-dim'}`}>{op.scope}</span>
        {op.targetExamDate && (
          <span className="opp-date">
            EXAMEN · {new Date(op.targetExamDate).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: '2-digit' })}
          </span>
        )}
      </div>
      <div className="opp-name">{op.name}</div>
      <div className="opp-meta">
        {op.hoursPerWeek ? `${op.hoursPerWeek}h/semana` : 'Sin horas definidas'}
      </div>
      <div className="progress-bar">
        <div className="progress-bar-fill" style={{ width: '30%' }}></div>
      </div>
    </div>
  );
}

window.Dashboard = Dashboard;
window.OppositionCard = OppositionCard;
