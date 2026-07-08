### [🎮 TicTacToe](https://github.com/Sokolpr/TicTacToe)

**Сервер для игры в «Крестики-нолики»** с поддержкой PvP, PvE (AI), JWT-авторизацией, рейтингом и историей игр.

---

#### 🧠 Функционал

- **Режимы игры:**
    - **PvP** — два игрока по очереди делают ходы
    - **PvE** — игра против ИИ на основе алгоритма **минимакс** (Minimax)
- **Авторизация и аутентификация:**
    - **JWT** (Access + Refresh токены)
    - Обновление токенов (refresh-токен хранится в БД)
    - Роли пользователей (`USER`)
- **Хранение данных:**
    - PostgreSQL (игры, пользователи, токены)
    - Spring Data JPA + Hibernate
- **REST API:**
    - Регистрация / Логин
    - Создание игры
    - Подключение к игре
    - Ход игрока
    - Получение состояния игры
    - Список доступных / активных игр
    - История завершённых игр
    - **Рейтинг игроков** (топ N по проценту побед)
- **Безопасность:** Spring Security, JWT-фильтр, BCrypt для паролей
- **Контейнеризация:** Docker + Docker Compose

---

#### 🛠️ Технологический стек

| Компонент | Технология |
|---|---|
| **Язык** | Java 21 |
| **Фреймворк** | Spring Boot 3, Spring Security, Spring Data JPA |
| **Авторизация** | JWT (Access + Refresh) |
| **База данных** | PostgreSQL |
| **Сборка** | Gradle (Kotlin DSL) |
| **Контейнеризация** | Docker, Docker Compose |

---

#### 📡 API Эндпоинты

| Метод   | URL | Описание |
|---------|---|---|
| `GET`   | `/auth/signup` | Регистрация |
| `POST`  | `/auth/login` | Логин (Access + Refresh) |
| `POST`  | `/auth/update/access` | Обновление Access-токена |
| `POST`  | `/auth/update/refresh` | Обновление Refresh-токена |
| `POST`  | `/game/create` | Создание игры (true — бот, false — PvP) |
| `POST`  | `/game/{uuidGame}/join` | Подключение к игре (1 — X, 2 — O) |
| `POST`  | `/game/{uuidGame}` | Ход игрока |
| `GET` | `/game/{uuidGame}/get` | Получить состояние игры |
| `POST`  | `/game/all` | Список доступных игр |
| `POST`  | `/game/get_current` | Активные игры пользователя |
| `POST`  | `/game/history` | История игр пользователя |
| `POST`  | `/game/game_over` | Все завершённые игры |
| `POST`  | `/game/best` | Топ N игроков по % побед |

---

#### 🐳 Запуск

```bash
docker-compose up --build