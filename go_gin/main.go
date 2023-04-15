package main

import (
  "net/http"
  "github.com/gin-gonic/gin"
  "github.com/joho/godotenv"
  "log"
  "badawczy/database"
  "strconv"
  "badawczy/model"
  "fmt"
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

  router.Run(":8000") //running application, Default port is 8080
}