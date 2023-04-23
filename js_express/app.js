var express = require("express");
const { Pool } = require("pg")
const { Op } = require("sequelize");
const bodyparser = require('body-parser');
// const model = require("./models/index")
const Product = require("./src/models/product");


var app = express();
app.use(bodyparser.json());
app.use(bodyparser.urlencoded({ extended: false }));
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

const writeDataMany = async (times) => {
    const [product_name, price] = ["Generated", 999];
    for(var i = 0; i < times; i++){
        res = await pool.query("INSERT INTO product (product_name, product_price) VALUES ($1, $2)",
            [product_name, price]
        );
    }
    return res;
};

app.listen(3000, () => {
    console.log("Server running on port 3000");
});

app.get("/response", (req, res, next) => {
    res.json("Hello World!");
});

app.get("/database_read", async (req, res) => {
    // const result = await readData();
    // console.log(result);
    // res.json(result.rows);

    var userData = await Product.findAll({});
        if (userData.length > 0) {
            res
            .status(200)
            .json({ data: userData });
        } else {
        res.status(200).json({ message: "Connection failed", data: [] });
        }
});

app.get("/database_read_conn", async (req, res) => {
    const result = await readData();
    console.log(result);
    res.json(result.rows);
});

app.get("/product/:id", async (req, res) => {
    // const result = await readData();
    // console.log(result);
    // res.json(result.rows);
    var id = 5

    var userData = await Product.findAll({where: {[Op.or]: [ {id} ]}});
        if (userData.length > 0) {
            res
            .status(200)
            .json({ data: userData });
        } else {
        res.status(200).json({ message: "Connection failed", data: [] });
        }
});

app.post("/database_write", async (req, res) => {
    // const result = await writeData();
    // res.json("DB write");
    // const name =  req.body.product_name;
    // const price = req.body.product_price;

    await Product.create({
            product_name: "Generated JS",
            product_price: 123
        })
        .then((result) => {
            res.status(201).json({
            message: "user successful created"
            });
        });

});

app.post("/database_write_many", async (req, res) => {
    // const result = await writeData();
    // res.json("DB write");
    // const name =  req.body.product_name;
    // const price = req.body.product_price;
    var times = 10;
    
    for(var i = 0; i < times; i++){
        await Product.create({
            product_name: "Generated JS",
            product_price: 123
        })
    }
    res.json("DB write");
});

app.post("/database_write_conn", async (req, res) => {
    const result = await writeData();
    res.json("DB write");
});

app.post("/database_write_many_conn", async (req, res) => {
    const result = await writeDataMany(10);
    res.json("DB write");
});

