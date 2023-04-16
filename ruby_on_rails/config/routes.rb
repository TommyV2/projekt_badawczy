Rails.application.routes.draw do
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Defines the root path route ("/")
  get "/response", to: "response#index"
  get "/database_read", to: "database#read"
  get "/product/:productId", to: "database#product_by_id"
  post "/database_write", to: "database#write"
end
