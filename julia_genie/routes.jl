using Genie.Router

route("/response") do
  "Hello world"
end

route("/database_read") do
  "Route for database read"
end

route("/database_write") do
  "Route for database write"
end