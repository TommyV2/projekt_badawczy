using Genie.Router, Genie.Responses, Genie.Requests
using SearchLight
import Genie.Renderer.Json: json

function dataframe_to_json(df)
  li = []
  for row in eachrow(df)
      di = Dict()
      for name in names(row)
          di[string(name)] = row[name]
      end
      push!(li, di)
  end
  li
end

route("/response") do
  "Hello world"
end

route("/database_read") do
  query = "SELECT * FROM product"
  cur = SearchLight.query(query)

  dataframe_to_json(cur) |> json
end

route("/product/:product_id") do
  query = "SELECT * FROM product WHERE product_id=$(payload(:product_id))"
  cur = SearchLight.query(query)

  dataframe_to_json(cur) |> json
end

route("/database_write", method = POST) do
  name = jsonpayload()["name"]
  price = jsonpayload()["price"]
  query = "INSERT INTO product (product_name, product_price) VALUES ('$(name)', '$(price)' )"
  SearchLight.query(query)
  return setstatus(201)
end

