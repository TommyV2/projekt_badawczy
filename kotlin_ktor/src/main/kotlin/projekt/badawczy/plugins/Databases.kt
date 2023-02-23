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

    // val dbConnection: Connection = connectToPostgres()
    // val cityService = CityService(dbConnection)
    routing {
        post("/database_write") {
            val id = "b25a6810-481f-4dd2-ae50-735d6e1f46fc"
            call.respond(HttpStatusCode.Created, id)
        }
        get("/database_read") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Application.connectToPostgres(): Connection {
    Class.forName("org.postgresql.Driver")
    // val url = environment.config.property("postgres.url").getString()
    // val user = environment.config.property("postgres.user").getString()
    // val password = environment.config.property("postgres.password").getString()

    val url = "jdbc:postgresql://0.0.0.0:5432/exampledb"
    val user = "docker"
    val password = "docker"

    return DriverManager.getConnection(url, user, password)
}
