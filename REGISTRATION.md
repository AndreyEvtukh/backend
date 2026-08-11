# JWT Registration с Email Подтверждением

## Описание
Добавлена функциональность регистрации пользователей с подтверждением через email.

### Процесс регистрации
1. **Инициирование регистрации** (POST `/api/auth/register`)
   - Frontend отправляет email и username
   - Backend генерирует 6-значный код и отправляет на email
   - Код действует 15 минут

2. **Подтверждение и создание пользователя** (POST `/api/auth/verify-code`)
   - Frontend отправляет email и код подтверждения
   - Backend проверяет код, создаёт пользователя в БД
   - Пользователь готов для логина

## Конфигурация Email

### Используемый SMTP: Gmail

Для использования Gmail SMTP нужно:

1. Включить 2-factor authentication на аккаунте Google
2. Создать "App Password" на странице https://myaccount.google.com/apppasswords
3. Установить переменные окружения перед запуском:

```bash
set MAIL_USERNAME=ваш-email@gmail.com
set MAIL_PASSWORD=ваш-app-password-из-гугла
set JWT_SECRET=ваш-секрет-для-jwt
set DB_USER=ваш-user-бд
set DB_PASSWORD=ваш-пароль-бд
```

### Альтернатива: Testmail или MailHog (для разработки)

Если не хотите использовать реальный email, можно настроить MailHog локально:

```bash
docker run -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

Затем обновить `application.yml`:
```yaml
mail:
  host: localhost
  port: 1025
```

## Примеры API запросов

### 1. Инициирование регистрации
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "username": "john_doe"
  }'
```

Ответ:
```json
{
  "message": "Verification code sent to user@example.com"
}
```

### 2. Подтверждение кода и создание пользователя
```bash
curl -X POST http://localhost:8080/api/auth/verify-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "verificationCode": "123456"
  }'
```

Ответ:
```json
{
  "message": "User registered successfully"
}
```

### 3. Логин (получение JWT)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe"
  }'
```

Ответ:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 4. Использование токена для защищённых запросов
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  http://localhost:8080/graphql
```

## БД: Миграция

Таблица `registration_tokens` создаётся автоматически при запуске (если `ddl-auto` не установлен на `validate`).

Если используется `ddl-auto: validate`, создайте таблицу вручную:

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

## Реализация

### Новые файлы:
- `model/RegistrationToken.java` — сущность для хранения данных регистрации
- `repository/RegistrationTokenRepository.java` — репозиторий
- `service/RegistrationService.java` — бизнес-логика регистрации
- `config/MailConfig.java` — конфигурация email

### Обновлённые файлы:
- `controller/AuthController.java` — добавлены эндпоинты `/register` и `/verify-code`
- `repository/UserRepository.java` — добавлен метод `findByEmail()`
- `pom.xml` — добавлена зависимость `spring-boot-starter-mail`
- `application.yml` — добавлена конфигурация для SMTP

## Примечания по безопасности

1. **Код подтверждения**: 6-значный код, действует 15 минут
2. **Email**: Проверяется уникальность при регистрации
3. **Username**: Проверяется уникальность и во время инициирования, и при подтверждении
4. **Пароль**: На текущий момент не требуется (можно добавить в отдельном шаге)
5. **Rate limiting**: Рекомендуется добавить на уровне контроллера для защиты от brute-force

## Дальнейшие улучшения

- [ ] Добавить проверку пароля при регистрации
- [ ] Реализовать забывчивость пароля (forgot password)
- [ ] Добавить повторную отправку кода (resend code)
- [ ] Rate limiting для эндпоинтов регистрации
- [ ] OAuth2 интеграция (Google, GitHub)
- [ ] Email шаблоны (HTML emails вместо plain text)
