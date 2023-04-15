Rails.application.routes.draw do
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Defines the root path route ("/")
  # root "articles#index"
  get "/response", to: "response#index"
  get "/database_read", to: "database#read"
  post "/database_write", to: "database#write"
end
