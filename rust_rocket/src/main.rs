use rocket::{
    response::status::{Created, NotFound},
    serde::json::Json,
};

use diesel::prelude::*;

use rest_api::{
    models::{Artist, NewArtist},
    schema::product,
    ApiError, PgConnection,
};

#[rocket::launch]
fn rocket() -> _ {
    rocket::build()
        // State
        .attach(PgConnection::fairing())
        // Routes
        .mount("/",rocket::routes![response, list, retrieve, create],)
}

#[rocket::get("/database_read")]
async fn list(connection: PgConnection) -> Json<Vec<Artist>> {
    connection
        .run(|c| product::table.load(c))
        .await
        .map(Json)
        .expect("Failed to fetch artists")
}

#[rocket::get("/response")]
fn response() -> &'static str {
    "Hello, world!"
}

#[rocket::get("/product/<id>")]
async fn retrieve(
    connection: PgConnection,
    id: i32,
) -> Result<Json<Artist>, NotFound<Json<ApiError>>> {
    connection
        .run(move |c| product::table.filter(product::product_id.eq(id)).first(c))
        .await
        .map(Json)
        .map_err(|e| {
            NotFound(Json(ApiError {
                details: e.to_string(),
            }))
        })
}

#[rocket::post("/database_write")]
async fn create(
    connection: PgConnection,
) -> Result<Created<Json<Artist>>, Json<ApiError>> {
    let product = NewArtist { product_name: "Rust_Generated".to_string(), product_price: 99 };
    connection
        .run(move |c| {
            diesel::insert_into(product::table)
                .values(&product)
                .get_result(c)
        })
        .await
        .map(|a| Created::new("/").body(Json(a)))
        .map_err(|e| {
            Json(ApiError {
                details: e.to_string(),
            })
        })
}
