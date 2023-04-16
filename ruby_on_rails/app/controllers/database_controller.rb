class DatabaseController < ApplicationController
  skip_before_action :verify_authenticity_token, only: [:write]

  def read
    @products = Product.select(:product_id, :product_name, :product_price)
    render json:@products
  end

  def write
    @product = Product.new(product_params)
    @product.save
  end

  def product_by_id
    @product = Product.find_by(product_id: params[:productId])
    render json:@product
  end

  def product_params
    params.permit(:product_id, :product_name, :product_price)
  end
end
