# Быстрый старт: JWT + Email Registration

## Предварительная настройка (один раз)

### 1. Зависимости уже добавлены в `pom.xml`:
- `spring-boot-starter-mail` — для отправки email
- `flyway-core` — для миграций БД
- `jjwt` — для JWT токенов

### 2. Переменные окружения (перед запуском)

**Для Windows (cmd.exe):**
```cmd
set MAIL_USERNAME=ваш-email@gmail.com
set MAIL_PASSWORD=ваш-app-password-из-гугла
set JWT_SECRET=ваш-секрет-для-jwt-минимум-32-символа
set DB_USER=ваш-db-user
set DB_PASSWORD=ваш-db-password
```

**Для Linux/Mac (bash/zsh):**
```bash
export MAIL_USERNAME=ваш-email@gmail.com
export MAIL_PASSWORD=ваш-app-password-из-гугла
export JWT_SECRET=ваш-секрет-для-jwt-минимум-32-символа
export DB_USER=ваш-db-user
export DB_PASSWORD=ваш-db-password
```

### 3. Подготовка Gmail (для email отправки)

Если используете Gmail:
1. Включить 2FA: https://myaccount.google.com/security
2. Создать App Password: https://myaccount.google.com/apppasswords
3. Использовать полученный пароль как `MAIL_PASSWORD`

**Альтернатива (для локальной разработки):** MailHog
```bash
docker run -p 1025:1025 -p 8025:8025 mailhog/mailhog
```
Затем обновить в `application.yml`:
```yaml
spring:
  mail:
    host: localhost
    port: 1025
```

## Запуск

```bash
cd d:\Projects\portfolio2026\backend
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

Приложение запустится на **http://localhost:8080**

## Процесс регистрации (примеры)

### Шаг 1: Инициирование регистрации
**Отправляем:** email и username
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "username": "alice"
  }'
```

**Ответ:**
```json
{
  "message": "Verification code sent to alice@example.com"
}
```

На указанный email придёт письмо с 6-значным кодом (действует 15 минут).

### Шаг 2: Подтверждение кода и создание пользователя
**Отправляем:** email и код подтверждения
```bash
curl -X POST http://localhost:8080/api/auth/verify-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "verificationCode": "123456"
  }'
```

**Ответ:**
```json
{
  "message": "User registered successfully"
}
```

Пользователь создан в БД и готов к логину.

### Шаг 3: Логин (получение JWT)
**Отправляем:** username
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice"
  }'
```

**Ответ:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbGljZSIsImlhdCI6MTY5MTc0MDgwMCwiZXhwIjoxNjkxNzQ0NDAwfQ.7Z9..."
}
```

### Шаг 4: Использование токена
**Отправляем:** заголовок Authorization с токеном
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  http://localhost:8080/graphql
```

Запрос выполнится с аутентификацией.

## Структура БД

### Таблица `users` (существующая)
```
- id (INT, PK)
- username (VARCHAR)
- email (VARCHAR)
- password_hash (VARCHAR)
- enabled (BOOLEAN)
- role (VARCHAR)
- created_at (TIMESTAMP)
```

### Таблица `registration_tokens` (новая, создаётся Flyway)
```
- id (INT, PK)
- email (VARCHAR, UNIQUE)
- username (VARCHAR)
- verification_code (VARCHAR)
- is_verified (BOOLEAN)
- created_at (TIMESTAMP)
- expires_at (TIMESTAMP)
```

Таблица `registration_tokens` создаётся автоматически при первом запуске (Flyway миграция: `V1__create_registration_tokens_table.sql`).

## Файлы, которые были добавлены/изменены

### Новые файлы
- `model/RegistrationToken.java` — JPA сущность
- `repository/RegistrationTokenRepository.java` — Spring Data JPA репозиторий
- `service/RegistrationService.java` — сервис регистрации (отправка кода, подтверждение)
- `config/MailConfig.java` — конфигурация JavaMailSender
- `src/main/resources/db/migration/V1__create_registration_tokens_table.sql` — Flyway миграция

### Обновлённые файлы
- `controller/AuthController.java` — добавлены эндпоинты `/register` и `/verify-code`
- `repository/UserRepository.java` — добавлен метод `findByEmail()`
- `pom.xml` — добавлены зависимости `spring-boot-starter-mail`, `flyway-core`, `flyway-database-postgresql`
- `src/main/resources/application.yml` — добавлена конфигурация `spring.mail.*`

## Тестирование

### Вариант 1: Использовать встроенный скрипт (Windows)
```cmd
test-registration.bat
```

### Вариант 2: Вручную с curl

```bash
# 1. Регистрация
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "username": "testuser"}'

# 2. Подтверждение (замените CODE на код из email)
curl -X POST http://localhost:8080/api/auth/verify-code \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "verificationCode": "CODE"}'

# 3. Логин
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser"}'

# 4. Защищённый запрос (замените TOKEN на полученный токен)
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/graphql
```

### Вариант 3: Использовать Postman/Insomnia

Импортируйте эти запросы в Postman:

**Register**
- Method: POST
- URL: `http://localhost:8080/api/auth/register`
- Body (JSON): `{"email": "test@example.com", "username": "testuser"}`

**Verify Code**
- Method: POST
- URL: `http://localhost:8080/api/auth/verify-code`
- Body (JSON): `{"email": "test@example.com", "verificationCode": "123456"}`

**Login**
- Method: POST
- URL: `http://localhost:8080/api/auth/login`
- Body (JSON): `{"username": "testuser"}`

**Protected Request (GraphQL)**
- Method: GET
- URL: `http://localhost:8080/graphql`
- Headers: `Authorization: Bearer <TOKEN_FROM_LOGIN>`

## Безопасность

### Текущие механизмы защиты
- ✅ 6-значный код подтверждения (действует 15 минут)
- ✅ Email unique в БД (не может быть две регистрации на один email)
- ✅ Username unique в БД
- ✅ JWT токены для защиты эндпоинтов
- ✅ Stateless аутентификация (SessionCreationPolicy.STATELESS)
- ✅ CSRF отключен (для REST/GraphQL API — правильно)

### Рекомендации для production
- [ ] Добавить rate limiting для эндпоинтов регистрации (защита от brute-force)
- [ ] Добавить проверку пароля (BCrypt хеширование)
- [ ] Добавить повторную отправку кода (resend code endpoint)
- [ ] Добавить логирование попыток регистрации
- [ ] HTML email шаблоны вместо plain text
- [ ] Двойная проверка email (click link in email)
- [ ] CORS конфигурация для фронтенда
- [ ] HTTPS (обязательно)

## Troubleshooting

### Ошибка: "Failed to send verification email"
- Проверьте переменные окружения `MAIL_USERNAME` и `MAIL_PASSWORD`
- Убедитесь, что интернет соединение работает
- Если используете Gmail, проверьте что app password правильный

### Ошибка: "Email already registered"
- Email уже существует в таблице `users`
- Используйте другой email или удалите пользователя из БД

### Ошибка: "Invalid email or verification code"
- Проверьте что код правильный (из email)
- Проверьте что прошло не более 15 минут с момента отправки
- Email должен точно совпадать

### Таблица `registration_tokens` не создана
- Убедитесь что зависимость Flyway добавлена
- Проверьте логи при запуске (должно быть сообщение про миграцию)
- Если необходимо, создайте таблицу вручную:
```sql
CREATE TABLE registration_tokens (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL,
    verification_code VARCHAR(6) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);
```

## Дополнительная информация

### JWT токен
- Содержит: username (subject)
- Время жизни: 1 час (настраивается в `application.yml`: `jwt.expiration-ms`)
- Алгоритм: HS256 (HMAC SHA-256)
- Подпись: использует `JWT_SECRET` из переменных окружения

### Эндпоинты безопасности
- ✅ Публичные: `/api/auth/**`, `/graphiql`, `/api/users`, `/vendor/**`
- 🔒 Защищённые: всё остальное (требует JWT токен в заголовке Authorization)

### Процесс аутентификации
1. Frontend отправляет запрос с заголовком `Authorization: Bearer <token>`
2. `JwtAuthFilter` перехватывает запрос
3. Фильтр извлекает токен из заголовка
4. `JwtService.validateAndGetSubject()` проверяет подпись и срок действия
5. Если валиден — создаётся `UsernamePasswordAuthenticationToken` и кладётся в `SecurityContext`
6. Spring Security проверяет авторизацию на основе конфигурации в `SecurityConfig`
7. Если всё OK — запрос проходит дальше к контроллеру

---

**Последнее обновление:** август 2026
