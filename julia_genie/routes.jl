using Genie.Router, Genie.Responses
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

route("/database_write", method = POST) do
  query = "INSERT INTO product (product_name, product_price) VALUES ('Chair', '203' )"
  SearchLight.query(query)
  return setstatus(201)
end

