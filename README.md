# demo-graphql-jwt

Минимальный Spring Boot проект: GraphQL + JWT + подключение к Postgres (Supabase).

## Что делает

Запрос `test` через GraphQL достаёт все строки из таблицы `users` в вашей Supabase БД
и возвращает их в JSON на фронтенд. Эндпоинт защищён JWT — без валидного токена
в заголовке `Authorization: Bearer <token>` запрос будет отклонён (401/403).

## Перед запуском — обязательно проверьте

1. **Схема таблицы `users`.** В `User.java` я задал поля `id, email, name, created_at`
   как предположение — это не подтверждённые данные о вашей реальной таблице.
   Откройте Supabase → Table Editor → `users` и приведите поля/типы в соответствие.
2. **Пароль от БД.** Он не был передан мне и не должен храниться в коде.
   Установите переменную окружения перед запуском:
   ```bash
   export DB_PASSWORD=ваш_пароль_из_supabase
   ```
3. **JWT-секрет.** В `application.yml` стоит dev-заглушка. Для реального использования:
   ```bash
   export JWT_SECRET=$(openssl rand -base64 32)
   ```
4. **Генерация токена.** В этом скелете нет `/auth/login` эндпоинта (в задаче он не
   запрашивался) — `JwtService.generateToken(...)` уже готов, но вызвать его сейчас
   неоткуда. Для теста можно временно сгенерировать токен вручную (например, в
   `@PostConstruct` или тестовым классом) либо добавить простой REST-контроллер логина.

## Запуск

```bash
export DB_PASSWORD=...
export JWT_SECRET=...
mvn spring-boot:run
```

(Maven wrapper — файлы `mvnw`/`mvnw.cmd` — в архив не включён; используйте
локально установленный Maven или сгенерируйте wrapper командой `mvn -N wrapper:wrapper`.)

GraphiQL-консоль (для ручной проверки без FE): http://localhost:8080/graphiql

## Пример запроса с FE

```graphql
query {
  test {
    id
    email
    name
    createdAt
  }
}
```

Заголовок запроса:
```
Authorization: Bearer <ваш JWT>
Content-Type: application/json
```

## Технологии и версии (для самостоятельной проверки актуальности)

- Spring Boot 3.3.4 (spring-boot-starter-graphql, spring-boot-starter-security,
  spring-boot-starter-data-jpa)
- io.jsonwebtoken (jjwt) 0.12.6
- PostgreSQL JDBC driver (версия управляется Spring Boot BOM)

Перед продакшн-использованием сверьте версии зависимостей на
https://mvnrepository.com — я указал версии, актуальные на момент написания,
но не могу гарантировать, что они останутся последними к моменту вашей сборки.
