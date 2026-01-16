# EcoPulse – Home Energy Dashboard

A full-stack web application that helps households understand and optimize their electricity usage.
EcoPulse tracks energy consumption per device over time, correlates it with local weather, and provides insights to encourage more sustainable behavior.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Backend Setup (Spring Boot)](#backend-setup-spring-boot)
  - [Frontend Setup (React)](#frontend-setup-react)
- [Environment Configuration](#environment-configuration)
- [API Overview](#api-overview)
- [Database Model](#database-model)
- [Roadmap / Future Improvements](#roadmap--future-improvements)
- [Learning Goals](#learning-goals)
- [License](#license)

---

## Overview

Most people receive an electricity bill once a month but have little idea which devices or habits drive their energy usage.
EcoPulse aims to make home energy usage **visible**, **actionable**, and **sustainable**:

- Track daily kWh usage per device (e.g., AC unit, washing machine, lighting).
- Visualize trends over time.
- Pull in **weather data** and show how temperature affects energy consumption.
- Act as a portfolio project demonstrating full-stack skills (Spring Boot + React + API integration).

---

## Features

**Core Features:**

- User management (per-user data isolation).
- Device management: add/edit/list household devices.
- Energy logging: record daily energy usage per device.
- Time-series visualization (frontend) for daily energy consumption.
- Aggregated stats (e.g., total kWh per day over the last N days).

**Advanced / Resume-Worthy Features:**

- Integration with a **Weather API** (e.g., OpenWeatherMap):
  - Fetch current weather for the user’s location.
  - Cache responses to avoid unnecessary external calls.
  - Correlate weather with energy usage (e.g., higher AC usage on hot days).
- Clean layered architecture:
  - Entities, Repositories, Services, Controllers, DTOs.
- Input validation and basic security (per-user access to their own data).

---

## Architecture

The project is structured as a **full-stack** app:

- **Backend:** Spring Boot REST API
  - Handles business logic, data persistence, and calls to the external Weather API.
- **Frontend:** React single-page app (SPA)
  - Displays dashboards, charts, and forms for managing devices and energy readings.
- **Database:** H2 (in-memory) for development, with option to switch to PostgreSQL for production.

A typical request flow:

1. React frontend calls a REST endpoint (e.g., `GET /api/readings/stats?days=7`).
2. Spring Boot service loads energy readings from the database.
3. Backend optionally fetches or reuses cached weather data.
4. Data is aggregated and returned as JSON.
5. React renders charts and recommendations based on the response.

---

## Tech Stack

**Backend:**

- Java 17+
- Spring Boot 3.x
  - Spring Web
  - Spring Data JPA
  - Spring Security (for auth, later)
  - H2 Database (dev) / PostgreSQL (prod-ready)
  - Lombok
  - Validation (Jakarta)

**Frontend (planned):**

- React (Vite or Create React App)
- TypeScript (optional but recommended)
- React Router
- Axios (HTTP client)
- Recharts or Chart.js (for graphs)
- Tailwind CSS / Material UI / Chakra UI (styling)

**External Services:**

- OpenWeatherMap (or similar) for weather data.

---

## Getting Started

### Prerequisites

- **Java:** 17 or 21
- **Node.js:** 18+ (for frontend)
- **Maven:** (if not included with your IDE)
- (Optional) PostgreSQL if you want a persistent DB instead of H2

You can clone this repository and run backend and frontend separately.

---

### Backend Setup (Spring Boot)

1. **Clone the repository:**

   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   cd your-repo-name
   ```

2. **Navigate to the backend project folder**
   (Adjust if you named it differently, e.g., `energy-dashboard`):

   ```bash
   cd backend
   ```

3. **Configure application properties**

   For development, you can use H2 in-memory with a configuration similar to:

   ```yaml
   # src/main/resources/application.yml
   spring:
     application:
       name: energy-dashboard

     datasource:
       url: jdbc:h2:mem:ecopulse
       driver-class-name: org.h2.Driver
       username: sa
       password:

     h2:
       console:
         enabled: true
         path: /h2-console

     jpa:
       hibernate:
         ddl-auto: create-drop
       show-sql: true
       properties:
         hibernate:
           format_sql: true

   server:
     port: 8080

   weather:
     api:
       key: YOUR_OPENWEATHERMAP_API_KEY_HERE
       url: https://api.openweathermap.org/data/2.5/weather
   ```

   - Replace `YOUR_OPENWEATHERMAP_API_KEY_HERE` with your actual key.
   - The H2 console will be available at `http://localhost:8080/h2-console`.

4. **Run the backend**

   Using Maven:

   ```bash
   mvn spring-boot:run
   ```

   Or from your IDE, run the main class `EnergyDashboardApplication` (or equivalent).

5. **Verify it’s running**

   Hit a simple health endpoint (depending on what you implemented first), e.g.:

   ```bash
   curl http://localhost:8080/actuator/health
   ```

   Or a sample custom endpoint like:

   ```bash
   curl http://localhost:8080/api/devices
   ```

---

### Frontend Setup (React)

> If the frontend is not yet implemented, this section describes the planned setup.

1. **From the project root:**

   ```bash
   cd frontend
   ```

2. **Create the React app (if not already created):**

   ```bash
   npm create vite@latest ecopulse-ui -- --template react
   cd ecopulse-ui
   npm install
   npm install axios react-router-dom recharts
   ```

3. **Configure API base URL**

   Create `.env` in the React project:

   ```bash
   VITE_API_BASE_URL=http://localhost:8080
   ```

4. **Run the frontend dev server:**

   ```bash
   npm run dev
   ```

5. Open the app in your browser at the URL printed in the terminal (e.g., `http://localhost:5173`).

---

## Environment Configuration

**Backend Environment Variables (recommended for production):**

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `WEATHER_API_KEY` (if you externalize the key instead of hardcoding in `application.yml`)

Example `application-prod.yml` (PostgreSQL):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecopulse
    username: ecopulse_user
    password: strong_password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

---

## API Overview

> This is a high-level overview; see the code for full details.

Base URL: `http://localhost:8080/api`

### Devices

- `GET /api/devices`
  List all devices for the authenticated user.

- `POST /api/devices`
  Create a new device.

  **Request body (example):**

  ```json
  {
    "name": "Living Room AC",
    "location": "Living Room",
    "type": "HVAC",
    "estimatedPowerWatts": 1500
  }
  ```

### Energy Readings

- `POST /api/readings`
  Log a new energy reading for a device.

  ```json
  {
    "deviceId": 1,
    "date": "2026-01-16",
    "energyKwh": 4.5,
    "source": "manual"
  }
  ```

- `GET /api/readings/stats?days=7`
  Get aggregated daily energy usage for the last N days.

  **Sample response:**

  ```json
  [
    { "date": "2026-01-10", "totalKwh": 12.3 },
    { "date": "2026-01-11", "totalKwh": 10.8 }
  ]
  ```

### Weather (planned endpoint)

- `GET /api/weather/current`
  Returns current weather for the user’s configured location (using a cached call to the external Weather API).

---

## Database Model

**Entities:**

- `User`
  - `id`, `email`, `passwordHash`, `name`, `homeLocation`, `createdAt`
  - Relationships: `1:N` with `Device`

- `Device`
  - `id`, `user`, `name`, `location`, `type`, `estimatedPowerWatts`
  - Relationships: `N:1` with `User`, `1:N` with `EnergyReading`

- `EnergyReading`
  - `id`, `device`, `date`, `energyKwh`, `source`

- `WeatherCache`
  - `id`, `city`, `temperature`, `condition`, `timestamp`

---

## Roadmap / Future Improvements

- [ ] Implement full authentication & authorization (JWT or session-based).
- [ ] Role-based access (admin vs regular user).
- [ ] More advanced analytics:
  - Cost estimation based on local electricity rates.
  - Device efficiency score / recommendations.
- [ ] UI enhancements and polished dashboards.
- [ ] Deployment:
  - Backend to a cloud platform (Render / Railway / Heroku alternative).
  - Frontend to Netlify / Vercel.
- [ ] Automated tests:
  - Unit tests for services.
  - Integration tests for controllers.

---

## Learning Goals

This project is designed to practice and demonstrate:

- Building a **RESTful API** using Spring Boot.
- Modeling relational data with **JPA/Hibernate**.
- Implementing **DTOs**, service layers, and repository patterns.
- Integrating with a **third-party API** (Weather).
- Building a **React** frontend that consumes a Java backend.
- Handling **time-series data** and visualizing it with charts.
- Managing environments, configuration, and external secrets.

---

## License

This project is open-source under the MIT License.
Feel free to use it as a reference or starting point for your own portfolio.

```

You can paste this directly into a `README.md` file in your GitHub repo and then tweak project names/paths (like `backend`, `frontend`, or `energy-dashboard`) to match your actual folder structure.
