#[macro_use] extern crate rocket;

#[get("/response")]
fn response() -> &'static str {
    "Hello, world!"
}

#[get("/hello/<name>")]
fn hello(name: &str) -> String {
    format!("Hello, {}!", name)
}


#[launch]
fn rocket() -> _ {
    rocket::build()
        .mount("/", routes![response])
        .mount("/", routes![hello])
}