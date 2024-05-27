# Мини-Банк
Проект "Мини-Банк" разрабатывается в рамках бэкенд-академии [GPB IT FACTORY 2024](https://gpb.fut.ru/itfactory/backend). Проект включает в себя создание системы, состоящей из трёх компонентов: frontend (Telegram-бот), middle-слоя и backend.

## Архитектура системы
![Архитектура Мини-Банка](images/components.png)
<details>
  <summary>Посмотреть код PlantUML</summary>

```plantuml
@startuml
actor User as "Client"
participant "Frontend" as Frontend
participant "Middle" as Middle
participant "Backend" as Backend
User -> Frontend : Инициирует запрос
activate Frontend
Frontend -> Middle : Передать запрос
activate Middle
Middle -> Backend : Обработать запрос
activate Backend
Backend -> Middle : Ответ
deactivate Backend
Middle -> Frontend : Ответ
deactivate Middle
Frontend -> User : Ответ пользователю
deactivate Frontend
@enduml
```
</details>

### Компоненты системы
1. **Frontend (Telegram-бот)**
   - Выступает как клиентское приложение.
   - Инициирует запросы пользователей.

2. **Middle-слой (Java/Kotlin-сервис)**
   - Принимает запросы от Telegram-бота.
   - Выполняет валидацию и маршрутизацию запросов.

3. **Backend (Java/Kotlin-сервис)**
   - Обрабатывает банковские транзакции.
   - Хранит клиентские данные.

## Доступные операции

1. **/start** - Отправляет стандартный текст приветствия.

2. **/pong** - Отвечает сообщением "pong". 

## Стек технологий

### Платформы
- **Java Spring Boot** версия 3.3.0
- **Gradle** версия 8.7
- **JDK** версия 21.0.3

### Интерфейс
- java-telegram-bot-api
### База данных
- PostgreSQL

## Запуск проекта

Linux/MacOS:
1. Клонирование репозитория:
```
$ git clone https://github.com/gpb-it-factory/protasov-telegram-bot.git
$ cd protasov-telegram-bot
```
2. Настройка параметров бота:
```
$ export bot_name="твой_bot_name"
$ export bot_token="твой_bot_token"
```
3. Запуск приложения:
```
$ ./gradlew bootRun
```
## Материалы и ресурсы
- [Практические задания](https://github.com/gpb-it-factory/practice/tree/trunk/exercises)
