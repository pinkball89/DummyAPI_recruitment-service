package vn.unigap;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService{
    private final Map<Integer, Product> products = new HashMap<>();

    public ProductService() {
        // Dummy data initialization
        initializeDummyData();
    }

    // Initialize dummy data
    private void initializeDummyData() {
        // Add some dummy products to the map
        for (int i = 1; i <= 10; i++) {
            Product product = new Product();
            product.setId(i);
            product.setTitle("Product " + i);
            product.setDescription("Description for Product " + i);
            product.setPrice(10 * i);
            product.setDiscountPercentage(10.0);
            product.setRating(4.5);
            product.setStock(10 - i);
            product.setBrand("Brand " + i);
            product.setCategory("Category " + i);
            product.setThumbnail("thumbnail" + i + ".jpg");
            product.setImages(List.of("image1.jpg", "image2.jpg", "image3.jpg"));
            products.put(i, product);
        }
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Product getProductById(int id) {
        return products.get(id);
    }

    public List<String> getAllCategories() {
        return products.values().stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Product> getProductsByCategory(String category) {
        return products.values().stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Product> getPaginatedProducts(int limit, int skip) {
        return products.values().stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Product addProduct(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    public Product updateProduct(int id, Product updatedProduct) {
        if (products.containsKey(id)) {
            products.put(id, updatedProduct);
            return updatedProduct;
        } else {
            throw new IllegalArgumentException("Product with id " + id + " not found");
        }
    }

    public Product deleteProduct(int id) {
        Product deletedProduct = products.remove(id);
        if (deletedProduct == null) {
            throw new IllegalArgumentException("Product with id " + id + " not found");
        }
        return deletedProduct;
    }

}
