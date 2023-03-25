var express = require("express");
const { Pool } = require("pg")

var app = express();

const pool = new Pool({
    user: 'docker',
    host: 'db',
    database: 'exampledb',
    password: 'docker',
    port: 5432
});

const readData = async () => {
    res = await pool.query("SELECT * FROM product");

    return res;
};

const writeData = async () => {
    const [product_name, price] = ["Generated", 999];
    res = await pool.query("INSERT INTO product (product_name, product_price) VALUES ($1, $2)",
        [product_name, price]
    );

    return res;
};

app.listen(3000, () => {
    console.log("Server running on port 3000");
});

app.get("/response", (req, res, next) => {
    res.json("Hello World!");
});

app.get("/database_read", async (req, res, next) => {
    const result = await readData();
    console.log(result);
    res.json(result.rows);
});

app.post("/database_write", async (req, res, next) => {
    const result = await writeData();
    res.json("DB write");
});
