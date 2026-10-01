@echo off
REM Скрипт для локального тестирования регистрации и JWT

REM Устанавливаем переменные окружения
set MAIL_USERNAME=your-email@gmail.com
set MAIL_PASSWORD=your-app-password
set JWT_SECRET=this-is-a-dev-only-secret-change-it-please-32bytes
set DB_USER=ваш-db-user
set DB_PASSWORD=ваш-db-password

echo.
echo ======================================
echo JWT Registration - Test Script
echo ======================================
echo.

REM Тестовые значения
set TEST_EMAIL=testuser%RANDOM%@example.com
set TEST_USERNAME=testuser%RANDOM%
set TEST_VERIFICATION_CODE=123456

echo 1. Инициирование регистрации (отправка кода на email)...
echo Отправляем email: %TEST_EMAIL%
echo Отправляем username: %TEST_USERNAME%
echo.

curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"email\": \"%TEST_EMAIL%\", \"username\": \"%TEST_USERNAME%\"}"

echo.
echo.
echo 2. Введите код подтверждения из email (или используйте дефолт 123456)
echo Нажмите Enter чтобы продолжить с кодом 123456...
pause

echo.
echo Проверяем код подтверждения...
echo.

curl -X POST http://localhost:8080/api/auth/verify-code ^
  -H "Content-Type: application/json" ^
  -d "{\"email\": \"%TEST_EMAIL%\", \"verificationCode\": \"%TEST_VERIFICATION_CODE%\"}"

echo.
echo.
echo 3. Получаем JWT токен (логинимся)...
echo.

curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\": \"%TEST_USERNAME%\"}"

echo.
echo.
echo ======================================
echo Тестирование завершено!
echo ======================================
echo.
