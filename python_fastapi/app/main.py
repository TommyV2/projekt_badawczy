from typing import Union
from fastapi import FastAPI
from sqlalchemy import create_engine, Column, Integer, String, Sequence, DDL, event
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import psycopg2

app = FastAPI()

# Define the ORM model for the product table
Base = declarative_base()

class Product(Base):
    __tablename__ = 'product'
    product_id = Column(Integer, primary_key=True, autoincrement=True)
    product_name = Column(String(255))
    product_price = Column(Integer)

# Connect to the database
DATABASE_URL = "postgresql://docker:docker@db/exampledb"
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# # Create the "product_id_seq" sequence in the database
# Base.metadata.create_all(engine, checkfirst=True)
# product_id_seq = Sequence('product_id_seq')
# event.listen(Product.__table__, 'after_create', DDL(f"ALTER SEQUENCE {product_id_seq.name} OWNED BY {Product.__tablename__}.{Product.product_id.name}"))

@app.get("/response")
def get_response():
    return {"Hello": "World"}

@app.get("/database_read_conn")
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
    cur.execute("SELECT * FROM product;")
    result = cur.fetchall()
    print(f"{result=}")
    cur.close()
    conn.close()
    return result

@app.get("/database_read")
async def get_product_price():
    # Retrieve all products from the database
    db = SessionLocal()
    products = db.query(Product).all()
    db.close()
    return products

@app.get("/product/{product_id}")
async def get_product_price(product_id: int):
    # Retrieve a product by its ID from the database
    db = SessionLocal()
    product = db.query(Product).filter(Product.product_id == product_id).first()
    db.close()
    return product

@app.post("/database_write_conn", status_code=201)
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
        f"INSERT INTO product (product_name, product_price) VALUES ('ABC', '203' )"
    )
    conn.commit()
    cur.close()
    conn.close()


@app.post("/database_write", status_code=201)
async def add_record():
    print("Create endpoint hit!!")

    # Add a new product record to the database
    db = SessionLocal()
    new_product = Product(product_name='ABC', product_price=203)
    db.add(new_product)
    db.commit()
    db.close()

@app.post("/database_write_many_conn", status_code=201)
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
    for insert in range (0, 10):
        cur.execute(
            f"INSERT INTO product (product_name, product_price) VALUES ('ABC{insert}', '20{insert}' )"
        )
    conn.commit()
    cur.close()
    conn.close()


@app.post("/database_write_many", status_code=201)
async def add_record():
    print("Create endpoint hit!!")
    # Add a new product record to the database
    db = SessionLocal()
    for insert in range (10, 10):
        new_product = Product(product_name=f'ABC{insert}', product_price=(200 + insert))
        db.add(new_product)
    db.commit()
    db.close()