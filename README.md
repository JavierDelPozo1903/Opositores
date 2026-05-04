# Opositores — Plataforma de preparación de oposiciones

Aplicación full-stack compuesta por un **backend Spring Boot** y una **app Android nativa (Java)** para ayudar a opositores en España a gestionar su estudio.

---

## Estructura del proyecto

```
Opositores/
├── backend/          # Spring Boot 3.x + PostgreSQL + JWT
└── android/          # App Android nativa Java (MVVM + Retrofit + Room)
```

---

## Backend

### Requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 14+ (base de datos `opositores_db`)

### Configuración

Crea la base de datos:
```sql
CREATE DATABASE opositores_db;
```

Variables de entorno (o ajusta `application.yml`):
```bash
export DB_USER=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=tu-clave-secreta-de-al-menos-32-caracteres
export AI_API_KEY=tu-clave-openai
export AI_BASE_URL=https://api.openai.com/v1
export AI_MODEL=gpt-4o-mini
```

### Arrancar
```bash
cd backend
mvn spring-boot:run
```

El servidor arranca en `http://localhost:8080`.

### Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/auth/register` | Registro de usuario |
| POST | `/auth/login` | Login → JWT |
| GET | `/oppositions` | Listar oposiciones |
| POST | `/oppositions` | Crear oposición |
| GET | `/oppositions/{id}/blocks` | Bloques del temario |
| POST | `/blocks/{id}/topics` | Crear tema |
| PUT | `/topics/{id}` | Actualizar estado/dificultad |
| POST | `/topics/{id}/documents` | Subir documento |
| POST | `/ai/summary` | Resumen con IA |
| POST | `/ai/flashcards` | Generar flashcards con IA |
| POST | `/ai/questions/mcq` | Generar preguntas MCQ con IA |
| POST | `/oppositions/{id}/plan` | Generar plan de estudio |
| GET | `/oppositions/{id}/plan` | Consultar sesiones |
| PATCH | `/study-sessions/{id}` | Marcar sesión completada/saltada |
| POST | `/tests` | Crear test |
| POST | `/tests/{id}/submit` | Enviar respuestas |
| GET | `/stats/overview` | Resumen estadístico |
| GET | `/stats/opposition/{id}` | Estadísticas de oposición |

Todos los endpoints (excepto `/auth/**`) requieren header:
```
Authorization: Bearer <token>
```

### Integración con IA
El servicio `AIService` llama al endpoint de chat completions de OpenAI (formato compatible).
Para usar otro proveedor (Anthropic, etc.), cambia `AI_BASE_URL` y `AI_MODEL`.
El prompt para MCQ exige explícitamente **no inventar leyes** que no estén en el documento.

---

## App Android

### Requisitos
- Android Studio Hedgehog o superior
- SDK mínimo: API 26 (Android 8.0)
- Emulador o dispositivo físico

### Configuración

En `app/build.gradle` ajusta la URL del backend:
```groovy
buildConfigField "String", "BASE_URL", '"http://10.0.2.2:8080/"'  // emulador
// buildConfigField "String", "BASE_URL", '"http://192.168.X.X:8080/"' // red local
```

### Arquitectura
- **MVVM**: `ViewModel` + `LiveData`
- **Retrofit** para llamadas REST al backend
- **Room** para caché offline de oposiciones, bloques, temas y preguntas recientes
- **SessionManager** (SharedPreferences) para persistir el token JWT

### Pantallas implementadas
| Pantalla | Clase | Descripción |
|----------|-------|-------------|
| Login/Registro | `LoginActivity` | Formulario con validación |
| Lista oposiciones | `OppositionListActivity` | RecyclerView + caché Room |
| Detalle oposición | `OppositionDetailActivity` | Tabs: Temario / Plan / Stats |
| Lista temas | `TopicListFragment` | Bloques expandibles + spinner de estado |
| Crear test | `TestCreationActivity` | Selección modo/nº preguntas |
| Realizar test | `TestRunActivity` | Preguntas con timer, envío y resultado |
| Plan de estudio | `PlanFragment` | Sesiones del plan |
| Estadísticas | `StatsFragment` | % avance + historial notas |

---

## Modelo de datos

```
User
 └── Opposition (1:N)
      ├── Block (1:N)
      │    └── Topic (1:N)
      │         ├── Document (1:N)
      │         ├── Flashcard (1:N)
      │         └── Question (1:N)
      ├── StudySessionPlan (1:N)
      └── Test (1:N)
           ├── TestQuestion (1:N)
           └── TestResult (1:1)
                └── TestAnswer (1:N)
```

---

## Pendiente para producción
- Extracción de texto real de PDFs (Apache PDFBox / Tika)
- Integración con S3 para almacenamiento de documentos
- Notificaciones push (FCM)
- Integración con pasarela de pago (Stripe) para plan PRO
- Tests unitarios e integración
