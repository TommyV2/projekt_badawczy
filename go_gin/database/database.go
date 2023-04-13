package database

import (
    "fmt"
    "gorm.io/driver/postgres"
    "gorm.io/gorm"
    "os"
	"badawczy/model"
	"log"
)

var Database *gorm.DB

func Connect() {
    var err error
    host := os.Getenv("DB_HOST")
    username := os.Getenv("DB_USER")
    password := os.Getenv("DB_PASSWORD")
    databaseName := os.Getenv("DB_NAME")
    port := os.Getenv("DB_PORT")

    dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable TimeZone=Africa/Lagos", host, username, password, databaseName, port)
    Database, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})

    if err != nil {
        panic(err)
    } else {
        fmt.Println("Successfully connected to the database")
    }
}

func GetAllProducts() []model.Product {
	var products []model.Product
	Database.Find(&products)
	return products
}

func GetProduct(id int) model.Product {
	var product model.Product
	Database.First(&product, id)
	return product
}

func AddProduct(product model.Product) {
	Database.Create(&product)
	log.Print("Added 1 record to DB")
}