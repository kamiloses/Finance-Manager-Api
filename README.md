

# Uruchomienie aplikacji

## 1. Przez Docker 

### Krok 1: Sklonuj repozytorium
```bash
git clone https://github.com/kamiloses/Finance-Manager-Api
cd Finance-Manager-Api
```

### Krok 2: Zbuduj obraz
```bash
docker compose up --build
```
### Krok 3: Zaimportuj przykładowe requesty z Postman Collection

### Krok 3: Uruchom Swaggera
http://localhost:8080/swagger-ui/index.html (opcjonalnie)






<br><br><br><br><br>

# Finance Manager API

Proste REST API do zarządzania budżetem osobistym.  
Aplikacja pozwala tworzyć konta, dodawać przychody i wydatki, przeglądać transakcje, generować podsumowanie oraz eksportować transakcje do CSV.



## Funkcje

### Konta
- lista wszystkich kont
- tworzenie nowego konta
- pobranie szczegółów konta wraz z aktualnym saldem
- usuwanie konta tylko wtedy, gdy nie ma przypisanych transakcji

### Transakcje
- lista transakcji dla konta
- filtrowanie po:
  - `from`
  - `to`
  - `category`
- dodawanie transakcji typu:
  - `INCOME`
  - `EXPENSE`
- automatyczna aktualizacja salda konta po dodaniu i usunięciu transakcji
- usuwanie transakcji z cofnięciem salda

### Podsumowanie
- łączny przychód
- łączny wydatek
- wydatki pogrupowane po kategorii

### Dodatkowo
- eksport transakcji konta do CSV
- obsługa ostrzeżenia przy przekroczeniu limitu budżetu dla kategorii
- dokumentacja API w OpenAPI / Swagger
- uruchamianie w formie kontenera 

## Technologie
- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Jakarta Validation
- JUnit 
- Mockito
- Swagger (OpenAPI)
- PostgreSQL
- Docker Compose

## Model danych

### Account
- `id`
- `name`
- `balance`

### Transaction
- `id`
- `amount`
- `type`
- `category`
- `description`
- `date`
- `account_id`

### BudgetLimit
- `id`
- `category`
- `limitAmount`

## Zasady biznesowe
- kwota transakcji musi być większa od `0`
- konto ma unikalną nazwę
- saldo konta aktualizuje się automatycznie po każdej transakcji
- konto można usunąć tylko wtedy, gdy nie posiada transakcji
- usunięcie transakcji cofa wcześniejszą zmianę salda
- przy wydatku można otrzymać ostrzeżenie, jeśli limit kategorii został przekroczony

## Endpoints

### Konta
- `GET /accounts` — lista kont
- `POST /accounts` — utworzenie konta
- `GET /accounts/{id}` — szczegóły konta
- `DELETE /accounts/{id}` — usunięcie konta

### Transakcje
- `GET /accounts/{accountId}/transactions` — lista transakcji z opcjonalnymi filtrami
- `POST /accounts/{accountId}/transactions` — dodanie transakcji
- `DELETE /accounts/{accountId}/transactions/{transactionId}` — usunięcie transakcji

### Podsumowanie
- `GET /accounts/{accountId}/summary` — podsumowanie konta

### Export CSV
- `GET /accounts/{accountId}/transactions/export` — eksport transakcji do CSV

## Obsługa błędów
API zwraca czytelne odpowiedzi błędów w formacie JSON.

Stosowane statusy HTTP:
- `200 OK`
- `201 Created`
- `204 No Content`
- `400 Bad Request`
- `404 Not Found`
- `409 Conflict`

Przykładowe wyjątki:
- konto nie istnieje
- transakcja nie istnieje
- konto ma już taką samą nazwę
- próba usunięcia konta z transakcjami
- błędy walidacji danych wejściowych

## Testy
W projekcie znajdują się testy:
- testy jednostkowe serwisów
- testy integracyjne kontrolerów
- testy repozytoriów JPA

Sprawdzane są m.in.:
- tworzenie kont
- wykrywanie duplikatu konta
- tworzenie i usuwanie transakcji
- aktualizacja salda
- filtrowanie transakcji
- podstawowe zapytania repozytoriów




