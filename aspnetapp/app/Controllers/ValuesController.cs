using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using Npgsql;
using Dapper;

namespace aspnetapp.Controllers
{
    [ApiController]
    [Route("")]
    public class ValuesController : ControllerBase
    {
        private readonly string connectionString = "Host=db;Username=docker;Password=docker;Database=exampledb";

        // GET: /values/response
        [HttpGet("response")]
        public IEnumerable<string> GetResponse()
        {
            return new string[] { "Hello", "World" };
        }

        // GET: /values/database_read
        [HttpGet("database_read")]
        public IEnumerable<string> GetProductPrice()
        {
            using (var connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                var results = connection.Query<Product>("SELECT * FROM product");

                List<string> resultStrings = new List<string>();
                foreach (var result in results)
                {
                    string resultString = $"{result.product_id}: {result.product_name} - {result.product_price}";
                    resultStrings.Add(resultString);
                }

                return resultStrings;
            }
        }
        [HttpGet("database_read_conn")]
        public IEnumerable<string> GetProductPrice2()
        {
            string connectionString = "Host=db;Username=docker;Password=docker;Database=exampledb";
            List<string> results = new List<string>();
            
            using (var connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                using (var command = new NpgsqlCommand("SELECT * FROM product", connection))
                using (var reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        int id = reader.GetInt32(0);
                        string name = reader.GetString(1);
                        int price = reader.GetInt32(2);
                        string result = $"{id}: {name} - {price}";
                        results.Add(result);
                    }
                }
            }
            
            return results;
        }
        // GET: /values/product/{product_id}
        [HttpGet("product/{product_id}")]
        public IActionResult GetProductById(int product_id)
        {
            using (var connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                var product = connection.QueryFirstOrDefault<Product>("SELECT * FROM product WHERE product_id = @product_id", new { product_id });
                if (product != null)
                {
                    return Ok(product);
                }
                else
                {
                    return NotFound();
                }
            }
        }
        // POST: /values/database_write
        [HttpPost("database_write")]
        public IActionResult AddRecord1()
        {
            using (var connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                var product = new Product { product_name = "Chair", product_price = 203 };
                var result = connection.Execute("INSERT INTO product (product_name, product_price) VALUES (@product_name, @product_price) RETURNING product_id", product);
                if (result > 0)
                {
                    return CreatedAtAction(nameof(GetProductPrice), null);
                }
                else
                {
                    return BadRequest("Failed to insert record into the database.");
                }
            }
        }
        [HttpPost("database_write_conn")]
        public IActionResult AddRecord2()
        {
            string connectionString = "Host=db;Username=docker;Password=docker;Database=exampledb";
            using (var connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                using (var command = new NpgsqlCommand("INSERT INTO product (product_name, product_price) VALUES ('Chair', '203')", connection))
                {
                    command.ExecuteNonQuery();
                    return CreatedAtAction(nameof(GetProductPrice), null);
                }
            }
        }
        [HttpPost("database_write_many")]
        public IActionResult AddRecords3()
        {
            using (var connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                var products = new List<Product>
                {
                    new Product { product_name = "Chair", product_price = 203 },
                    new Product { product_name = "Table", product_price = 450 },
                    new Product { product_name = "Lamp", product_price = 85 },
                    new Product { product_name = "Sofa", product_price = 750 },
                    new Product { product_name = "Desk", product_price = 325 },
                    new Product { product_name = "Bookcase", product_price = 280 },
                    new Product { product_name = "TV Stand", product_price = 195 },
                    new Product { product_name = "Mirror", product_price = 110 },
                    new Product { product_name = "Cabinet", product_price = 375 },
                    new Product { product_name = "Clock", product_price = 70 }
                };

                var values = new List<string>();

                foreach (var product in products)
                {
                    values.Add($"('{product.product_name}', {product.product_price})");
                }

                var valuesString = string.Join(", ", values);

                var result = connection.Execute($"INSERT INTO product (product_name, product_price) VALUES {valuesString}");

                if (result == products.Count)
                {
                    return CreatedAtAction(nameof(GetProductPrice), null);
                }
                else
                {
                    return BadRequest("Failed to insert records into the database.");
                }
            }
        }
        [HttpPost("database_write_many_conn")]
        public IActionResult AddRecords4()
        {
            string connectionString = "Host=db;Username=docker;Password=docker;Database=exampledb";
            using (var connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                using (var command = new NpgsqlCommand("INSERT INTO product (product_name, product_price) VALUES (@product_name, @product_price)", connection))
                {
                    for (int i = 0; i < 10; i++)
                    {
                        command.Parameters.AddWithValue("product_name", "Chair");
                        command.Parameters.AddWithValue("product_price", (203+i));
                        command.ExecuteNonQuery();
                        command.Parameters.Clear();
                    }
                    return CreatedAtAction(nameof(GetProductPrice), null);
                }
            }
        }
    }
    
    public class Product
    {
        public int product_id { get; set; }
        public string product_name { get; set; }
        public int? product_price { get; set; }
    }
}