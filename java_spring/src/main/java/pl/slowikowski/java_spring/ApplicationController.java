package pl.slowikowski.java_spring;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.slowikowski.java_spring.product.Product;
import pl.slowikowski.java_spring.product.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RestController
public class ApplicationController {

    private final ProductRepository productRepository;

    public ApplicationController(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/response")
    public String getResponse() {
        return "Hello world!";
    }

    @GetMapping("/database_read")
    public List<Product> databaseRead() {
        return productRepository.findAll();
    }

    @GetMapping("/product/{productId}")
    public Product getProductById(@PathVariable("productId") String productId) {

        return productRepository.findById(Integer.valueOf(productId)).orElse(null);
    }

    @PostMapping("/database_write")
    @Transactional
    public void databaseWrite(@RequestBody Product body) {
        productRepository.save(new Product(body.getProduct_id(), body.getProduct_name(), body.getProduct_price()));
    }
}

