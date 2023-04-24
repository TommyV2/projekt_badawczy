package main

import (
  "net/http"
  "github.com/gin-gonic/gin"
  "github.com/joho/godotenv"
  "log"
  "badawczy/database"
  "strconv"
  "badawczy/model"
  "github.com/jackc/pgx/v5"
  "fmt"
  "os"
  ctx "context"
)

func main() {
  loadEnv()
  loadDatabase()
  serveApplication()
}

func loadEnv() {
    err := godotenv.Load(".env")
    if err != nil {
        log.Fatal("Error loading .env file")
    }
}

// var conn *pgx.Conn

func loadDatabase() {
    database.Connect()
}


func serveApplication() {
  router := gin.Default() //new gin router initialization

  router.GET("/response", func(context *gin.Context) {
    context.JSON(http.StatusOK, gin.H{"data": "Hello World !"})    
  }) // first endpoint returns Hello World

  router.GET("/database_read", func(context *gin.Context) {
    context.JSON(http.StatusOK, database.GetAllProducts())    
  })

  router.GET("/database_read_conn", func(context *gin.Context) {
    conn, _ := pgx.Connect(ctx.Background(), os.Getenv("DB_URL"))
	  defer conn.Close(ctx.Background())
	  rows, _ := conn.Query(ctx.Background(), "select * from product")
    defer rows.Close()
    var products []model.Product
    for rows.Next() {
      product := model.Product{}
      rows.Scan(&product.Id, &product.Name, &product.Price)
      products = append(products, product)
    }
    log.Print(products)
    context.JSON(http.StatusOK, products)    
  })

  router.GET("/product/:id", func(context *gin.Context) {
    id, _ := strconv.Atoi(context.Param("id"))
    context.JSON(http.StatusOK, database.GetProduct(id))    
  })

  router.POST("/database_write", func(context *gin.Context) {
    product := model.Product{}
    // using BindJson method to serialize body with struct
    if err:=context.BindJSON(&product);err!=nil{
      context.AbortWithError(http.StatusBadRequest,err)
      return
    }
    fmt.Println(product)
	  database.AddProduct(product)
    context.JSON(http.StatusAccepted,&product)   
  })

  router.POST("/database_write_conn", func(context *gin.Context) {
    conn, _ := pgx.Connect(ctx.Background(), os.Getenv("DB_URL"))
	  defer conn.Close(ctx.Background())
	  conn.Exec(ctx.Background(), "insert into product (product_name, product_price) values ('nice product', 22)")
    context.JSON(http.StatusOK, "ok")    
  })

  router.POST("/database_write_many", func(context *gin.Context) {
    for i := 0; i < 10; i++ {
      product := model.Product{}
      product.Name = "Sample name"
      product.Price = int32(i)
      database.AddProduct(product)
    }
    context.JSON(http.StatusOK, "Done")
  })

  router.Run(":8000") //running application, Default port is 8080
}