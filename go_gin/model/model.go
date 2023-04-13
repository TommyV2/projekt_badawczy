package model

type Product struct {
	Id int32 `gorm:"column:product_id;primaryKey"`
	Name string `gorm:"column:product_name"`
	Price int32 `gorm:"column:product_price"`
}

func (Product) TableName() string {
    return "product"
}