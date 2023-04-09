using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using Npgsql;

namespace aspnetapp.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class ValuesController : ControllerBase
    {
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
            string connectionString = "Host=db;Username=docker;Password=docker;Database=exampledb";
            using (var connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                using (var command = new NpgsqlCommand("SELECT MAX(product_price) FROM product", connection))
                {
                    var result = command.ExecuteScalar().ToString();
                    return new string[] { result };
                }
            }
        }

        // POST: /values/database_write
        [HttpPost("database_write")]
        public IActionResult AddRecord()
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
    }
}