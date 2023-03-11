package pl.slowikowski.java_spring;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RestController
public class ApplicationController {

    private final EntityManager entityManager;

    public ApplicationController(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GetMapping("/response")
    public String getResponse() {
        return "Hello world!";
    }

    @GetMapping("/database_read")
    public List<Object> databaseRead() {
        Query query = entityManager.createNativeQuery("select product_id, product_name, product_price from Product");
        return query.getResultList();
    }

    @PostMapping("/database_write")
    @Transactional
    public void databaseWrite(@RequestBody Product body) {
        Query query = entityManager.createNativeQuery(
                "insert into Product (product_id, product_name, product_price) values (" +
                        body.product_id + ", '" + body.product_name + "', " + body.product_price + ") "
        );
        query.executeUpdate();
    }

    static class Product {
        private int product_id;
        private String product_name;
        private int product_price;

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(final int product_id) {
            this.product_id = product_id;
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
}

