package pl.slowikowski.java_spring.product;

public class ProductDTO {

    private String product_name;
    private int product_price;

    public ProductDTO() {
    }

    public ProductDTO(final String product_name, final int product_price) {
        this.product_name = product_name;
        this.product_price = product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(final String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(final int product_price) {
        this.product_price = product_price;
    }
}
