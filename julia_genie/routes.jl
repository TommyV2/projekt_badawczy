using Genie.Router, Genie.Responses
using SearchLight

route("/response") do
  "Hello world"
end

route("/database_read") do
  query = "SELECT * FROM product"
  cur = SearchLight.query(query)
  return cur
end

route("/database_write", method = POST) do
  query = "INSERT INTO product (product_name, product_price) VALUES ('Chair', '203' )"
  SearchLight.query(query)
  return setstatus(201)
end

