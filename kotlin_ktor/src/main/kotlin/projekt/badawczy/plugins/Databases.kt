package projekt.badawczy.plugins

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import kotlinx.coroutines.*
import java.sql.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureDatabases() {

    val dbConnection: Connection = connectToPostgres()
    routing {
        post("/database_write") {
            val newProduct = call.receive<NewProduct>()
            val INSERT_NEW_PRODUCT = "INSERT INTO product (product_name, product_price) VALUES (?, ?)"
            val statement = dbConnection.prepareStatement(INSERT_NEW_PRODUCT, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, newProduct.name)
            statement.setInt(2, newProduct.price)
            statement.executeUpdate()

            val generatedKeys = statement.generatedKeys
            if(generatedKeys.next()) {
                call.respond(HttpStatusCode.Created, generatedKeys.getInt(1))
            }

            call.respond(HttpStatusCode.Created, 0)
        }

        get("/database_read") {

            val SELECT_PRODUCTS = "SELECT * FROM product"
            val result = dbConnection.prepareStatement(SELECT_PRODUCTS).executeQuery()

            val products = mutableListOf<Product>()
            while(result.next()) {
                val id = result.getInt("product_id")
                val name = result.getString("product_name")
                val price = result.getInt("product_price")
                products.add(Product(id, name,price))
            }
            call.respond(HttpStatusCode.OK, products)
        }

        get("/product/{productId}") {
            val SELECT_PRODUCT = "SELECT * FROM product WHERE product_id=" + call.parameters["productId"]
            val result = dbConnection.prepareStatement(SELECT_PRODUCT).executeQuery()
            if(result.next()) {
                val id = result.getInt("product_id")
                val name = result.getString("product_name")
                val price = result.getInt("product_price")
                val product = Product(id, name, price)

                call.respond(HttpStatusCode.OK, product)
            }else call.respond(HttpStatusCode.NotFound)
        }
    }
}

fun Application.connectToPostgres(): Connection {
    Class.forName("org.postgresql.Driver")
    // val url = environment.config.property("postgres.url").getString()
    // val user = environment.config.property("postgres.user").getString()
    // val password = environment.config.property("postgres.password").getString()

    val url = "jdbc:postgresql://db:5432/exampledb"
    val user = "docker"
    val password = "docker"

    return DriverManager.getConnection(url, user, password)
}

@Serializable
data class Product(val id: Int, val name: String, val price: Int)

@Serializable
data class NewProduct(val name: String, val price: Int)