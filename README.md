Проект представляет собой приложение для учета калорий, использующее Spring Boot и PostgreSQL. Пользователи могут добавлять приемы пищи, рассчитывать калории и отслеживать их дневное потребление.

Технологии
Spring Boot — основной фреймворк для разработки приложения.
Spring Data JPA — для работы с базой данных и управлением сущностями.
PostgreSQL — база данных для хранения информации о пользователях и приемах пищи.
JUnit 5 — для написания юнит-тестов.
Maven — для управления зависимостями.

Функционал
Регистрация пользователей — создание пользователя с базовыми данными.
Добавление приемов пищи — возможность добавлять прием пищи с блюдами.
Подсчет калорий — система автоматически считает калории для каждого блюда и суммирует их для каждого приема пищи.
Проверка дневной нормы калорий — система позволяет проверить, не превышает ли потребление калорий дневную норму пользователя.
Валидация данных — проверка правильности введенных данных (например, проверка на отрицательные калории или неправильный формат даты).
Обработка ошибок — приложение возвращает подробные сообщения об ошибках (например, если пользователь не найден).
Тестирование — юнит-тесты для основной логики приложени

1. Клонирование репозитория:
Сначала клонируйте репозиторий на свою машину:

git clone https://github.com/Denskiybostan/calories-calculation.git
cd calories-calculation
2. Настройка PostgreSQL:
Убедитесь, что у вас установлен PostgreSQL.

Создайте базу данных для проекта:

CREATE DATABASE calories_calculation;
3. Настройка файла конфигурации:
Отредактируйте файл src/main/resources/application.properties, чтобы указать параметры подключения к базе данных:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/calories_calculation
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
4. Сборка и запуск проекта:
Для сборки проекта используйте Maven:

mvn clean install
Для запуска приложения:

mvn spring-boot:run
После этого приложение будет доступно по адресу: http://localhost:8080.

Использование API
1. Создание пользователя
Для регистрации нового пользователя отправьте запрос на создание:

POST /users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "height": 180,
  "weight": 75
}
2. Добавление приема пищи
Для добавления нового приема пищи с блюдами:

POST /meals
Content-Type: application/json

{
  "userId": 1,
  "dishes": [
    {
      "name": "Pasta",
      "calories": 500
    },
    {
      "name": "Salad",
      "calories": 200
    }
  ],
  "dateTime": "2025-03-31T12:30:00"
}
3. Получение всех приемов пищи пользователя
Для получения всех приемов пищи конкретного пользователя:

GET /meals/{userId}
Пример: GET /meals/1

4. Проверка дневной нормы калорий
Для проверки, не превышает ли потребление калорий дневную норму:

GET /meals/{userId}/calories
Пример: GET /meals/1/calories
