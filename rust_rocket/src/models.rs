use rocket::serde::{Deserialize, Serialize};

use diesel::{AsChangeset, Insertable, Queryable};

use crate::schema::product;

#[derive(Serialize, Queryable, Debug)]
#[serde(crate = "rocket::serde")]
pub struct Artist {
    pub product_id: i32,
    pub product_name: String,
    pub product_price: i32,
}

#[derive(Deserialize, Insertable, Debug, Clone)]
#[serde(crate = "rocket::serde")]
#[table_name = "product"]
pub struct NewArtist {
    pub product_name: String,
    pub product_price: i32,
}

#[derive(Deserialize, AsChangeset, Debug)]
#[serde(crate = "rocket::serde")]
#[table_name = "product"]
pub struct UpdatedArtist {
    pub product_name: Option<String>,
    pub product_price: Option<i32>,
}
