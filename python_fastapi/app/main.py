from typing import Union

from fastapi import FastAPI
import psycopg2

app = FastAPI()


@app.get("/response")
def get_response():
    return {"Hello": "World"}


@app.get("/database_read")
async def get_product_price():
    # Connect to our database
    conn = psycopg2.connect(
        database="exampledb",
        user="docker",
        password="docker",
        host="db",
        port=5432,
    )
    cur = conn.cursor()
    cur.execute("SELECT MAX(product_price) FROM product;")
    result = cur.fetchall()
    print(f"{result=}")
    cur.close()
    conn.close()
    return result

@app.post("/database_write", status_code=201)
async def add_record():
    print("Create endpoint hit!!")

    conn = psycopg2.connect(
        database="exampledb",
        user="docker",
        password="docker",
        host="db",
        port="5432",
    )
    cur = conn.cursor()
    cur.execute(
        f"INSERT INTO product (product_name, product_price) VALUES ('Chair', '203' )"
    )
    conn.commit()
    cur.close()
    conn.close()

