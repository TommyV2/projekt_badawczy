class CreateProducts < ActiveRecord::Migration[7.0]
  def change
    create_table :product do |t|
      t.integer :product_id
      t.string :product_name
      t.integer :product_price

      t.timestamps
    end
  end
end
