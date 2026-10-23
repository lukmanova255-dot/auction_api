\# Auction API — 1bac24020



REST API для аукциона коллекционных предметов. Spring Boot 4.1.1 + Maven + PostgreSQL 17.



\## Сущности



\- User — пользователь системы

\- CollectibleItem — коллекционный предмет (название, описание, состояние, владелец)

\- AuctionLot — аукционный лот (предмет, стартовая цена, шаг, сроки, статус)

\- Bid — ставка на лот (пользователь, сумма, время)

\- AuctionDeal — сделка по лоту (выигравшая ставка, сумма, статус)



\## Технологии



\- Java 25

\- Spring Boot 4.1.1

\- Spring Data JPA (Hibernate)

\- PostgreSQL 17

\- Maven



\## Запуск



1\. Установить PostgreSQL 17.

2\. Создать БД:

&#x20;  CREATE DATABASE auction\_db;

&#x20;  CREATE USER auction\_user WITH PASSWORD 'auction\_pass\_123';

&#x20;  GRANT ALL PRIVILEGES ON DATABASE auction\_db TO auction\_user;

3\. Настроить application.yml.

4\. Запустить: ./mvnw spring-boot:run

5\. API: http://localhost:8080



\## CRUD



По 5 эндпоинтов на каждую сущность:

GET /api/(resources)

GET /api/(resources)/{id}

POST /api/(resources)

PATCH /api/(resources)/{id}

DELETE /api/(resources)/{id}



\## Бизнес-операции



1\. POST /api/business/lots/{lotId}/close — закрыть лот, создать сделку

2\. POST /api/business/deals/{dealId}/pay — оплатить сделку

3\. POST /api/business/bids/{bidId}/withdraw?userId=... — отозвать ставку

4\. POST /api/business/lots/{lotId}/cancel — отменить лот

5\. POST /api/business/items/{itemId}/transfer?newOwnerId=... — передать предмет



\## Ограничения



\- users.username UNIQUE

\- FK: collectible\_items.owner\_id → users.id

\- FK: auction\_lots.item\_id → collectible\_items.id

\- FK: bids.lot\_id → auction\_lots.id, bids.user\_id → users.id

\- FK: auction\_deals.lot\_id → auction\_lots.id (UNIQUE)

\- FK: auction\_deals.winning\_bid\_id → bids.id (UNIQUE)



\## Коллекция запросов



См. requests.http



\## Автор



Студент: 1bac24020



