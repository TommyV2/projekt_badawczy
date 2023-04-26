class DatabaseController < ApplicationController
  skip_before_action :verify_authenticity_token, only: [:write, :write_conn, :write_many, :write_many_conn]

  def read
    @products = Product.select(:product_id, :product_name, :product_price)
    render json: @products
  end

  def read_conn
    sql = "select * from product"
    result = ActiveRecord::Base.connection.execute(sql)
    render json: result
  end

  def write
    @product = Product.new
    @product.product_name = "Ruby on rails write"
    @product.product_price = 1234
    @product.save
  end

  def write_conn
    sql = "insert into product (product_name, product_price) values ('ROR write conn', 1234)"
    ActiveRecord::Base.connection.execute(sql)
  end

  def write_many
    for i in 1..10 do
      @product = Product.new
      @product.product_name = "Ruby on rails write many"
      @product.product_price = 1234
      @product.save
    end
  end

  def write_many_conn
    for i in 1..10 do
      sql = "insert into product (product_name, product_price) values ('ROR write conn', 1234)"
      ActiveRecord::Base.connection.execute(sql)
    end
  end

  def product_by_id
    @product = Product.find_by(product_id: params[:productId])
    render json: @product
  end
end
