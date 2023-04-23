use rocket::{
    response::status::{Created, NotFound},
    serde::json::Json,
    serde::{Serialize, Deserialize},
};

use tokio_postgres::{NoTls, Row};
use diesel::prelude::*;

use rest_api::{
    models::{Artist, NewArtist},
    schema::product,
    ApiError, PgConnection,
};
// use postgres::{Client, NoTls};

#[rocket::launch]
fn rocket() -> _ {
    rocket::build()
        // State
        .attach(PgConnection::fairing())
        // Routes
        .mount("/",rocket::routes![response, list, retrieve, create, create_many, list_conn, write_conn, write_many_conn],)
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

#[rocket::post("/database_write_many")]
async fn create_many(
    connection: PgConnection,
) -> &'static str  {
    let product = NewArtist { product_name: "Rust_Generated".to_string(), product_price: 99 };

    for _ in 0..10 {
        let product_clone = product.clone();

        connection.run(move |c| {
            diesel::insert_into(product::table)
                .values(&product_clone)
                .execute(c)
                .unwrap();
        });
    }

    "saved"
}

async fn get_products() -> Result<Vec<Artist>, tokio_postgres::Error> {
    // Connect to the Postgres database
    let (client, connection) = tokio_postgres::connect(
        "host=db user=docker password=docker dbname=exampledb port=5432",
        NoTls,
    ).await?;
    
    // Spawn a task to keep the Postgres connection alive
    tokio::spawn(async move {
        if let Err(e) = connection.await {
            eprintln!("error: {}", e);
        }
    });

    // Execute a query to retrieve all rows from the "products" table
    let rows = client.query("SELECT * FROM product", &[]).await?;

    // Map each row to a Product struct
    let products = rows.iter().map(|row| Artist {
        product_id: row.get(0),
        product_name: row.get(1),
        product_price: row.get(2),
    }).collect();

    Ok(products)
}

#[rocket::get("/database_read_conn")]
async fn list_conn() -> Json<Vec<Artist>> {
    let products = match get_products().await {
        Ok(products) => products,
        Err(_) => Vec::new(),
    };

    Json(products)
}

// async fn write_products() -> &'static str, tokio_postgres::Error> {
//     // Connect to the Postgres database
//     let (client, connection) = tokio_postgres::connect(
//         "host=db user=docker password=docker dbname=exampledb port=5432",
//         NoTls,
//     ).await?;
    
//     // Spawn a task to keep the Postgres connection alive
//     tokio::spawn(async move {
//         if let Err(e) = connection.await {
//             eprintln!("error: {}", e);
//         }
//     });

//     for i in 1..=10 {
//         client.execute("INSERT INTO product (product_name, product_price) VALUES ('Rust_Generated', 111)").unwrap();
//     }


//     "saved many"
// }



async fn write_product() -> Result<Vec<Artist>, tokio_postgres::Error> {
    let (client, connection) = tokio_postgres::connect(
        "host=db user=docker password=docker dbname=exampledb port=5432",
        NoTls,
    ).await?;
    
    // Spawn a task to keep the Postgres connection alive
    tokio::spawn(async move {
        if let Err(e) = connection.await {
            eprintln!("error: {}", e);
        }
    });

    client.query("INSERT INTO product (product_name, product_price) VALUES ('Rust_Generated', 111)", &[]).await?;

    let product = Artist { product_id: 1, product_name: "Rust_Generated".to_string(), product_price: 99 };
    let products = vec![product];
    Ok(products)
}

#[rocket::post("/database_write_conn")]
async fn write_conn() -> Json<Vec<Artist>> {
    let products = match write_product().await {
        Ok(products) => products,
        Err(_) => Vec::new(),
    };

    Json(products)
}

async fn write_products() -> Result<Vec<Artist>, tokio_postgres::Error> {
    let (client, connection) = tokio_postgres::connect(
        "host=db user=docker password=docker dbname=exampledb port=5432",
        NoTls,
    ).await?;
    
    // Spawn a task to keep the Postgres connection alive
    tokio::spawn(async move {
        if let Err(e) = connection.await {
            eprintln!("error: {}", e);
        }
    });

    for i in 1..=10 {
        client.query("INSERT INTO product (product_name, product_price) VALUES ('Rust_Generated', 111)", &[]).await?;

    }

    let product = Artist { product_id: 1, product_name: "Rust_Generated".to_string(), product_price: 99 };
    let products = vec![product];
    Ok(products)
}

#[rocket::post("/database_write_many_conn")]
async fn write_many_conn() -> Json<Vec<Artist>> {
    let products = match write_products().await {
        Ok(products) => products,
        Err(_) => Vec::new(),
    };

    Json(products)
}

// #[derive(Debug, Serialize, Deserialize)]
// struct Record {
//     product_name: String,
//     product_price: i32,
// }

// #[rocket::post("/database_write_many_conn")]
// fn save_records() -> &'static str {
//     let mut client = Client::connect(
//         "postgresql://docker:docker@db:5243/exampledb",
//         NoTls,
//     );
    
//     for i in 1..=10 {
//         let record = Record {
//             product_name: "Rust_Generated".to_string(),
//             product_price: i,
//         };
//         client.execute("INSERT INTO product (product_name, product_price) VALUES ($1, $2)", &[&record.product_name, &record.product_price]).unwrap();
//     }
//     "saved"
// }

// "INSERT INTO app_user (username, password, email) VALUES ($1, $2, $3)",
//         &[&"user1", &"mypass", &"user@test.com"]