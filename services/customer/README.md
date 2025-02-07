# Документация API Customer Service

## Описание
Customer сервис предоставляет REST API для управления клиентами. Этот сервис является автономным и может вызываться другими сервисами.

## Функциональные возможности
1. Создание нового клиента.
2. Удаление существующего клиента.
3. Изменение данных существующего клиента.
4. Получение всех клиентов.

## Хранение данных
Данные о клиентах хранятся в базе данных PostgreSQL.


## Базовый URL
```
http://localhost:8090
```

## Эндпоинты

### 1. Создание клиента
**Метод:** `POST`

**URL:** `/api/v1/customers`

**Описание:** Создаёт нового клиента.

**Тело запроса (JSON):**

```json
{
  "firstName": "Modi",
  "lastName": "Modi",
  "email": "modi@example.com",
  "address": {
    "zipCode": "12345",
    "houseNumber": "12",
    "street": "street"
  }
}
```

**Ответ (201 Created):**
```json
{
  "id": 1,
  "firstName": "Modi",
  "lastName": "Modi",
  "email": "modi@example.com",
  "address": {
    "zipCode": "12345",
    "houseNumber": "12",
    "street": "street"
  }
}
```

---

### 2. Получение всех клиентов
**Метод:** `GET`

**URL:** `/api/v1/customers`

**Описание:** Возвращает список всех клиентов.

**Ответ (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "Modi",
    "lastName": "Modi",
    "email": "modi@example.com",
    "address": {
      "zipCode": "12345",
      "houseNumber": "12",
      "street": "street"
    }
  },
  {
    "id": 2,
    "firstName": "Modi2",
    "lastName": "Modi2",
    "email": "modi2@example.com",
    "address": {
      "zipCode": "12345",
      "houseNumber": "123",
      "street": "street2"
    }
  }
]
```

---

### 3. Получение клиента по ID
**Метод:** `GET`

**URL:** `/api/v1/customers/{customer-id}`

**Описание:** Возвращает информацию о клиенте по его ID.

**Пример запроса:** `/api/v1/customers/1`

**Ответ (200 OK):**
```json
{
  "id": 1,
  "firstName": "Modi",
  "lastName": "Modi",
  "email": "modi@example.com",
  "address": {
    "zipCode": "12345",
    "houseNumber": "12",
    "street": "street"
  }
}
```

**Ответ при отсутствии клиента (404 Not Found):**
```json
{
  "message": "Клиент с указанным идентификатором 1 не найден"
}
```

---

### 4. Проверка существования клиента
**Метод:** `GET`

**URL:** `/api/v1/customers/{customer-id}/exists`

**Описание:** Проверяет, существует ли клиент с указанным ID.

**Пример запроса:** `/api/v1/customers/1/exists`

**Ответ (200 OK):**
```json
true
```

---

### 5. Обновление клиента
**Метод:** `PUT`

**URL:** `/api/v1/customers/{customer-id}`

**Описание:** Обновляет данные клиента.

**Пример запроса:** `/api/v1/customers/1`

**Тело запроса (JSON):**
```json
{
  "firstName": "Modi",
  "lastName": "Modi",
  "email": "modi@example.com",
  "address": {
    "zipCode": "12345",
    "houseNumber": "12",
    "street": "street"
  }
}
```

**Ответ (202 Accepted):** Пустой ответ.

**Ответ при отсутствии клиента (404 Not Found):**
```json
{
  "message": "Клиент с указанным идентификатором 1 не найден"
}
```

---

### 6. Удаление клиента
**Метод:** `DELETE`

**URL:** `/api/v1/customers/{customer-id}`

**Описание:** Удаляет клиента по его ID.

**Пример запроса:** `/api/v1/customers/1`

**Ответ (204 No Content):** Пустой ответ.

**Ответ при отсутствии клиента (404 Not Found):**
```json
{
  "message": "Клиент с указанным идентификатором 1 не найден"
}
```

---

## Ошибки

| Код | Описание |
|-----|----------|
| 400 | Некорректные данные в запросе |
| 404 | Клиент не найден |
| 500 | Внутренняя ошибка сервера |

---





