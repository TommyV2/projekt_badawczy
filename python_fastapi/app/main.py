from typing import Union
from fastapi import FastAPI
from sqlalchemy import create_engine, Column, Integer, String, Sequence, DDL, event
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

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

@app.post("/database_write", status_code=201)
async def add_record():
    print("Create endpoint hit!!")

    # Add a new product record to the database
    db = SessionLocal()
    new_product = Product(product_name='Chair', product_price=203)
    db.add(new_product)
    db.commit()
    db.close()