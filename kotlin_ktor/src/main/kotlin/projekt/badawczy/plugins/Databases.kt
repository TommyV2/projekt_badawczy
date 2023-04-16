package projekt.badawczy.plugins

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.jackson.KtormModule
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import projekt.badawczy.plugins.Products.products

fun Application.configureDatabases() {

    val database = Database.connect("jdbc:postgresql://db:5432/exampledb", user = "docker", password = "docker")
    val objectMapper = jacksonObjectMapper()
    objectMapper.registerModule(KtormModule())

    routing {

        post("/database_write") {
            val jsonString:String = call.receiveText()
            val newProduct: Product = objectMapper.readValue(jsonString)
            database.products.add(newProduct)
            call.respond(HttpStatusCode.Created)
        }

        get("/database_read") {
            val products = database.products.toList()
            val productsJson = objectMapper.writeValueAsString(products)
            call.respondText(productsJson, contentType = ContentType.Application.Json, HttpStatusCode.OK)
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