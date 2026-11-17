$content = @'

\# Auction API — 1bac24020



REST API для аукциона коллекционных предметов.



\## Список лабораторных работ



Каждая лабораторная работа находится в отдельной ветке. Для просмотра кода перейдите по соответствующей ссылке:



\*   \[Лабораторная №5: JWT Authentication](https://github.com/lukmanova255-dot/auction\_api/tree/lab5) - Access/Refresh токены, управление сессиями.

\*   \[Лабораторная №4: Spring Security (Basic Auth)](https://github.com/lukmanova255-dot/auction\_api/tree/lab4) - Роли, регистрация, BCrypt.

\*   \[Лабораторная №3: PostgreSQL](https://github.com/lukmanova255-dot/auction\_api/tree/lab3) - JPA, связи, бизнес-операции.

\*   \[Лабораторная №2: REST API](https://github.com/lukmanova255-dot/auction\_api/tree/lab2) - In-memory коллекции, без БД.



\## Технологии

Java 25, Spring Boot 4.1.1, Spring Data JPA, Spring Security, PostgreSQL 17, JWT, Maven.

'@



$utf8NoBom = New-Object System.Text.UTF8Encoding $false

\[System.IO.File]::WriteAllText("$PWD\\README.md", $content, $utf8NoBom)

Write-Host "README.md обновлён"

