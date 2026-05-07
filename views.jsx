/* Oppositions, Tests, Flashcards, Plan, AI views */

function OppositionsView({ onNav }) {
  return (
    <div className="page">
      <div className="page-hero fadein-1">
        <div>
          <div className="hero-sub">[ 03 OPOSICIONES ACTIVAS · 151 TEMAS TOTALES ]</div>
          <h1 className="hero-title">Mis <span className="acid" style={{fontStyle: "italic"}}>oposiciones</span></h1>
        </div>
        <button className="btn btn-primary">＋ Nueva oposición</button>
      </div>

      <div className="dash-grid fadein-2">
        {MOCK_OPPOSITIONS.map(op => (
          <div key={op.id} className="span-4">
            <div className="card tilt" style={{minHeight: 280}}>
              <div className="row">
                <span className={"tag " + op.color}>{op.scope}</span>
                <span style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.14em"}}>EXAMEN · {op.targetExamDate}</span>
              </div>
              <h3 style={{fontFamily: "var(--font-display)", fontWeight: 400, fontSize: 28, lineHeight: 1.1, margin: "16px 0 8px", letterSpacing: "-0.01em"}}>{op.name}</h3>
              <div style={{color: "var(--ink-dim)", fontSize: 13}}>{op.blocks} bloques · {op.topics} temas · {op.hoursPerWeek}h/sem</div>
              <div style={{marginTop: 22}}>
                <div className="row" style={{justifyContent: "space-between", marginBottom: 6}}>
                  <span style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.16em"}}>PROGRESO</span>
                  <span style={{fontFamily: "var(--font-display)", fontSize: 22}}>{Math.round(op.progress * 100)}%</span>
                </div>
                <div style={{height: 6, background: "rgba(255,255,255,0.05)", borderRadius: 99, overflow: "hidden"}}>
                  <div style={{height: "100%", width: (op.progress * 100) + "%", background: op.color === "acid" ? "var(--grad-1)" : op.color === "magenta" ? "var(--grad-2)" : "var(--grad-3)", borderRadius: 99}}></div>
                </div>
              </div>
              <button className="btn btn-ghost" style={{marginTop: 18, width: "fit-content"}}>Abrir →</button>
            </div>
          </div>
        ))}
      </div>

      {/* Detail of first opposition */}
      <div style={{marginTop: 36}} className="fadein-3">
        <div className="row" style={{justifyContent: "space-between", marginBottom: 14}}>
          <h2 style={{fontFamily: "var(--font-display)", fontWeight: 400, fontSize: 36, margin: 0, letterSpacing: "-0.01em"}}>
            <span className="glitch" data-text="Auxiliar Administrativo">Auxiliar Administrativo</span> <span className="acid">/</span> Bloques y temas
          </h2>
          <div className="row" style={{gap: 6}}>
            <button className="btn btn-ghost">Filtrar</button>
            <button className="btn btn-ghost">Ordenar: prioridad</button>
          </div>
        </div>

        <div className="dash-grid">
          <div className="span-5">
            <div className="card">
              <div className="card-label">Bloques (haz click)</div>
              <div style={{display: "flex", flexDirection: "column", gap: 10}}>
                {MOCK_BLOCKS.map(b => {
                  const pct = Math.round(b.done / b.topicCount * 100);
                  return (
                    <div key={b.id} className="topic-row" style={{gridTemplateColumns: "1fr auto auto", gap: 16}}>
                      <div>
                        <div style={{fontWeight: 500}}>{b.name}</div>
                        <div style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.1em", marginTop: 2}}>
                          PESO {Math.round(b.weight * 100)}% · {b.done}/{b.topicCount} TEMAS
                        </div>
                      </div>
                      <div className="bar" style={{width: 80}}>
                        <div className="bar-fill" style={{width: pct + "%"}}></div>
                      </div>
                      <span style={{fontFamily: "var(--font-display)", fontSize: 20, color: pct > 60 ? "var(--acid)" : "var(--ink)"}}>{pct}%</span>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>

          <div className="span-7">
            <div className="card">
              <div className="card-label">Temas del bloque seleccionado</div>
              <div className="topic-list">
                {MOCK_TOPICS.map(t => (
                  <div key={t.id} className={"topic-row " + t.status.toLowerCase()}>
                    <div className="topic-num">{t.n}</div>
                    <div>
                      <div className="topic-title">{t.title}</div>
                      <div style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.1em", marginTop: 2}}>
                        {t.reviewCount} REPASOS · {t.lastReview}
                      </div>
                    </div>
                    <div className="topic-meta">
                      <span className={"tag " + (t.priority >= 4 ? "magenta" : "")}>P{t.priority}</span>
                      <span className={"tag " + (t.difficulty >= 4 ? "orange" : "cyan")}>D{t.difficulty}</span>
                      <span className={"tag " + (t.status === "MASTERED" ? "acid" : t.status === "REVIEWED" ? "cyan" : t.status === "STUDYING" ? "magenta" : "")}>
                        {t.status === "NOT_STARTED" ? "PEND." : t.status === "STUDYING" ? "ESTUD." : t.status === "REVIEWED" ? "REPAS." : "DOMIN."}
                      </span>
                    </div>
                    <div style={{fontFamily: "var(--font-mono)", color: "var(--ink-dim)"}}>›</div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

/* ---------- TESTS ---------- */

function TestsView() {
  const [qIdx, setQIdx] = React.useState(0);
  const [selected, setSelected] = React.useState(null);
  const [revealed, setRevealed] = React.useState(false);
  const [time, setTime] = React.useState(48 * 60);
  const [answers, setAnswers] = React.useState([]);

  React.useEffect(() => {
    const t = setInterval(() => setTime(s => Math.max(0, s - 1)), 1000);
    return () => clearInterval(t);
  }, []);

  const q = MOCK_QUESTIONS[qIdx];
  const total = MOCK_QUESTIONS.length;
  const mm = String(Math.floor(time / 60)).padStart(2, "0");
  const ss = String(time % 60).padStart(2, "0");

  const pick = (i) => {
    if (revealed) return;
    setSelected(i);
    setTimeout(() => {
      setRevealed(true);
      setAnswers(prev => [...prev, i === q.correct]);
    }, 350);
  };
  const next = () => {
    if (qIdx < total - 1) {
      setQIdx(qIdx + 1);
      setSelected(null);
      setRevealed(false);
    }
  };

  return (
    <div className="page">
      <div className="page-hero fadein-1">
        <div>
          <div className="hero-sub">[ MODO PRÁCTICA · 30 PREGUNTAS · 60 MIN ]</div>
          <h1 className="hero-title">Test <span className="acid" style={{fontStyle: "italic"}}>simulacro</span></h1>
        </div>
        <div className="row" style={{gap: 8}}>
          <button className="btn btn-ghost">Pausar</button>
          <button className="btn btn-ghost">Rendirse</button>
        </div>
      </div>

      <div className="test-stage fadein-2">
        <div className="test-bar">
          <div className="test-counter">Pregunta <strong>{String(qIdx + 1).padStart(2, "0")}</strong> <span style={{color: "var(--ink-dim)"}}>/ {String(total).padStart(2, "0")}</span></div>
          <div className="timer"><span className="pulse"></span> {mm}:{ss}</div>
        </div>

        <p className="q-text">{q.q}</p>

        <div className="q-options">
          {q.options.map((opt, i) => {
            let cls = "q-option";
            if (revealed) {
              if (i === q.correct) cls += " correct";
              else if (i === selected) cls += " wrong";
            } else if (i === selected) cls += " selected";
            return (
              <div key={i} className={cls} onClick={() => pick(i)}>
                <div className="q-option-key">{opt.k}</div>
                <div>{opt.t}</div>
              </div>
            );
          })}
        </div>

        {revealed && (
          <div style={{marginTop: 20, padding: 16, background: "rgba(0,229,255,0.06)", border: "1px solid rgba(0,229,255,0.2)", borderRadius: "var(--r-md)", fontSize: 14, position: "relative", zIndex: 2}}>
            <span style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--cyan)", letterSpacing: "0.18em"}}>EXPLICACIÓN ›</span>
            <div style={{marginTop: 6}}>{q.explain}</div>
          </div>
        )}

        <div className="test-progress">
          {Array.from({length: total}).map((_, i) => (
            <span key={i} className={i === qIdx ? "current" : i < qIdx ? "done" : ""}></span>
          ))}
        </div>

        <div className="test-controls">
          <button className="btn btn-ghost">‹ Anterior</button>
          <button className="btn btn-primary" onClick={next} disabled={!revealed}>
            {qIdx === total - 1 ? "Ver resultados →" : "Siguiente →"}
          </button>
        </div>
      </div>

      {/* Stats below */}
      <div className="dash-grid fadein-3" style={{marginTop: 24}}>
        <div className="span-4">
          <div className="card metric">
            <div className="card-label">Aciertos</div>
            <div className="metric-value" style={{color: "var(--acid)"}}>{answers.filter(Boolean).length}</div>
            <div className="metric-delta">+{answers.filter(Boolean).length} esta sesión</div>
          </div>
        </div>
        <div className="span-4">
          <div className="card metric">
            <div className="card-label">Fallos</div>
            <div className="metric-value" style={{color: "var(--orange)"}}>{answers.filter(a => a === false).length}</div>
            <div className="metric-delta down">se enviarán a repaso</div>
          </div>
        </div>
        <div className="span-4">
          <div className="card metric">
            <div className="card-label">Sin contestar</div>
            <div className="metric-value" style={{color: "var(--ink-dim)"}}>{total - answers.length}</div>
            <div className="metric-delta">de {total} totales</div>
          </div>
        </div>
      </div>
    </div>
  );
}

/* ---------- FLASHCARDS ---------- */

function FlashcardsView() {
  const [idx, setIdx] = React.useState(0);
  const [flipped, setFlipped] = React.useState(false);
  const [swipe, setSwipe] = React.useState(null);

  const card = MOCK_FLASHCARDS[idx % MOCK_FLASHCARDS.length];
  const card2 = MOCK_FLASHCARDS[(idx + 1) % MOCK_FLASHCARDS.length];
  const card3 = MOCK_FLASHCARDS[(idx + 2) % MOCK_FLASHCARDS.length];

  const swipeIt = (dir) => {
    setSwipe(dir);
    setTimeout(() => {
      setIdx(idx + 1);
      setFlipped(false);
      setSwipe(null);
    }, 450);
  };

  return (
    <div className="page">
      <div className="page-hero fadein-1">
        <div>
          <div className="hero-sub">[ MAZO · CONSTITUCIÓN ESPAÑOLA · 124 TARJETAS ]</div>
          <h1 className="hero-title"><span className="magenta">flash</span><i style={{color: "var(--cyan)"}}>cards</i></h1>
        </div>
        <div className="row" style={{gap: 6}}>
          <button className="btn btn-ghost">Cambiar mazo</button>
          <button className="btn btn-primary">＋ Generar con IA</button>
        </div>
      </div>

      <div className="dash-grid">
        <div className="span-8 fadein-2">
          <div className="card" style={{padding: 30}}>
            <div className="row" style={{justifyContent: "space-between"}}>
              <div className="card-label">Tarjeta {idx + 1} · {card.tag}</div>
              <span style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.14em"}}>SRS · INTERVALO 3D</span>
            </div>

            <div className="flashcard-stack">
              <div className="flash behind-2"
                   style={{background: "linear-gradient(135deg, #1a1a22, #131319)"}}>
                <div className="flash-face front">
                  <div className="flash-tag">{card3.tag}</div>
                  <div className="flash-q">{card3.q}</div>
                </div>
              </div>
              <div className="flash behind-1">
                <div className="flash-face front">
                  <div className="flash-tag">{card2.tag}</div>
                  <div className="flash-q">{card2.q}</div>
                </div>
              </div>
              <div className={"flash " + (flipped ? "flipped" : "") + (swipe === "left" ? " swipe-left" : swipe === "right" ? " swipe-right" : "")}
                   onClick={() => setFlipped(f => !f)}>
                <div className="flash-face front">
                  <div className="flash-tag">{card.tag}</div>
                  <div className="flash-q">{card.q}</div>
                  <div className="flash-foot">
                    <span>↻ TOCA PARA VOLTEAR</span>
                    <span>{idx + 1} / {MOCK_FLASHCARDS.length}</span>
                  </div>
                </div>
                <div className="flash-face back">
                  <div className="flash-tag">RESPUESTA</div>
                  <div className="flash-a">{card.a}</div>
                  <div className="flash-foot">
                    <span>← NO LA SABÍA · LA SABÍA →</span>
                    <span>{idx + 1} / {MOCK_FLASHCARDS.length}</span>
                  </div>
                </div>
              </div>
            </div>

            <div className="flash-controls">
              <button className="flash-btn no" onClick={() => swipeIt("left")} title="No la sabía">✕</button>
              <button className="flash-btn flip" onClick={() => setFlipped(f => !f)} title="Voltear">↻</button>
              <button className="flash-btn yes" onClick={() => swipeIt("right")} title="La sabía">✓</button>
            </div>
          </div>
        </div>

        <div className="span-4 fadein-3">
          <div className="card" style={{minHeight: "100%"}}>
            <div className="card-label">Sesión actual</div>
            <div style={{display: "flex", flexDirection: "column", gap: 18, marginTop: 12}}>
              <div>
                <div style={{fontFamily: "var(--font-display)", fontSize: 56, lineHeight: 1, color: "var(--acid)"}}>{idx}</div>
                <div style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.18em"}}>REVISADAS</div>
              </div>
              <div className="divider" style={{margin: 0}}></div>
              <div className="row" style={{justifyContent: "space-between"}}>
                <div>
                  <div style={{fontFamily: "var(--font-display)", fontSize: 32, color: "var(--cyan)"}}>87%</div>
                  <div style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.16em"}}>ACIERTO</div>
                </div>
                <div>
                  <div style={{fontFamily: "var(--font-display)", fontSize: 32, color: "var(--magenta)"}}>4d</div>
                  <div style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.16em"}}>SIG. REPASO</div>
                </div>
              </div>
              <div className="divider" style={{margin: 0}}></div>
              <div>
                <div style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.18em", marginBottom: 8}}>ESTADO DEL MAZO</div>
                <div className="row" style={{gap: 6, flexWrap: "wrap"}}>
                  <span className="tag acid">42 dominadas</span>
                  <span className="tag cyan">58 en repaso</span>
                  <span className="tag orange">24 difíciles</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

/* ---------- PLAN ---------- */

function PlanView() {
  return (
    <div className="page">
      <div className="page-hero fadein-1">
        <div>
          <div className="hero-sub">[ MAYO · 2026 · 22h/SEMANA ]</div>
          <h1 className="hero-title">Plan <span className="strike">caótico</span> <span className="acid" style={{fontStyle: "italic"}}>diario</span></h1>
        </div>
        <div className="row" style={{gap: 8}}>
          <button className="btn btn-ghost">‹ Abr</button>
          <button className="btn btn-ghost">Mayo 2026</button>
          <button className="btn btn-ghost">Jun ›</button>
          <button className="btn btn-primary">⟳ Replanificar IA</button>
        </div>
      </div>

      <div className="dash-grid">
        <div className="span-8 fadein-2">
          <div className="card">
            <div className="card-label">Calendario</div>
            <div className="calendar">
              {["L","M","X","J","V","S","D"].map(d => <div key={d} className="cal-head">{d}</div>)}
              {CALENDAR.map((d, i) => (
                <div key={i} className={"cal-day " + (d.muted ? "muted " : "") + (d.today ? "today " : "") + (d.bits ? "has " : "")}>
                  <div className="cal-num">{d.num}</div>
                  <div className="cal-bits">
                    {Array.from({length: d.bits || 0}).map((_, j) => (
                      <span key={j} className={"cal-bit " + (d.exam ? "exam" : "")}></span>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="span-4 fadein-3">
          <div className="card" style={{minHeight: "100%"}}>
            <div className="card-label">Hoy · sábado 11</div>
            <div style={{display: "flex", flexDirection: "column", gap: 10, marginTop: 12}}>
              {[
                { t: "09:30", title: "Procedimiento adm. II", min: 60, color: "acid" },
                { t: "11:00", title: "Test rápido · 20 preg.", min: 25, color: "cyan" },
                { t: "16:00", title: "Flashcards · CE Título I", min: 30, color: "magenta" },
                { t: "18:00", title: "Repaso espaciado", min: 45, color: "orange" },
              ].map((s, i) => (
                <div key={i} className="topic-row" style={{gridTemplateColumns: "auto 1fr auto", padding: "12px 14px"}}>
                  <span style={{fontFamily: "var(--font-mono)", fontSize: 12, color: "var(--ink-dim)", letterSpacing: "0.1em"}}>{s.t}</span>
                  <div>
                    <div style={{fontWeight: 500, fontSize: 14}}>{s.title}</div>
                    <div style={{fontFamily: "var(--font-mono)", fontSize: 10, color: "var(--ink-dim)", letterSpacing: "0.1em", marginTop: 2}}>{s.min} MIN</div>
                  </div>
                  <span className={"tag " + s.color}>●</span>
                </div>
              ))}
            </div>
            <div className="divider"></div>
            <div className="row" style={{justifyContent: "space-between"}}>
              <span style={{fontFamily: "var(--font-mono)", fontSize: 11, color: "var(--ink-dim)", letterSpacing: "0.14em"}}>TOTAL HOY</span>
              <span style={{fontFamily: "var(--font-display)", fontSize: 28}}>2h 40m</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

/* ---------- AI ---------- */

const AI_RESULT = `RESUMEN · LEY 39/2015, ARTÍCULOS 21-24

▸ Plazo máximo de resolución: cuando la norma reguladora del procedimiento
  no fije plazo, el plazo máximo será de TRES MESES.
▸ Cómputo: desde la fecha de presentación de la solicitud (a instancia
  de parte) o desde el acuerdo de iniciación (de oficio).
▸ Silencio administrativo: vencido el plazo sin resolución expresa, se
  entenderá ESTIMADA la solicitud, salvo excepciones legales.
▸ Suspensión: el plazo puede suspenderse por requerimientos de subsanación,
  informes preceptivos o pruebas técnicas (art. 22).

CONCEPTOS CLAVE → silencio positivo · silencio negativo · resolución expresa.`;

function AIView() {
  const [typed, setTyped] = React.useState("");
  const [running, setRunning] = React.useState(true);
  const [prompt, setPrompt] = React.useState("");

  React.useEffect(() => {
    if (!running) return;
    let i = 0;
    const t = setInterval(() => {
      i += 4;
      setTyped(AI_RESULT.slice(0, i));
      if (i >= AI_RESULT.length) {
        clearInterval(t);
        setRunning(false);
      }
    }, 18);
    return () => clearInterval(t);
  }, [running]);

  return (
    <div className="page">
      <div className="page-hero fadein-1">
        <div>
          <div className="hero-sub">[ ASISTENTE · GPT-4o-MINI · CONTEXTO 4096 TOKENS ]</div>
          <h1 className="hero-title">tu <span style={{color: "var(--violet)", fontStyle: "italic"}}>copiloto</span> de oposición</h1>
        </div>
      </div>

      <div className="ai-shell fadein-2">
        <div className="ai-pill"><span className="dot"></span> IA · ACTIVA</div>
        <h2 style={{fontFamily: "var(--font-display)", fontWeight: 400, fontSize: 38, lineHeight: 1.1, margin: "14px 0 4px", letterSpacing: "-0.01em"}}>
          ¿Qué quieres que prepare hoy?
        </h2>
        <p style={{color: "var(--ink-dim)", margin: 0}}>
          Resúmenes, esquemas, flashcards, simulacros tipo test… alimentado con tus PDFs y los temarios oficiales.
        </p>

        <div className="ai-prompt">
          <span style={{color: "var(--violet)", fontSize: 18}}>✺</span>
          <input
            value={prompt}
            onChange={e => setPrompt(e.target.value)}
            placeholder="Resúmeme los artículos 21 al 24 de la Ley 39/2015 con ejemplos…"
          />
          <button className="btn btn-primary" onClick={() => { setTyped(""); setRunning(true); }}>Generar →</button>
        </div>

        <div className="ai-suggestions">
          {["Generar 20 flashcards · Tema 04", "Resumir PDF cargado", "Crear simulacro de 50 preg.", "Esquema visual · Cortes Generales", "Examen tipo AGE 2024"].map((s, i) => (
            <div key={i} className="ai-chip" onClick={() => setPrompt(s)}>{s}</div>
          ))}
        </div>

        <div className="ai-result">
          {typed}
          {running && <span className="cursor"></span>}
        </div>
      </div>

      <div className="dash-grid fadein-3" style={{marginTop: 22}}>
        <div className="span-4">
          <div className="card metric">
            <div className="card-label">Documentos</div>
            <div className="metric-value">14</div>
            <div className="metric-delta">3 procesados esta semana</div>
          </div>
        </div>
        <div className="span-4">
          <div className="card metric">
            <div className="card-label">Resúmenes IA</div>
            <div className="metric-value" style={{color: "var(--violet)"}}>56</div>
            <div className="metric-delta">+12 últimos 7d</div>
          </div>
        </div>
        <div className="span-4">
          <div className="card metric">
            <div className="card-label">Flashcards generadas</div>
            <div className="metric-value" style={{color: "var(--magenta)"}}>318</div>
            <div className="metric-delta">tasa de acierto 84%</div>
          </div>
        </div>
      </div>
    </div>
  );
}

Object.assign(window, { OppositionsView, TestsView, FlashcardsView, PlanView, AIView });
