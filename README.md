# projekt_badawczy

Uruchomienie:

`docker compose up` w głównym folderze

Do uruchomienia load testów wymagany jest w aktualnym momencie program JMeter

Założenia:

1. Każdy framework w oddzielnym folderze (przykład python_flask)
2. Do każdego dockerfile
3. Endpointy:

- /response - zwraca prostą odpowiedź GET
- /database_read - prosty read z bazy danych GET
- /database_write - prosty write do bazy danych POST

4. Baza danych postgres

## SPRINT 1

### (deadline 5.03.2023)

TODO:

- każdy robi 1 api ze swojej listy (+dockerfile działający z naszym wspólnym docker-compose)

## SPRINT 2

### (deadline 26.03.2023)

TODO:

- skrypt na seedowanie bazy danych - 1 tabela, 1 milion rekordów - **Kuba**
- skrypt do testowania czasu odpowiedzi endpointów (response, database_read, database_write), argument: N - wywołań, obliczanie średniej czasu **Tomek + Julian**
- load/stress testy ogarnięcie narzędzia 2os **Andrzej + Maciek**
- nginx (wszyscy)

## SPRINT 3
endpointy:

- /response
- /database_read
- /database_read_conn
- /database_write
- /database_write_conn
- /database_write_many
- /database_write_many_conn
- /product/:id


body:

// database_write


{
    "name": "ABC",
    "price": 20
}


// database_write_many

{
    "products": [
        {
            "name": "ABC",
            "price": 20
        },
        {
            "name": "ABC2",
            "price": 21
        }
    ]  
}
