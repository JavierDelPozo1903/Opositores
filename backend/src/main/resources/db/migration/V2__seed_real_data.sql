-- =============================================================
-- opositOS — Datos reales de oposiciones españolas
-- Fuente: BOE y convocatorias oficiales
-- =============================================================

-- Usuario demo para explorar la app
-- Contraseña: Demo1234!  (BCrypt del texto anterior)
INSERT INTO users (email, password_hash, name, subscription_plan, subscription_status)
VALUES ('demo@opositOS.es',
        '$2a$12$LQv3c1yqBWVHxkd0LQ1tIuqKpvzGKQXBL3k1K1DGZ.xH6VQLqLFIq',
        'Usuario Demo', 'PRO', 'ACTIVE');

SET @demo_user = LAST_INSERT_ID();

-- =============================================================
-- OPOSICIÓN 1: Auxiliar Administrativo del Estado (C2 · AGE)
-- =============================================================
INSERT INTO oppositions (user_id, name, scope, target_exam_date, hours_per_week)
VALUES (@demo_user, 'Auxiliar Administrativo del Estado', 'AGE', '2026-06-15', 20);
SET @op1 = LAST_INSERT_ID();

-- Bloque I
INSERT INTO blocks (opposition_id, name, weight) VALUES (@op1, 'Bloque I – Organización Política del Estado', 0.30);
SET @b1 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b1, 'Tema 1',  'La Constitución Española de 1978. Estructura y contenido. Principios generales. Reforma constitucional.', 3, 5, 'NOT_STARTED'),
(@b1, 'Tema 2',  'Derechos y deberes fundamentales de los españoles. Garantías y suspensión de derechos.', 3, 5, 'NOT_STARTED'),
(@b1, 'Tema 3',  'La Corona. Funciones constitucionales del Rey. La sucesión a la Corona.', 2, 4, 'NOT_STARTED'),
(@b1, 'Tema 4',  'Las Cortes Generales. El Congreso de los Diputados y el Senado. El Defensor del Pueblo.', 3, 4, 'NOT_STARTED'),
(@b1, 'Tema 5',  'El Gobierno y la Administración del Estado. Relaciones entre el Gobierno y las Cortes Generales.', 3, 5, 'NOT_STARTED'),
(@b1, 'Tema 6',  'El Poder Judicial. El Consejo General del Poder Judicial. El Tribunal Constitucional.', 3, 4, 'NOT_STARTED'),
(@b1, 'Tema 7',  'La organización territorial del Estado. Las Comunidades Autónomas. Los Estatutos de Autonomía.', 3, 4, 'NOT_STARTED'),
(@b1, 'Tema 8',  'La Administración Local. El municipio. La provincia. La isla. Régimen de concejo abierto.', 2, 3, 'NOT_STARTED');

-- Bloque II
INSERT INTO blocks (opposition_id, name, weight) VALUES (@op1, 'Bloque II – Administración General del Estado', 0.30);
SET @b2 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b2, 'Tema 9',  'La Administración General del Estado. Organización central, periférica y exterior.', 3, 5, 'NOT_STARTED'),
(@b2, 'Tema 10', 'Los órganos superiores y directivos de la AGE. El Consejo de Estado.', 3, 4, 'NOT_STARTED'),
(@b2, 'Tema 11', 'Los funcionarios públicos. Clases y situaciones administrativas. El personal laboral.', 4, 5, 'NOT_STARTED'),
(@b2, 'Tema 12', 'La selección del personal al servicio de la AGE. Los sistemas de provisión de puestos.', 3, 5, 'NOT_STARTED'),
(@b2, 'Tema 13', 'Los derechos y deberes de los empleados públicos. El régimen disciplinario.', 3, 4, 'NOT_STARTED'),
(@b2, 'Tema 14', 'La jornada de trabajo, permisos y licencias de los funcionarios. Las vacaciones.', 2, 4, 'NOT_STARTED'),
(@b2, 'Tema 15', 'La Seguridad Social de los funcionarios públicos. MUFACE. Clases pasivas.', 3, 3, 'NOT_STARTED');

-- Bloque III
INSERT INTO blocks (opposition_id, name, weight) VALUES (@op1, 'Bloque III – Actividad Administrativa', 0.25);
SET @b3 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b3, 'Tema 16', 'El acto administrativo. Concepto, clases y elementos. Eficacia y validez.', 4, 5, 'NOT_STARTED'),
(@b3, 'Tema 17', 'El procedimiento administrativo. Principios generales. Fases del procedimiento.', 4, 5, 'NOT_STARTED'),
(@b3, 'Tema 18', 'Los recursos administrativos. El recurso de alzada. El recurso potestativo de reposición. El recurso extraordinario de revisión.', 4, 5, 'NOT_STARTED'),
(@b3, 'Tema 19', 'La responsabilidad patrimonial de la Administración. Concepto y presupuestos. El procedimiento de responsabilidad.', 3, 4, 'NOT_STARTED'),
(@b3, 'Tema 20', 'Los contratos del sector público. Tipos de contratos. El contrato de obras y el contrato de servicios.', 4, 3, 'NOT_STARTED'),
(@b3, 'Tema 21', 'La Transparencia y el acceso a la información pública. La Ley 19/2013.', 3, 4, 'NOT_STARTED'),
(@b3, 'Tema 22', 'Protección de datos de carácter personal. El Reglamento General de Protección de Datos (RGPD).', 3, 5, 'NOT_STARTED');

-- Bloque IV
INSERT INTO blocks (opposition_id, name, weight) VALUES (@op1, 'Bloque IV – Ofimática e Informática', 0.15);
SET @b4 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b4, 'Tema 23', 'Sistemas operativos. Windows 10/11. El explorador de archivos. Gestión de usuarios.', 2, 4, 'NOT_STARTED'),
(@b4, 'Tema 24', 'Tratamiento de textos. Microsoft Word. Edición, formato, tablas e impresión.', 2, 5, 'NOT_STARTED'),
(@b4, 'Tema 25', 'Hoja de cálculo. Microsoft Excel. Fórmulas, funciones, gráficos y filtros.', 3, 5, 'NOT_STARTED'),
(@b4, 'Tema 26', 'Bases de datos. Microsoft Access. Concepto y utilización básica.', 3, 3, 'NOT_STARTED'),
(@b4, 'Tema 27', 'Internet, intranet y correo electrónico. Seguridad informática. Protección frente a malware.', 2, 4, 'NOT_STARTED'),
(@b4, 'Tema 28', 'La Administración electrónica. La sede electrónica. El certificado digital y el DNIe.', 3, 5, 'NOT_STARTED');

-- =============================================================
-- OPOSICIÓN 2: Agente de Clasificación y Reparto — Correos (AGE)
-- =============================================================
INSERT INTO oppositions (user_id, name, scope, target_exam_date, hours_per_week)
VALUES (@demo_user, 'Agente de Clasificación y Reparto – Correos', 'AGE', '2026-04-20', 15);
SET @op2 = LAST_INSERT_ID();

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op2, 'Bloque I – El Grupo Correos', 0.25);
SET @b5 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b5, 'Tema 1', 'El Grupo Correos. Historia y evolución. Estructura organizativa y sociedades del Grupo.', 2, 5, 'NOT_STARTED'),
(@b5, 'Tema 2', 'La Sociedad Estatal Correos y Telégrafos, S.A. Objeto social y funciones. Órganos de gobierno.', 2, 5, 'NOT_STARTED'),
(@b5, 'Tema 3', 'El servicio postal universal. Concepto, características y prestación. El operador designado.', 3, 5, 'NOT_STARTED'),
(@b5, 'Tema 4', 'Los servicios de Correos. Carta certificada, burofax, telegrama. Servicios de paquetería.', 2, 4, 'NOT_STARTED'),
(@b5, 'Tema 5', 'Correos Express. Servicios financieros: giro postal, giro telegráfico y giro internacional.', 2, 4, 'NOT_STARTED'),
(@b5, 'Tema 6', 'Los servicios digitales de Correos. Correos Online. La notificación electrónica.', 2, 3, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op2, 'Bloque II – Normativa Postal', 0.30);
SET @b6 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b6, 'Tema 7',  'La Ley 43/2010 del servicio postal universal. Objeto, ámbito y definiciones.', 3, 5, 'NOT_STARTED'),
(@b6, 'Tema 8',  'Los envíos postales. Clasificación: cartas, tarjetas postales, cecogramas y paquetes.', 2, 5, 'NOT_STARTED'),
(@b6, 'Tema 9',  'Franqueo y tarifas postales. El sello de correos. La etiqueta de franqueo.', 2, 4, 'NOT_STARTED'),
(@b6, 'Tema 10', 'Derechos de los usuarios del servicio postal. Reclamaciones y devoluciones.', 2, 4, 'NOT_STARTED'),
(@b6, 'Tema 11', 'La Unión Postal Universal (UPU). La Unión Postal de las Américas, España y Portugal (UPAEP).', 2, 3, 'NOT_STARTED'),
(@b6, 'Tema 12', 'Envíos internacionales. Normativa aplicable. Aduanas y derechos arancelarios.', 3, 3, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op2, 'Bloque III – Geografía Postal', 0.25);
SET @b7 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b7, 'Tema 13', 'Organización territorial de España. Las Comunidades Autónomas y sus capitales.', 2, 5, 'NOT_STARTED'),
(@b7, 'Tema 14', 'Las provincias españolas. Capitales de provincia. Ciudades Autónomas de Ceuta y Melilla.', 2, 5, 'NOT_STARTED'),
(@b7, 'Tema 15', 'El código postal en España. Estructura y significado. Codificación por provincias.', 1, 5, 'NOT_STARTED'),
(@b7, 'Tema 16', 'Geografía urbana. Principales ciudades y áreas metropolitanas de España.', 2, 4, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op2, 'Bloque IV – Prevención de Riesgos Laborales y Calidad', 0.20);
SET @b8 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b8, 'Tema 17', 'La Ley de Prevención de Riesgos Laborales. Principios generales y obligaciones del empresario.', 3, 4, 'NOT_STARTED'),
(@b8, 'Tema 18', 'Riesgos laborales en el reparto postal. Ergonomía, seguridad vial y riesgos físicos.', 2, 5, 'NOT_STARTED'),
(@b8, 'Tema 19', 'La calidad en el servicio postal. Indicadores y estándares de calidad de Correos.', 2, 3, 'NOT_STARTED'),
(@b8, 'Tema 20', 'Medioambiente y sostenibilidad en Correos. El Plan de Sostenibilidad del Grupo Correos.', 2, 3, 'NOT_STARTED');

-- =============================================================
-- OPOSICIÓN 3: Policía Nacional – Escala Básica
-- =============================================================
INSERT INTO oppositions (user_id, name, scope, target_exam_date, hours_per_week)
VALUES (@demo_user, 'Policía Nacional – Escala Básica', 'POLICIA', '2026-09-01', 25);
SET @op3 = LAST_INSERT_ID();

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op3, 'Bloque I – Ciencias Jurídicas', 0.40);
SET @b9 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b9, 'Tema 1',  'La Constitución Española de 1978. Estructura. Derechos fundamentales y libertades públicas.', 3, 5, 'NOT_STARTED'),
(@b9, 'Tema 2',  'Las Fuerzas y Cuerpos de Seguridad. La Ley Orgánica 2/1986. El Cuerpo Nacional de Policía.', 3, 5, 'NOT_STARTED'),
(@b9, 'Tema 3',  'El Código Penal. Delitos contra las personas, el patrimonio y el orden público.', 4, 5, 'NOT_STARTED'),
(@b9, 'Tema 4',  'La Ley de Enjuiciamiento Criminal. La detención. Los derechos del detenido.', 4, 5, 'NOT_STARTED'),
(@b9, 'Tema 5',  'La Ley Orgánica 1/2004 de Medidas de Protección Integral contra la Violencia de Género.', 3, 5, 'NOT_STARTED'),
(@b9, 'Tema 6',  'Legislación de extranjería. La Ley Orgánica 4/2000. Tipos de visado y residencia.', 4, 4, 'NOT_STARTED'),
(@b9, 'Tema 7',  'La identificación de personas. El Documento Nacional de Identidad. El pasaporte.', 2, 4, 'NOT_STARTED'),
(@b9, 'Tema 8',  'Delitos informáticos y ciberdelincuencia. Marco normativo y tipos penales.', 4, 4, 'NOT_STARTED'),
(@b9, 'Tema 9',  'El tráfico de drogas. La Ley Orgánica 1/1992 sobre Protección de la Seguridad Ciudadana.', 3, 4, 'NOT_STARTED'),
(@b9, 'Tema 10', 'El terrorismo. Marco normativo. Delitos de terrorismo en el Código Penal.', 4, 4, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op3, 'Bloque II – Ciencias Sociales', 0.25);
SET @b10 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b10, 'Tema 11', 'Geografía de España. Relieve, clima e hidrografía. Organización territorial.', 2, 4, 'NOT_STARTED'),
(@b10, 'Tema 12', 'Historia contemporánea de España. La Transición. La España democrática.', 3, 4, 'NOT_STARTED'),
(@b10, 'Tema 13', 'La Unión Europea. Historia, instituciones y funcionamiento. El espacio Schengen.', 3, 4, 'NOT_STARTED'),
(@b10, 'Tema 14', 'Los Derechos Humanos. La Declaración Universal. El Convenio Europeo de Derechos Humanos.', 3, 4, 'NOT_STARTED'),
(@b10, 'Tema 15', 'La sociedad española actual. Demografía, inmigración e integración social.', 2, 3, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op3, 'Bloque III – Materias Técnico-Científicas', 0.20);
SET @b11 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b11, 'Tema 16', 'Informática básica. Sistemas operativos. Redes e Internet. Ciberseguridad.', 3, 4, 'NOT_STARTED'),
(@b11, 'Tema 17', 'Primeros auxilios. RCP básica. Actuación ante accidentes de tráfico y heridas.', 3, 5, 'NOT_STARTED'),
(@b11, 'Tema 18', 'Seguridad vial. El Reglamento General de Circulación. Señales de tráfico.', 3, 5, 'NOT_STARTED'),
(@b11, 'Tema 19', 'Accidentes nucleares, radiológicos, biológicos y químicos (NRBQ). Actuación policial.', 4, 3, 'NOT_STARTED'),
(@b11, 'Tema 20', 'Topografía básica. Lectura de mapas y planos. Sistema GPS y cartografía digital.', 3, 3, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op3, 'Bloque IV – Inglés', 0.15);
SET @b12 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b12, 'Tema 21', 'Gramática inglesa. Tiempos verbales, auxiliares modales y estructura de la oración.', 3, 4, 'NOT_STARTED'),
(@b12, 'Tema 22', 'Vocabulario policial en inglés. Términos jurídicos y de seguridad pública.', 3, 4, 'NOT_STARTED'),
(@b12, 'Tema 23', 'Comprensión lectora en inglés. Textos jurídicos y periodísticos.', 3, 4, 'NOT_STARTED');

-- =============================================================
-- OPOSICIÓN 4: Técnico de Hacienda (A2 · AGE)
-- =============================================================
INSERT INTO oppositions (user_id, name, scope, target_exam_date, hours_per_week)
VALUES (@demo_user, 'Técnico de Hacienda – Agencia Tributaria', 'AGE', '2026-11-10', 30);
SET @op4 = LAST_INSERT_ID();

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op4, 'Bloque I – Derecho Constitucional y Administrativo', 0.20);
SET @b13 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b13, 'Tema 1',  'La Constitución Española. El Estado social y democrático de derecho. La distribución de competencias.', 3, 5, 'NOT_STARTED'),
(@b13, 'Tema 2',  'La Unión Europea. El Derecho comunitario originario y derivado. Las Directivas en materia fiscal.', 4, 5, 'NOT_STARTED'),
(@b13, 'Tema 3',  'El acto administrativo tributario. La liquidación. Las autoliquidaciones. La declaración tributaria.', 4, 5, 'NOT_STARTED'),
(@b13, 'Tema 4',  'Los procedimientos tributarios. Gestión, inspección y recaudación. El procedimiento sancionador.', 5, 5, 'NOT_STARTED'),
(@b13, 'Tema 5',  'Los recursos en vía administrativa tributaria. El recurso de reposición. Las reclamaciones económico-administrativas.', 4, 5, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op4, 'Bloque II – Derecho Financiero y Tributario', 0.35);
SET @b14 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b14, 'Tema 6',  'La Ley General Tributaria. Principios del ordenamiento tributario. Los tributos: concepto y clases.', 4, 5, 'NOT_STARTED'),
(@b14, 'Tema 7',  'El hecho imponible. El devengo. La base imponible y la base liquidable.', 4, 5, 'NOT_STARTED'),
(@b14, 'Tema 8',  'Los sujetos de la obligación tributaria. El contribuyente. El sustituto. Los responsables.', 4, 5, 'NOT_STARTED'),
(@b14, 'Tema 9',  'El Impuesto sobre la Renta de las Personas Físicas. Hecho imponible y rentas exentas.', 5, 5, 'NOT_STARTED'),
(@b14, 'Tema 10', 'El IRPF. Rendimientos del trabajo. Rendimientos del capital inmobiliario y mobiliario.', 5, 5, 'NOT_STARTED'),
(@b14, 'Tema 11', 'El IRPF. Actividades económicas. Las ganancias y pérdidas patrimoniales. La base imponible del ahorro.', 5, 5, 'NOT_STARTED'),
(@b14, 'Tema 12', 'El Impuesto sobre Sociedades. Concepto, naturaleza y ámbito de aplicación.', 5, 5, 'NOT_STARTED'),
(@b14, 'Tema 13', 'El Impuesto sobre el Valor Añadido. Hecho imponible. Sujeto pasivo. Base imponible y tipos.', 5, 5, 'NOT_STARTED'),
(@b14, 'Tema 14', 'El IVA. El régimen general de deducciones. La prorrata. Las operaciones con el exterior.', 5, 5, 'NOT_STARTED'),
(@b14, 'Tema 15', 'Los impuestos especiales. Impuesto sobre hidrocarburos, alcohol y tabaco.', 4, 4, 'NOT_STARTED'),
(@b14, 'Tema 16', 'El Impuesto sobre Transmisiones Patrimoniales y Actos Jurídicos Documentados.', 4, 4, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op4, 'Bloque III – Contabilidad y Gestión', 0.25);
SET @b15 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b15, 'Tema 17', 'El Plan General de Contabilidad. Principios contables. El marco conceptual.', 4, 5, 'NOT_STARTED'),
(@b15, 'Tema 18', 'El inmovilizado material e intangible. Amortización. El inmovilizado financiero.', 4, 4, 'NOT_STARTED'),
(@b15, 'Tema 19', 'Las existencias. Los créditos comerciales. La tesorería. Los débitos y partidas a pagar.', 4, 4, 'NOT_STARTED'),
(@b15, 'Tema 20', 'Las cuentas anuales. El balance de situación. La cuenta de pérdidas y ganancias. El estado de flujos de efectivo.', 5, 5, 'NOT_STARTED'),
(@b15, 'Tema 21', 'La contabilidad de costes. Sistemas de costes. El punto de equilibrio.', 4, 3, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op4, 'Bloque IV – Informática y Estadística', 0.20);
SET @b16 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b16, 'Tema 22', 'Bases de datos relacionales. SQL. El modelo entidad-relación.', 4, 5, 'NOT_STARTED'),
(@b16, 'Tema 23', 'Hoja de cálculo avanzada. Tablas dinámicas. Funciones financieras y estadísticas.', 3, 5, 'NOT_STARTED'),
(@b16, 'Tema 24', 'Estadística descriptiva. Medidas de centralización y dispersión. Representaciones gráficas.', 3, 4, 'NOT_STARTED'),
(@b16, 'Tema 25', 'Probabilidad. Distribuciones de probabilidad. La distribución normal. Inferencia estadística básica.', 4, 4, 'NOT_STARTED');

-- =============================================================
-- OPOSICIÓN 5: Maestro/a de Educación Primaria (EDUCACION · CCAA)
-- =============================================================
INSERT INTO oppositions (user_id, name, scope, target_exam_date, hours_per_week)
VALUES (@demo_user, 'Maestro/a Educación Primaria – Cuerpo de Maestros', 'EDUCACION', '2026-05-20', 20);
SET @op5 = LAST_INSERT_ID();

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op5, 'Bloque I – Ciencias de la Educación', 0.30);
SET @b17 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b17, 'Tema 1',  'El sistema educativo español. La LOMLOE. Principios, fines y estructura del sistema educativo.', 3, 5, 'NOT_STARTED'),
(@b17, 'Tema 2',  'La Educación Primaria. Objetivos, etapas, áreas y principios pedagógicos.', 3, 5, 'NOT_STARTED'),
(@b17, 'Tema 3',  'El currículo de Educación Primaria. Elementos curriculares: competencias, saberes y criterios de evaluación.', 4, 5, 'NOT_STARTED'),
(@b17, 'Tema 4',  'Teorías del aprendizaje. Conductismo, cognitivismo y constructivismo. Aprendizaje significativo.', 4, 5, 'NOT_STARTED'),
(@b17, 'Tema 5',  'La atención a la diversidad. La educación inclusiva. El alumnado con necesidades específicas de apoyo educativo (NEAE).', 4, 5, 'NOT_STARTED'),
(@b17, 'Tema 6',  'La programación didáctica. Elementos. Unidades didácticas y situaciones de aprendizaje.', 3, 5, 'NOT_STARTED'),
(@b17, 'Tema 7',  'La evaluación educativa. Tipos, instrumentos y criterios. La evaluación competencial.', 3, 5, 'NOT_STARTED'),
(@b17, 'Tema 8',  'La tutoría y la orientación educativa. El Plan de Acción Tutorial. Las familias y la escuela.', 3, 4, 'NOT_STARTED'),
(@b17, 'Tema 9',  'Las TIC en educación. La competencia digital docente. Recursos educativos digitales.', 3, 4, 'NOT_STARTED'),
(@b17, 'Tema 10', 'La convivencia escolar. El Reglamento de Régimen Interior. La mediación y la resolución de conflictos.', 3, 4, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op5, 'Bloque II – Didácticas Específicas', 0.40);
SET @b18 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b18, 'Tema 11', 'Didáctica de la Lengua Castellana. El enfoque comunicativo. Las habilidades lingüísticas.', 3, 5, 'NOT_STARTED'),
(@b18, 'Tema 12', 'La lectura y la escritura en Primaria. Métodos de lectoescritura. El Plan Lector de centro.', 3, 5, 'NOT_STARTED'),
(@b18, 'Tema 13', 'Didáctica de las Matemáticas. El pensamiento matemático en Primaria. La resolución de problemas.', 4, 5, 'NOT_STARTED'),
(@b18, 'Tema 14', 'Didáctica de las Ciencias Naturales y Sociales. El Conocimiento del Medio. El método científico escolar.', 3, 4, 'NOT_STARTED'),
(@b18, 'Tema 15', 'Didáctica de la Educación Física. El cuerpo y el movimiento. La expresión corporal.', 3, 4, 'NOT_STARTED'),
(@b18, 'Tema 16', 'La Educación Artística en Primaria. La educación plástica y visual. La educación musical.', 3, 3, 'NOT_STARTED'),
(@b18, 'Tema 17', 'Didáctica de la Lengua Extranjera. El enfoque AICLE. Los programas bilingües.', 3, 4, 'NOT_STARTED');

INSERT INTO blocks (opposition_id, name, weight) VALUES (@op5, 'Bloque III – Legislación Educativa', 0.30);
SET @b19 = LAST_INSERT_ID();
INSERT INTO topics (block_id, official_number, title, difficulty, priority, status) VALUES
(@b19, 'Tema 18', 'La función docente. Derechos y deberes del profesorado. La carrera docente.', 3, 5, 'NOT_STARTED'),
(@b19, 'Tema 19', 'La organización y funcionamiento del centro educativo. El Proyecto Educativo de Centro.', 3, 4, 'NOT_STARTED'),
(@b19, 'Tema 20', 'Los órganos de gobierno y de coordinación docente. El Consejo Escolar. El Claustro.', 2, 4, 'NOT_STARTED'),
(@b19, 'Tema 21', 'La protección de menores. Obligaciones del docente ante situaciones de riesgo o abuso.', 3, 5, 'NOT_STARTED'),
(@b19, 'Tema 22', 'Igualdad de género en educación. La coeducación. Planes de igualdad en los centros.', 3, 4, 'NOT_STARTED');
