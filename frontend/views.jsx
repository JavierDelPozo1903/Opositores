/* ============================================================
   opositOS — Main views (5 tabs)
   Oposiciones · Tests · Flashcards · Plan · IA
   All connected to Spring Boot backend via window.API
   ============================================================ */

/* ==================== OPOSICIONES ==================== */
function OposicionesView({ onNavigate }) {
  const [oppositions, setOppositions] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [showModal, setShowModal] = React.useState(false);
  const [selectedOp, setSelectedOp] = React.useState(null);

  const load = () => {
    setLoading(true);
    API.oppositions.list()
      .then(setOppositions)
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  React.useEffect(load, []);

  if (loading) return <div className="loading-wrap"><div className="spinner"></div><span className="loading-text">Cargando…</span></div>;

  return (
    <div>
      <div className="section-header fadein-1">
        <div>
          <div className="section-sub">[ {oppositions.length} ACTIVAS ]</div>
          <div className="section-title">Mis <span className="acid">oposiciones</span></div>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>＋ Nueva</button>
      </div>

      {oppositions.length === 0 ? (
        <div className="empty-state fadein">
          <div className="empty-emoji">🎯</div>
          <div className="empty-title">Sin oposiciones aún</div>
          <div className="empty-desc">Crea tu primera oposición para empezar a prepararte.</div>
          <button className="btn btn-primary" onClick={() => setShowModal(true)}>＋ Crear oposición</button>
        </div>
      ) : (
        oppositions.map(op => (
          <OposicionDetailCard
            key={op.id}
            op={op}
            onDelete={() => {
              API.oppositions.delete(op.id).then(load).catch(console.error);
            }}
            onOpen={() => setSelectedOp(op)}
          />
        ))
      )}

      {showModal && (
        <NuevaOposicionModal
          onClose={() => setShowModal(false)}
          onCreated={() => { setShowModal(false); load(); }}
        />
      )}

      {selectedOp && (
        <OposicionDetailModal
          op={selectedOp}
          onClose={() => setSelectedOp(null)}
        />
      )}
    </div>
  );
}

function OposicionDetailCard({ op, onDelete, onOpen }) {
  const scopeColors = {
    AGE: 'tag-acid', CCAA: 'tag-cyan', LOCAL: 'tag-violet',
    JUSTICIA: 'tag-orange', EDUCACION: 'tag-magenta',
    SANIDAD: 'tag-cyan', POLICIA: 'tag-orange', OTROS: 'tag-dim',
  };
  return (
    <div className="opp-card fadein" onClick={onOpen}>
      <div className="opp-card-header">
        <span className={`tag ${scopeColors[op.scope] || 'tag-dim'}`}>{op.scope}</span>
        {op.targetExamDate && (
          <span className="opp-date">EXAMEN · {new Date(op.targetExamDate).toLocaleDateString('es-ES', { day:'2-digit', month:'2-digit', year:'2-digit' })}</span>
        )}
      </div>
      <div className="opp-name">{op.name}</div>
      <div className="opp-meta">{op.hoursPerWeek ? `${op.hoursPerWeek}h/semana` : ''}</div>
      <div className="row mt-sm" style={{ justifyContent: 'flex-end' }}>
        <button className="btn btn-ghost" style={{ fontSize: 12, padding: '6px 12px' }}
          onClick={e => { e.stopPropagation(); onDelete(); }}>
          Eliminar
        </button>
        <button className="btn btn-dark" style={{ fontSize: 12, padding: '6px 12px' }}
          onClick={e => { e.stopPropagation(); onOpen(); }}>
          Abrir →
        </button>
      </div>
    </div>
  );
}

function NuevaOposicionModal({ onClose, onCreated }) {
  const [form, setForm] = React.useState({ name: '', scope: 'AGE', targetExamDate: '', hoursPerWeek: 10 });
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');
  const scopes = ['AGE','CCAA','LOCAL','JUSTICIA','EDUCACION','SANIDAD','POLICIA','OTROS'];

  const submit = () => {
    if (!form.name.trim()) { setError('El nombre es obligatorio'); return; }
    setLoading(true);
    API.oppositions.create(form)
      .then(onCreated)
      .catch(e => { setError(e.message); setLoading(false); });
  };

  return (
    <div className="modal-backdrop" onClick={e => e.target === e.currentTarget && onClose()}>
      <div className="modal fadein">
        <div className="modal-handle"></div>
        <div className="modal-title">Nueva oposición</div>
        {error && <div className="auth-error">{error}</div>}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          <div className="field">
            <label>Nombre</label>
            <input placeholder="Ej. Auxiliar Administrativo AGE"
              value={form.name}
              onChange={e => setForm(f => ({ ...f, name: e.target.value }))} />
          </div>
          <div className="field">
            <label>Ámbito</label>
            <select value={form.scope} onChange={e => setForm(f => ({ ...f, scope: e.target.value }))}>
              {scopes.map(s => <option key={s} value={s}>{s}</option>)}
            </select>
          </div>
          <div className="field">
            <label>Fecha de examen</label>
            <input type="date" value={form.targetExamDate}
              onChange={e => setForm(f => ({ ...f, targetExamDate: e.target.value }))} />
          </div>
          <div className="field">
            <label>Horas por semana</label>
            <input type="number" min="1" max="60" value={form.hoursPerWeek}
              onChange={e => setForm(f => ({ ...f, hoursPerWeek: parseFloat(e.target.value) }))} />
          </div>
          <div className="row" style={{ gap: 10, marginTop: 6 }}>
            <button className="btn btn-ghost" style={{ flex: 1 }} onClick={onClose}>Cancelar</button>
            <button className="btn btn-primary" style={{ flex: 1 }} onClick={submit} disabled={loading}>
              {loading ? 'Creando…' : 'Crear oposición'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

function OposicionDetailModal({ op, onClose }) {
  const [blocks, setBlocks] = React.useState([]);
  const [openBlocks, setOpenBlocks] = React.useState({});
  const [topics, setTopics] = React.useState({});
  const [loading, setLoading] = React.useState(true);
  const [stats, setStats] = React.useState(null);

  React.useEffect(() => {
    Promise.all([
      API.blocks.list(op.id),
      API.stats.opposition(op.id),
    ]).then(([blks, st]) => {
      setBlocks(blks);
      setStats(st);
    }).catch(console.error).finally(() => setLoading(false));
  }, [op.id]);

  const toggleBlock = (blockId) => {
    setOpenBlocks(prev => {
      const next = { ...prev, [blockId]: !prev[blockId] };
      if (next[blockId] && !topics[blockId]) {
        API.topics.list(blockId).then(ts => setTopics(t => ({ ...t, [blockId]: ts }))).catch(console.error);
      }
      return next;
    });
  };

  const statusLabel = { NOT_STARTED: 'PEND.', STUDYING: 'ESTUD.', REVIEWED: 'REPAS.', MASTERED: 'DOMIN.' };
  const statusClass = { NOT_STARTED: 'status-not-started', STUDYING: 'status-studying', REVIEWED: 'status-reviewed', MASTERED: 'status-mastered' };

  return (
    <div className="modal-backdrop" onClick={e => e.target === e.currentTarget && onClose()}>
      <div className="modal fadein" style={{ borderRadius: '28px 28px 0 0', maxHeight: '92vh' }}>
        <div className="modal-handle"></div>
        <div className="row" style={{ justifyContent: 'space-between', marginBottom: 16 }}>
          <div className="modal-title" style={{ margin: 0 }}>{op.name}</div>
          <button className="btn btn-ghost" style={{ padding: '6px 12px' }} onClick={onClose}>✕</button>
        </div>

        {stats && (
          <div className="stats-grid" style={{ padding: 0, marginBottom: 20 }}>
            <div className="stats-metric">
              <div className="val" style={{ color: 'var(--acid)' }}>{Math.round(stats.progressPercent || 0)}%</div>
              <div className="lbl">Progreso</div>
            </div>
            <div className="stats-metric">
              <div className="val" style={{ color: 'var(--cyan)' }}>{stats.reviewedTopics || 0}</div>
              <div className="lbl">Temas repasados</div>
            </div>
          </div>
        )}

        {loading ? <div className="loading-wrap"><div className="spinner"></div></div> : (
          blocks.length === 0 ? (
            <div className="empty-state">
              <div className="empty-desc">Sin bloques configurados. Añade bloques y temas desde el backend.</div>
            </div>
          ) : (
            <div className="topics-section">
              {blocks.map(b => (
                <div key={b.id} className="block-row">
                  <div className="block-header" onClick={() => toggleBlock(b.id)}>
                    <div>
                      <div className="block-name">{b.name}</div>
                    </div>
                    <span className={`block-chevron${openBlocks[b.id] ? ' open' : ''}`}>⌄</span>
                  </div>
                  {openBlocks[b.id] && (
                    <div className="topics-list">
                      {!topics[b.id] ? (
                        <div style={{ padding: '12px 18px' }}><div className="spinner" style={{ width: 16, height: 16 }}></div></div>
                      ) : topics[b.id].length === 0 ? (
                        <div style={{ padding: '12px 18px', color: 'var(--ink-dim)', fontSize: 12 }}>Sin temas</div>
                      ) : (
                        topics[b.id].map(t => (
                          <div key={t.id} className="topic-row">
                            <span className="topic-num">{t.officialNumber || t.id}</span>
                            <div>
                              <div className="topic-title">{t.title}</div>
                              <div className="topic-review">{t.reviewCount} REPASOS</div>
                            </div>
                            <div className={`status-dot ${statusClass[t.status]}`}></div>
                            <span className="topic-arrow">›</span>
                          </div>
                        ))
                      )}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )
        )}
      </div>
    </div>
  );
}

/* ==================== TESTS ==================== */
function TestsView() {
  const [phase, setPhase] = React.useState('setup'); // setup | running | results
  const [oppositions, setOppositions] = React.useState([]);
  const [selectedOp, setSelectedOp] = React.useState('');
  const [numQuestions, setNumQuestions] = React.useState(10);
  const [mode, setMode] = React.useState('PRACTICE');
  const [loading, setLoading] = React.useState(false);
  const [testData, setTestData] = React.useState(null);
  const [questions, setQuestions] = React.useState([]);
  const [qIdx, setQIdx] = React.useState(0);
  const [selected, setSelected] = React.useState(null);
  const [revealed, setRevealed] = React.useState(false);
  const [answers, setAnswers] = React.useState([]);
  const [results, setResults] = React.useState(null);
  const [time, setTime] = React.useState(0);

  React.useEffect(() => {
    API.oppositions.list().then(ops => {
      setOppositions(ops);
      if (ops.length > 0) setSelectedOp(ops[0].id);
    }).catch(console.error);
  }, []);

  React.useEffect(() => {
    if (phase !== 'running' || mode !== 'EXAM_SIMULATION') return;
    const limit = numQuestions * 1.5 * 60;
    setTime(limit);
    const t = setInterval(() => setTime(s => {
      if (s <= 1) { clearInterval(t); handleSubmit(); return 0; }
      return s - 1;
    }), 1000);
    return () => clearInterval(t);
  }, [phase]);

  const startTest = () => {
    if (!selectedOp) return;
    setLoading(true);
    API.tests.create({
      oppositionId: selectedOp,
      numQuestions,
      mode,
      timeLimitMinutes: mode === 'EXAM_SIMULATION' ? Math.ceil(numQuestions * 1.5) : null,
    }).then(test => {
      const qs = (test.testQuestions || []).map(tq => ({
        testQuestionId: tq.id,
        questionId: tq.question?.id,
        text: tq.question?.questionText || '(Pregunta sin texto)',
        correct: tq.question?.correctAnswer,
        options: (() => {
          try {
            const raw = tq.question?.options;
            if (!raw) return [];
            const arr = JSON.parse(raw);
            return arr.map((o, i) => ({ k: String.fromCharCode(65 + i), t: o }));
          } catch { return []; }
        })(),
        explain: tq.question?.correctAnswer || '',
      }));
      setTestData(test);
      setQuestions(qs);
      setAnswers([]);
      setQIdx(0);
      setSelected(null);
      setRevealed(false);
      setPhase('running');
    }).catch(e => alert('Error creando test: ' + e.message))
      .finally(() => setLoading(false));
  };

  const pick = (optText) => {
    if (revealed) return;
    setSelected(optText);
    setTimeout(() => {
      const q = questions[qIdx];
      const isCorrect = optText === q.correct;
      setAnswers(prev => [...prev, { questionId: q.questionId, selectedOption: optText, isCorrect }]);
      setRevealed(true);
    }, 350);
  };

  const next = () => {
    if (qIdx < questions.length - 1) {
      setQIdx(qIdx + 1); setSelected(null); setRevealed(false);
    } else {
      handleSubmit();
    }
  };

  const handleSubmit = () => {
    if (!testData) return;
    API.tests.submit(testData.id, answers.map(a => ({
      questionId: a.questionId, selectedOption: a.selectedOption
    }))).then(r => { setResults(r); setPhase('results'); }).catch(console.error);
  };

  const q = questions[qIdx];
  const mm = String(Math.floor(time / 60)).padStart(2, '0');
  const ss = String(time % 60).padStart(2, '0');

  if (phase === 'setup') return (
    <div>
      <div className="section-header fadein-1">
        <div>
          <div className="section-sub">[ SIMULACRO · PRÁCTICA ]</div>
          <div className="section-title">Test <span className="acid">nuevo</span></div>
        </div>
      </div>

      <div className="card fadein-2">
        <div className="card-label">Oposición</div>
        <div className="field">
          <select value={selectedOp} onChange={e => setSelectedOp(e.target.value)}
            style={{ background: 'var(--surface-2)', border: '1px solid var(--border-2)', borderRadius: 'var(--r-md)', padding: '13px 16px', color: 'var(--ink)', fontSize: 14 }}>
            {oppositions.map(op => <option key={op.id} value={op.id}>{op.name}</option>)}
          </select>
        </div>
      </div>

      <div className="card fadein-2">
        <div className="card-label">Número de preguntas</div>
        <div className="number-input-wrap">
          <button className="num-btn" onClick={() => setNumQuestions(n => Math.max(5, n - 5))}>−</button>
          <input readOnly value={numQuestions} />
          <button className="num-btn" onClick={() => setNumQuestions(n => Math.min(100, n + 5))}>＋</button>
        </div>
      </div>

      <div className="card fadein-2">
        <div className="card-label">Modo</div>
        <div className="create-test-options">
          <div className={`mode-option${mode === 'PRACTICE' ? ' selected' : ''}`} onClick={() => setMode('PRACTICE')}>
            <span className="mode-option-icon">🎯</span>
            <div className="mode-option-info">
              <div className="mode-option-title">Práctica libre</div>
              <div className="mode-option-desc">Sin tiempo, con explicaciones</div>
            </div>
            <div className="mode-check"></div>
          </div>
          <div className={`mode-option${mode === 'EXAM_SIMULATION' ? ' selected' : ''}`} onClick={() => setMode('EXAM_SIMULATION')}>
            <span className="mode-option-icon">⏱️</span>
            <div className="mode-option-info">
              <div className="mode-option-title">Simulacro</div>
              <div className="mode-option-desc">Condiciones reales de examen</div>
            </div>
            <div className="mode-check"></div>
          </div>
        </div>
      </div>

      <div className="pad-h" style={{ marginTop: 8 }}>
        <button className="btn btn-primary btn-lg" onClick={startTest} disabled={loading || !selectedOp}>
          {loading ? 'Generando test…' : 'Comenzar test →'}
        </button>
      </div>
    </div>
  );

  if (phase === 'running' && q) return (
    <div>
      <div className="test-header">
        <div className="test-meta">
          <span className="test-counter">
            Pregunta <strong>{qIdx + 1}</strong>
            <span className="dim"> / {questions.length}</span>
          </span>
          {mode === 'EXAM_SIMULATION' && (
            <div className="timer">
              <div className="timer-dot"></div>
              {mm}:{ss}
            </div>
          )}
        </div>
        <div className="test-progress-bar">
          <div className="test-progress-fill" style={{ width: `${((qIdx + 1) / questions.length) * 100}%` }}></div>
        </div>
      </div>

      <div className="question-card fadein">
        <p className="question-text">{q.text}</p>
      </div>

      <div className="options-list fadein">
        {q.options.map((opt, i) => {
          let cls = 'option';
          if (revealed) {
            if (opt.t === q.correct) cls += ' correct';
            else if (opt.t === selected) cls += ' wrong';
          } else if (opt.t === selected) cls += ' selected';
          return (
            <div key={i} className={cls} onClick={() => pick(opt.t)}>
              <div className="option-key">{opt.k}</div>
              <div>{opt.t}</div>
            </div>
          );
        })}
      </div>

      {revealed && mode === 'PRACTICE' && q.explain && (
        <div className="explain-box fadein">
          <div className="explain-label">Explicación ›</div>
          <div>{q.explain}</div>
        </div>
      )}

      <div className="test-dots">
        {Array.from({ length: questions.length }).map((_, i) => (
          <span key={i} className={`test-dot${i === qIdx ? ' current' : i < qIdx ? ' done' : ''}`}></span>
        ))}
      </div>

      <div className="test-nav">
        <button className="btn btn-ghost" style={{ flex: 1 }}
          onClick={() => { if (qIdx > 0) { setQIdx(qIdx - 1); setSelected(null); setRevealed(false); } }}
          disabled={qIdx === 0}>‹ Anterior</button>
        <button className="btn btn-primary" style={{ flex: 1 }} onClick={next} disabled={!revealed}>
          {qIdx === questions.length - 1 ? 'Ver resultados →' : 'Siguiente →'}
        </button>
      </div>
    </div>
  );

  if (phase === 'results' && results) return (
    <div>
      <div className="section-header fadein-1">
        <div>
          <div className="section-sub">[ RESULTADO ]</div>
          <div className="section-title">Test <span className="acid">completado</span></div>
        </div>
      </div>
      <div className="stats-grid fadein-2">
        <div className="stats-metric">
          <div className="val" style={{ color: 'var(--acid)' }}>{Math.round(results.score || 0)}</div>
          <div className="lbl">Puntuación</div>
        </div>
        <div className="stats-metric">
          <div className="val" style={{ color: 'var(--green)' }}>{results.correctCount}</div>
          <div className="lbl">Correctas</div>
        </div>
        <div className="stats-metric">
          <div className="val" style={{ color: 'var(--magenta)' }}>{results.incorrectCount}</div>
          <div className="lbl">Incorrectas</div>
        </div>
        <div className="stats-metric">
          <div className="val" style={{ color: 'var(--ink-dim)' }}>{results.blankCount}</div>
          <div className="lbl">En blanco</div>
        </div>
      </div>
      <div className="pad-h" style={{ marginTop: 16 }}>
        <button className="btn btn-primary btn-lg" onClick={() => setPhase('setup')}>
          Nuevo test →
        </button>
      </div>
    </div>
  );

  return <div className="loading-wrap"><div className="spinner"></div></div>;
}

/* ==================== FLASHCARDS ==================== */
function FlashcardsView() {
  const [oppositions, setOppositions] = React.useState([]);
  const [selectedOp, setSelectedOp] = React.useState(null);
  const [blocks, setBlocks] = React.useState([]);
  const [selectedBlock, setSelectedBlock] = React.useState(null);
  const [topics, setTopics] = React.useState([]);
  const [selectedTopic, setSelectedTopic] = React.useState(null);
  const [cards, setCards] = React.useState([]);
  const [idx, setIdx] = React.useState(0);
  const [flipped, setFlipped] = React.useState(false);
  const [phase, setPhase] = React.useState('select'); // select | study
  const [loading, setLoading] = React.useState(false);

  React.useEffect(() => {
    API.oppositions.list().then(ops => {
      setOppositions(ops);
      if (ops.length > 0) setSelectedOp(ops[0].id);
    }).catch(console.error);
  }, []);

  React.useEffect(() => {
    if (!selectedOp) return;
    API.blocks.list(selectedOp).then(blks => {
      setBlocks(blks);
      setSelectedBlock(blks[0]?.id || null);
    }).catch(console.error);
  }, [selectedOp]);

  React.useEffect(() => {
    if (!selectedBlock) return;
    API.topics.list(selectedBlock).then(ts => {
      setTopics(ts);
      setSelectedTopic(ts[0]?.id || null);
    }).catch(console.error);
  }, [selectedBlock]);

  const startStudy = () => {
    if (!selectedTopic) return;
    setLoading(true);
    API.flashcards.list(selectedTopic)
      .then(fcs => {
        setCards(fcs);
        setIdx(0);
        setFlipped(false);
        setPhase('study');
      }).catch(e => alert('Error: ' + e.message))
      .finally(() => setLoading(false));
  };

  const swipe = (dir) => {
    setFlipped(false);
    setTimeout(() => {
      setIdx(i => (i + 1) % Math.max(1, cards.length));
    }, 200);
  };

  if (phase === 'select') return (
    <div>
      <div className="section-header fadein-1">
        <div>
          <div className="section-sub">[ REPASO ESPACIADO ]</div>
          <div className="section-title"><span className="magenta">flash</span><span style={{ color: 'var(--cyan)', fontStyle: 'italic' }}>cards</span></div>
        </div>
      </div>

      <div className="card fadein-2">
        <div className="card-label">Oposición</div>
        <div className="field">
          <select value={selectedOp || ''} onChange={e => setSelectedOp(e.target.value)}
            style={{ background: 'var(--surface-2)', border: '1px solid var(--border-2)', borderRadius: 'var(--r-md)', padding: '13px 16px', color: 'var(--ink)', fontSize: 14 }}>
            {oppositions.map(op => <option key={op.id} value={op.id}>{op.name}</option>)}
          </select>
        </div>
      </div>

      {blocks.length > 0 && (
        <div className="card fadein-2">
          <div className="card-label">Bloque</div>
          <div className="field">
            <select value={selectedBlock || ''} onChange={e => setSelectedBlock(e.target.value)}
              style={{ background: 'var(--surface-2)', border: '1px solid var(--border-2)', borderRadius: 'var(--r-md)', padding: '13px 16px', color: 'var(--ink)', fontSize: 14 }}>
              {blocks.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
            </select>
          </div>
        </div>
      )}

      {topics.length > 0 && (
        <div className="card fadein-2">
          <div className="card-label">Tema</div>
          <div className="field">
            <select value={selectedTopic || ''} onChange={e => setSelectedTopic(e.target.value)}
              style={{ background: 'var(--surface-2)', border: '1px solid var(--border-2)', borderRadius: 'var(--r-md)', padding: '13px 16px', color: 'var(--ink)', fontSize: 14 }}>
              {topics.map(t => <option key={t.id} value={t.id}>{t.title}</option>)}
            </select>
          </div>
        </div>
      )}

      <div className="pad-h" style={{ marginTop: 8 }}>
        <button className="btn btn-primary btn-lg" onClick={startStudy} disabled={loading || !selectedTopic}>
          {loading ? 'Cargando…' : 'Estudiar flashcards →'}
        </button>
      </div>
    </div>
  );

  if (cards.length === 0) return (
    <div>
      <div className="section-header">
        <button className="btn btn-ghost" onClick={() => setPhase('select')}>← Volver</button>
      </div>
      <div className="empty-state">
        <div className="empty-emoji">🃏</div>
        <div className="empty-title">Sin flashcards</div>
        <div className="empty-desc">Este tema no tiene flashcards. Genera algunas con IA desde la pestaña IA.</div>
        <button className="btn btn-ghost" onClick={() => setPhase('select')}>← Volver</button>
      </div>
    </div>
  );

  const card = cards[idx % cards.length];

  return (
    <div>
      <div className="section-header fadein-1" style={{ paddingBottom: 8 }}>
        <button className="btn btn-ghost" onClick={() => setPhase('select')}>← Volver</button>
        <span className="mono dim" style={{ fontSize: 12, letterSpacing: '0.1em' }}>{idx + 1} / {cards.length}</span>
      </div>

      <div className="flash-area fadein-2">
        <div className={`flash-card${flipped ? ' flipped' : ''}`} onClick={() => setFlipped(f => !f)}
          style={{ transformStyle: 'preserve-3d' }}>
          <div className="flash-face">
            <div className="flash-card-tag">PREGUNTA</div>
            <div className="flash-card-q">{card.question}</div>
            <div className="flash-hint">↻ TOCA PARA VOLTEAR</div>
          </div>
          <div className="flash-face back">
            <div className="flash-card-tag">RESPUESTA</div>
            <div className="flash-card-a">{card.answer}</div>
            <div className="flash-hint">← NO · SÍ →</div>
          </div>
        </div>

        <div className="flash-controls">
          <button className="flash-btn no" onClick={() => swipe('left')} title="No la sabía">✕</button>
          <button className="flash-btn flip" onClick={() => setFlipped(f => !f)} title="Voltear">↻</button>
          <button className="flash-btn yes" onClick={() => swipe('right')} title="La sabía">✓</button>
        </div>
      </div>
    </div>
  );
}

/* ==================== PLAN ==================== */
function PlanView() {
  const [oppositions, setOppositions] = React.useState([]);
  const [selectedOp, setSelectedOp] = React.useState(null);
  const [sessions, setSessions] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [generating, setGenerating] = React.useState(false);

  const today = new Date().toISOString().split('T')[0];
  const in4weeks = new Date(Date.now() + 28 * 86400000).toISOString().split('T')[0];

  React.useEffect(() => {
    API.oppositions.list().then(ops => {
      setOppositions(ops);
      if (ops.length > 0) {
        const op = ops[0];
        setSelectedOp(op);
        return API.planning.list(op.id, today, in4weeks);
      }
      return [];
    }).then(ss => setSessions(ss || []))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const generatePlan = () => {
    if (!selectedOp) return;
    setGenerating(true);
    API.planning.generate(selectedOp.id, {
      startDate: today,
      targetExamDate: selectedOp.targetExamDate || in4weeks,
      hoursPerWeek: selectedOp.hoursPerWeek || 10,
    }).then(ss => setSessions(ss || []))
      .catch(e => alert('Error: ' + e.message))
      .finally(() => setGenerating(false));
  };

  const updateStatus = (sessionId, status) => {
    API.planning.updateSession(sessionId, status)
      .then(updated => setSessions(ss => ss.map(s => s.id === updated.id ? updated : s)))
      .catch(console.error);
  };

  const dayNames = ['DOM','LUN','MAR','MIÉ','JUE','VIE','SÁB'];

  if (loading) return <div className="loading-wrap"><div className="spinner"></div><span className="loading-text">Cargando plan…</span></div>;

  return (
    <div>
      <div className="section-header fadein-1">
        <div>
          <div className="section-sub">[ PRÓXIMAS 4 SEMANAS ]</div>
          <div className="section-title">Plan <span className="acid">diario</span></div>
        </div>
        <button className="btn btn-primary" onClick={generatePlan} disabled={generating || !selectedOp}>
          {generating ? '…' : '⟳'}
        </button>
      </div>

      {oppositions.length > 1 && (
        <div className="pad-h" style={{ marginBottom: 12 }}>
          <div className="field">
            <label>Oposición</label>
            <select value={selectedOp?.id || ''}
              onChange={e => {
                const op = oppositions.find(o => String(o.id) === e.target.value);
                setSelectedOp(op);
                if (op) {
                  setLoading(true);
                  API.planning.list(op.id, today, in4weeks)
                    .then(ss => setSessions(ss || []))
                    .finally(() => setLoading(false));
                }
              }}
              style={{ background: 'var(--surface-2)', border: '1px solid var(--border-2)', borderRadius: 'var(--r-md)', padding: '10px 14px', color: 'var(--ink)', fontSize: 13 }}>
              {oppositions.map(op => <option key={op.id} value={op.id}>{op.name}</option>)}
            </select>
          </div>
        </div>
      )}

      {sessions.length === 0 ? (
        <div className="empty-state fadein">
          <div className="empty-emoji">📅</div>
          <div className="empty-title">Sin plan generado</div>
          <div className="empty-desc">Pulsa el botón ⟳ para que la IA genere tu plan de estudio personalizado.</div>
          <button className="btn btn-primary" onClick={generatePlan} disabled={generating}>
            {generating ? 'Generando…' : '⟳ Generar plan'}
          </button>
        </div>
      ) : (
        <div className="card fadein-2" style={{ padding: 0 }}>
          {sessions.map(s => {
            const d = new Date(s.date);
            return (
              <div key={s.id} className="session-item" onClick={() => {
                const next = s.status === 'PENDING' ? 'COMPLETED' : s.status === 'COMPLETED' ? 'SKIPPED' : 'PENDING';
                updateStatus(s.id, next);
              }}>
                <div className="session-date-box">
                  <span className="session-day-num">{d.getDate()}</span>
                  <span className="session-day-name">{dayNames[d.getDay()]}</span>
                </div>
                <div>
                  <div className="session-topic">{s.topicTitle || 'Sesión de estudio'}</div>
                  <div className="session-mins">{s.plannedMinutes} MIN</div>
                </div>
                <span className={`session-status-badge status-${s.status?.toLowerCase()}`}>
                  {s.status === 'COMPLETED' ? '✓ Hecha' : s.status === 'SKIPPED' ? '— Saltada' : 'Pendiente'}
                </span>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

/* ==================== IA ==================== */
function IAView() {
  const [prompt, setPrompt] = React.useState('');
  const [result, setResult] = React.useState('');
  const [running, setRunning] = React.useState(false);
  const [stats, setStats] = React.useState(null);

  React.useEffect(() => {
    API.stats.overview().then(setStats).catch(console.error);
  }, []);

  const suggestions = [
    'Resúmeme los artículos 21-24 de la Ley 39/2015',
    'Diferencia entre silencio positivo y negativo',
    'Esquema de las Cortes Generales',
    'Crea 10 flashcards sobre la Constitución Española',
    'Principales plazos en el procedimiento administrativo',
  ];

  const ask = () => {
    if (!prompt.trim()) return;
    setRunning(true);
    setResult('');
    API.ai.ask(prompt)
      .then(r => {
        const text = r.summary || r.content || JSON.stringify(r);
        // Simulate typewriter
        let i = 0;
        const interval = setInterval(() => {
          i += 6;
          setResult(text.slice(0, i));
          if (i >= text.length) { clearInterval(interval); setRunning(false); }
        }, 20);
      }).catch(e => {
        setResult('Error: ' + e.message);
        setRunning(false);
      });
  };

  return (
    <div>
      <div className="ai-header fadein-1">
        <div className="ai-pill"><div className="ai-pill-dot"></div>IA · ACTIVA</div>
        <div className="ai-title">tu <span className="violet">copiloto</span><br/>de oposición</div>
        <div className="ai-desc">Resúmenes, esquemas, flashcards y simulacros alimentados con tu temario.</div>
      </div>

      <div className="ai-prompt fadein-2">
        <span className="ai-prompt-icon">✺</span>
        <input
          value={prompt}
          onChange={e => setPrompt(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && ask()}
          placeholder="Pregunta algo sobre tu temario…"
        />
        <button className="btn btn-primary" onClick={ask} disabled={running || !prompt.trim()}>
          {running ? '…' : 'Generar →'}
        </button>
      </div>

      <div className="ai-chips fadein-2">
        {suggestions.map((s, i) => (
          <div key={i} className="ai-chip" onClick={() => setPrompt(s)}>{s}</div>
        ))}
      </div>

      {(result || running) && (
        <div className="ai-result fadein-2">
          {result}
          {running && <span className="ai-cursor"></span>}
        </div>
      )}

      {stats && (
        <div className="stats-grid fadein-3" style={{ marginTop: 20 }}>
          <div className="stats-metric">
            <div className="val" style={{ color: 'var(--violet)' }}>{stats.totalOppositions}</div>
            <div className="lbl">Oposiciones</div>
          </div>
          <div className="stats-metric">
            <div className="val" style={{ color: 'var(--magenta)' }}>{stats.totalTopics}</div>
            <div className="lbl">Temas totales</div>
          </div>
          <div className="stats-metric">
            <div className="val" style={{ color: 'var(--acid)' }}>{stats.totalTests}</div>
            <div className="lbl">Tests realizados</div>
          </div>
          <div className="stats-metric">
            <div className="val" style={{ color: 'var(--cyan)' }}>{Math.round(stats.averageScore || 0)}</div>
            <div className="lbl">Media tests</div>
          </div>
        </div>
      )}
    </div>
  );
}

// Expose to global scope
Object.assign(window, { OposicionesView, TestsView, FlashcardsView, PlanView, IAView });
