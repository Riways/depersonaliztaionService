# Encoderlab

Лабораторная работа. REST-сервис на Spring Boot: находит email и телефоны в тексте,
удаляет их, поддерживает batch, кэш, счётчик и сохранение в БД.

## Запуск

```bash
mvn spring-boot:run
```

Приложение поднимается на `http://localhost:8080`.

## Эндпоинты

### GET /api/v1/personal-data

Параметры:
- `text` — текст (1–10000 символов)
- `mode` — `extract` или `remove`

```bash
curl "http://localhost:8080/api/v1/personal-data?text=a@b.com&mode=extract"
```

Ответ:
```json
{"emails":["a@b.com"],"phones":[],"sanitizedText":""}
```

### POST /api/v1/personal-data/bulk

```bash
curl -X POST http://localhost:8080/api/v1/personal-data/bulk \
  -H "Content-Type: application/json" \
  -d '{"texts":["a@b.com","c@d.org"],"mode":"extract"}'
```

Тело: `{"texts": [...], "mode": "extract"}`. До 100 элементов.

### GET /api/v1/personal-data/counter

```json
{"count": 42}
```

## H2 Console

`http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: пусто

## Тесты

```bash
mvn test
```

- `PersonalDataServiceTest` — unit на сервис
- `PersonalDataControllerTest` — MockMvc
- `PersonalDataResultRepositoryTest` — @DataJpaTest
- `PersonalDataControllerIntegrationTest` — через TestRestTemplate

## Ошибки

400 — пустой `text`, слишком длинный, невалидный `mode`, пустой/большой список.
```json
{"error":"VALIDATION_FAILED","details":["text: text must not be blank"]}
```

500 — внутренняя ошибка.
```json
{"error":"INTERNAL_ERROR"}
```

## Ограничения

- Regex поддерживает только Беларусь (`+375`, `80`).
- H2 in-memory — данные теряются при рестарте.
- `/counter` без авторизации.
- В логах только `mode` и `length`, текст не пишется (PII).
