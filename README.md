# 🌐 Translaator Text Azure - Backend

Este proyecto implementa un microservicio **reactivo** que permite traducir textos entre diferentes idiomas utilizando **Azure Cognitive Services Translator API**. También detecta el idioma de entrada y consulta los idiomas disponibles. Las traducciones son persistidas en una base de datos **PostgreSQL** utilizando **Spring WebFlux + R2DBC**.

---

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.3.3**
- **Spring WebFlux (programación reactiva)**
- **R2DBC (Reactive PostgreSQL)**
- **Azure Cognitive Services – Translator**
- **OkHttp + Gson + JSON**
- **Lombok**
- **Springdoc OpenAPI**
- **Docker Ready (opcional)**

---

## 🧩 Funcionalidades Principales

### ✅ Traducción de Texto
Traduce textos entre dos idiomas utilizando Azure Cognitive Services.

### 🧠 Detección de Idioma
Detecta el idioma original del texto enviado.

### 🌍 Idiomas Soportados
Consulta todos los códigos de idioma soportados por el servicio de traducción.

### 💾 Persistencia de Traducciones
Guarda, actualiza, consulta, y elimina traducciones en PostgreSQL.

---

## 📄 Endpoints REST

| Método | Endpoint                     | Descripción                              |
|--------|------------------------------|------------------------------------------|
| POST   | `/translate`                 | Traduce un texto de un idioma a otro     |
| GET    | `/translate/ping`            | Verifica conexión a la base de datos     |
| GET    | `/translate/last`            | Obtiene la última traducción guardada    |
| GET    | `/translate/all`             | Lista todas las traducciones             |
| GET    | `/translate/actives`         | Traducciones con estado `A` (activa)     |
| GET    | `/translate/inactives`       | Traducciones con estado `I` (inactiva)   |
| PUT    | `/translate/edit/{id}`       | Edita una traducción por ID              |
| PUT    | `/translate/activate/{id}`   | Reactiva una traducción inactiva         |
| DELETE | `/translate/delete-log/{id}` | Elimina lógicamente una traducción       |
| DELETE | `/translate/delete-fis/{id}` | Elimina físicamente una traducción       |
| GET    | `/lenguajes`                 | Devuelve los idiomas disponibles         |
| POST   | `/detect`                    | Detecta el idioma de un texto            |

---

## 🗄️ Base de Datos: PostgreSQL

La tabla principal del sistema es `translations`. Guarda las traducciones con validaciones de integridad de datos.

### 🔧 Script de Creación

```sql
CREATE TABLE translations (
    id SERIAL PRIMARY KEY,
    request_text TEXT NOT NULL CHECK (length(request_text) <= 1000),
    translated_text TEXT NOT NULL CHECK (length(translated_text) <= 1000),
    from_lang TEXT NOT NULL CHECK (length(from_lang) = 2),
    to_lang TEXT NOT NULL CHECK (length(to_lang) = 2),
    status TEXT NOT NULL DEFAULT 'A' CHECK (status IN ('A', 'I'))
);
