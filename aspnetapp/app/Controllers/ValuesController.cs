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
        public IActionResult AddRecord()
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
    }

    public class Product
    {
        public int product_id { get; set; }
        public string product_name { get; set; }
        public int? product_price { get; set; }
    }
}