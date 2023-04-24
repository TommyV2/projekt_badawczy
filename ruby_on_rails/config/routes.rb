Rails.application.routes.draw do
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Defines the root path route ("/")
  get "/response", to: "response#index"
  get "/product/:productId", to: "database#product_by_id"
  get "/database_read", to: "database#read"
  get "/database_read_conn", to: "database#read_conn"
  post "/database_write", to: "database#write"
  post "/database_write_conn", to: "database#write_conn"
  post "/database_write_many", to: "database#write_many"
  post "/database_write_many_conn", to: "database#write_many_conn"
end
