# Middle-сервис для приложения "Мини-Банк"
Middle-сервис является центральной частью проекта "Мини-Банк", разрабатываемого в рамках бэкенд-академии [GPB IT FACTORY 2024](https://gpb.fut.ru/itfactory/backend).

## Описание сервиса

Данный сервис работает как связующий слой между frontend (Telegram-ботом) и backend.
Он принимает запросы от Telegram-бота, проводит их валидацию, выполняет необходимую бизнес-логику и маршрутизирует обработанные запросы на backend для выполнения.

## Стек технологий

- **Java Spring Boot** версия 3.3.0
- **Gradle** версия 8.7
- **JDK** версия 21.0.3

## Запуск проекта

Linux/MacOS:

1. Клонирование репозитория:
```
$ git clone https://github.com/gpb-it-factory/protasov-telegram-bot.git
$ cd protasov-telegram-bot
```
2. Запуск:

```
$ ./gradlew bootRun
```