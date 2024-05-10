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

        List<Product> paginatedProducts = allProducts.stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("products", paginatedProducts);
        response.put("total", total);
        response.put("skip", skip);
        response.put("limit", limit);

        // Create a new map with keys in the desired order
        Map<String, Object> orderedResponse = new LinkedHashMap<>();
        orderedResponse.put("products", response.get("products"));
        orderedResponse.put("total", response.get("total"));
        orderedResponse.put("skip", response.get("skip"));
        orderedResponse.put("limit", response.get("limit"));

        return ResponseEntity.ok(orderedResponse);
    }

    //Get a single product
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam(name = "keyword", required = false) String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return productService.getAllProducts();
        } else {
            return productService.getAllProducts().stream()
                    .filter(product -> product.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("/categories/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/paginate")
    public List<Product> getPaginatedProducts(@RequestParam int limit, @RequestParam int skip) {
        return productService.getPaginatedProducts(limit, skip);
    }

    @PostMapping("/products/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("products/update/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("products/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable int id) {
        Product deletedProduct = productService.deleteProduct(id);
//        if (deletedProduct != null) {
            // Set deletion flag and timestamp
            deletedProduct.setDeleted(true);
            deletedProduct.setDeletedOn(LocalDateTime.now());

            // Create response with deleted product details
            Map<String, Object> response = new HashMap<>();
            response.put("products", deletedProduct);
            // Set other fields
//            response.put("isDeleted", true);
//            response.put("deletedOn", deletedProduct.getDeletedOn());

            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }
}
