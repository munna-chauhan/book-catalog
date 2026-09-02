# book-catalog

A Spring Boot REST API for managing a book catalog with search support.

## Overview

Provides CRUD operations for books and a case-insensitive partial-match search endpoint.

## Build

```bash
mvn package
```

## Run

**Local:**
```bash
java -jar target/book-catalog-*.jar
```

**Docker:**
```bash
docker build -t book-catalog .
docker run -p 8080:8080 book-catalog
```

**Docker Compose:**
```bash
docker-compose up
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /books | Create a book |
| GET | /books | List all books |
| GET | /books/{id} | Get book by ID |
| DELETE | /books/{id} | Delete book by ID |
| GET | /books/search?q={query} | Search books by title or author (case-insensitive partial match); returns 200 with empty array when no matches found |
