package projekt.badawczy.plugins

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.json.JSONObject
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.ktorm.jackson.KtormModule
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import projekt.badawczy.plugins.Products.products
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureDatabases() {

    val database = Database.connect("jdbc:postgresql://db:5432/exampledb", user = "docker", password = "docker")
    val objectMapper = jacksonObjectMapper()
    objectMapper.registerModule(KtormModule())
    val dbConnection: Connection = connectToPostgres()

    routing {

        post("/database_write") {
            val jsonString: String = call.receiveText()
            val newProduct: Product = objectMapper.readValue(jsonString)
            database.products.add(newProduct)
            call.respond(HttpStatusCode.Created)
        }

        post("/database_write_many") {
            val jsonString = JSONObject(call.receiveText()).getJSONArray("products").toString()
            val newProduct: List<Product> = objectMapper.readValue(jsonString)
            newProduct.forEach { database.products.add(it) }
            call.respond(HttpStatusCode.Created)
        }

        get("/database_read") {
            val products = database.products.toList()
            val productsJson = objectMapper.writeValueAsString(products)
            call.respondText(productsJson, contentType = ContentType.Application.Json, HttpStatusCode.OK)
        }

        post("/database_write_conn") {
            val product = call.receive<ProductConnNew>()
            val INSERT_NEW_PRODUCT = "INSERT INTO product (product_name, product_price) VALUES (?, ?)"
            val statement = dbConnection.prepareStatement(INSERT_NEW_PRODUCT)
            statement.setString(1, product.name)
            statement.setInt(2, product.price)
            statement.execute()
            call.respond(HttpStatusCode.Created)
        }

        post("/database_write_many_conn") {
            val jsonString = JSONObject(call.receiveText()).getJSONArray("products").toString()
            val productList = Gson().fromJson(jsonString, Array<ProductConnNew>::class.java)
            var INSERT_NEW_PRODUCT = "INSERT INTO product (product_name, product_price) VALUES (?, ?)"
            for (i in productList.indices - 1) {
                INSERT_NEW_PRODUCT = "$INSERT_NEW_PRODUCT,(?, ?)"
            }

            val statement = dbConnection.prepareStatement(INSERT_NEW_PRODUCT)
            productList.forEachIndexed { index, product ->
                run {
                    statement.setString(2 * index + 1, product.name)
                    statement.setInt(2 * index + 2, product.price)
                }
            }
            statement.execute()
            call.respond(HttpStatusCode.Created)

        }

        get("/database_read_conn") {

            val SELECT_PRODUCTS = "SELECT * FROM product"
            val result = dbConnection.prepareStatement(SELECT_PRODUCTS).executeQuery()

            val products = mutableListOf<ProductConn>()
            while (result.next()) {
                val id = result.getLong("product_id")
                val name = result.getString("product_name")
                val price = result.getInt("product_price")
                products.add(ProductConn(id, name, price))
            }
            call.respond(HttpStatusCode.OK, products)
        }


        get("/product/{productId}") {
            val id = call.parameters["productId"]!!.toInt()
            val product = database.products.find { it.id eq id }
            val json = objectMapper.writeValueAsString(product)
            call.respondText(json, contentType = ContentType.Application.Json, HttpStatusCode.OK)
        }
    }
}

object Products : Table<Product>("product") {
    val Database.products get() = this.sequenceOf(Products)
    val id = int("product_id").primaryKey().bindTo { it.id }
    val name = varchar("product_name").bindTo { it.name }
    val price = int("product_price").bindTo { it.price }
}


interface Product : Entity<Product> {

    companion object : Entity.Factory<Product>()

    val id: Int
    var name: String
    var price: Int
}

fun connectToPostgres(): Connection {
    Class.forName("org.postgresql.Driver")

    val url = "jdbc:postgresql://db:5432/exampledb"
    val user = "docker"
    val password = "docker"

    return DriverManager.getConnection(url, user, password)
}

@Serializable
class ProductConn(val id: Long, var name: String, var price: Int)

@Serializable
class ProductConnNew(val name: String, val price: Int)

