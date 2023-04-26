package pl.slowikowski.java_spring;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.slowikowski.java_spring.product.Product;
import pl.slowikowski.java_spring.product.ProductDTO;
import pl.slowikowski.java_spring.product.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RestController
public class ApplicationController {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;


    public ApplicationController(final ProductRepository productRepository, final EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @GetMapping("/response")
    public String getResponse() {
        return "Hello world!";
    }

    @GetMapping("/database_read")
    public List<Product> databaseRead() {
        return productRepository.findAll();
    }

    @GetMapping("/database_read_conn")
    public List databaseReadConn() {
        Query query = entityManager.createNativeQuery("select product_id, product_name, product_price from Product");
        return query.getResultList();
    }

    @GetMapping("/product/{productId}")
    public Product getProductById(@PathVariable("productId") String productId) {
        return productRepository.findById(Integer.valueOf(productId)).orElse(null);
    }

    @PostMapping("/database_write")
    @Transactional
    public void databaseWrite() {
        productRepository.save(new Product("Java db write", 4321));
    }

    @PostMapping("/database_write_conn")
    @Transactional
    public void databaseWriteConn() {
        Query query = entityManager.createNativeQuery("insert into product (product_name, product_price) values (:name, :price)");
//        query.setParameter("id", 1234);
        query.setParameter("name", "Java write conn");
        query.setParameter("price", 1234);
        query.executeUpdate();
    }

    @PostMapping("/database_write_many")
    @Transactional
    public void databaseWriteMany() {
        for (int i = 1000; i < 1010; i++) {
            productRepository.save(new Product(i, "Java write many", 1234));
        }
    }

    @PostMapping("/database_write_many_conn")
    @Transactional
    public void databaseWriteManyConn() {
        for (int i = 1010; i < 1020; i++) {
            Query query = entityManager.createNativeQuery("insert into product (product_name, product_price) values (:name, :price)");
            query.setParameter("name", "Java write many conn");
            query.setParameter("price", 1234);
            query.executeUpdate();
        }
    }
}

