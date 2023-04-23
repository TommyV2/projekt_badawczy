using Genie.Router, Genie.Responses, Genie.Requests
using SearchLight
import Genie.Renderer.Json: json
import SearchLight: AbstractModel, DbId, save!, findone
import Base: @kwdef

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
  all_products = all(Product)
  all_products |> json
end

route("/product/:product_id") do
  p_id = payload(:product_id)
  product = findone(Product, id = p_id)
  product |> json
end

route("/database_write", method = POST) do
  name = jsonpayload()["name"]
  price = jsonpayload()["price"]
  Product(product_name = name, product_price = price) |> save
  return setstatus(201)
end

route("/database_write_many", method = POST) do
  name, price = ["ABC", 12]
  for i in 1:10
    Product(product_name = name, product_price = price) |> save
  end
  return setstatus(201)
end

route("/database_read_conn") do
  query = "SELECT * FROM product"
  cur = SearchLight.query(query)

  dataframe_to_json(cur) |> json
end

route("/database_write_conn", method = POST) do
  name = jsonpayload()["name"]
  price = jsonpayload()["price"]
  query = "INSERT INTO product (product_name, product_price) VALUES ('$(name)', '$(price)' )"
  SearchLight.query(query)
  return setstatus(201)
end

route("/database_write_many_conn", method = POST) do
  name, price = ["ABC", 12]
  query = "INSERT INTO product (product_name, product_price) VALUES ('$(name)', '$(price)' )"
  for i in 1:9
    query = query * ", ('$(name)', '$(price)' )"
  end

  SearchLight.query(query)
  return setstatus(201)
end



@kwdef mutable struct Product <: AbstractModel
  id::DbId = DbId()
  product_name::String = ""
  product_price::Integer = 0
end