package vn.unigap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    List<String> list = new ArrayList<>();
    List<Product> products = new ArrayList<>();

    @GetMapping("/dummyapi")
    public String dummyApi() {
        return list.toString();
    }

    @GetMapping("/test/{input}")
    public String test(@PathVariable String input) {
        System.out.print(input);
        list.add(input);
        return "Success " + input;
        //http://localhost:8080/test/use
        //http://localhost:8080/test/add
    }

    @GetMapping("/testParam")
    public String testParam(@RequestParam String inputParam) {
        System.out.print(inputParam);
        return "Done " + inputParam;
        //localhost:8080/testParam?inputParam=testing
    }

    @GetMapping("/testParam2")
    public String testParam(@RequestParam String inputParam, @RequestParam String input2) {
        System.out.print(inputParam + " " + input2);
        return "Done " + inputParam + " " + input2;
        //http://localhost:8080/testParam2?inputParam=test1&input2=test2
    }

    @Autowired
    private ProductService productService;

    //Get All Products
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "30") int limit) {
        List<Product> allProducts = productService.getAllProducts();
        int total = allProducts.size();

        if (limit == 0) {
            limit = total;
        }
        List<Product> paginatedProducts = productService.getPaginatedProducts(limit, skip);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("products", paginatedProducts);
        response.put("total", total);
        response.put("skip", skip);
        response.put("limit", limit);

        return ResponseEntity.ok(response);
    }

    //Get a single product
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping("products/search")
    public List<Product> searchProducts(@RequestParam(name = "q", required = false) String q) {
        if (q == null || q.isEmpty()) {
            return productService.getAllProducts();
        } else {
            return productService.getAllProducts().stream()
                    .filter(product -> product.getTitle().toLowerCase().contains(q.toLowerCase()) ||
                            product.getDescription().toLowerCase().contains(q.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/products/categories")
    public List<String> getAllCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("products/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @PostMapping("/products/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("products/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable int id) {
        Product deletedProduct = productService.deleteProduct(id);

        // Create response with deleted product details
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("products", deletedProduct);
        // Set other fields
        response.put("isDeleted", true);
        response.put("deletedOn", LocalDateTime.now());

        return ResponseEntity.ok(response);

    }
}
