package com.ecommerce.app.config;

import com.ecommerce.app.entity.Product;
import com.ecommerce.app.entity.Role;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.repository.ProductRepository;
import com.ecommerce.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        initializeUsers();
        initializeProducts();

        System.out.println("================================");
        System.out.println("ShopHub Demo Data Loaded");
        System.out.println("Admin: adminDataInit@example.com");
        System.out.println("Password: admin123");
        System.out.println("================================");
    }

    private void initializeUsers() {

        if (userRepository.findByEmail("adminDataInit@example.com").isEmpty()) {

            User admin = new User();

            admin.setUsername("adminDataInit");
            admin.setEmail("adminDataInit@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            userRepository.save(admin);
        }

        if (userRepository.findByEmail("userDataInit@example.com").isEmpty()) {

            User user = new User();

            user.setUsername("userDataInit");
            user.setEmail("userDataInit@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.save(user);
        }
    }

    private void initializeProducts() {

        if (productRepository.count() > 0) {
            return;
        }

        saveProduct(
                "Dell Inspiron Laptop",
                "15.6 inch laptop with Intel Core i5 processor",
                649.99,
                20,
                "Electronics",
                "Dell",
                "https://images.unsplash.com/photo-1496181133206-80ce9b88a853"
        );

        saveProduct(
                "Sony Wireless Headphones",
                "Noise cancelling wireless headphones",
                129.99,
                35,
                "Electronics",
                "Sony",
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e"
        );

        saveProduct(
                "Java Programming Guide",
                "Complete guide to Java programming",
                29.99,
                50,
                "Books",
                "TechBooks",
                "https://images.unsplash.com/photo-1544947950-fa07a98d237f"
        );

        saveProduct(
                "Running Shoes",
                "Comfortable sports running shoes",
                79.99,
                40,
                "Sports",
                "RunPro",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff"
        );

        saveProduct(
                "Coffee Maker",
                "Automatic coffee maker for home use",
                89.99,
                25,
                "Home & Kitchen",
                "BrewMaster",
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085"
        );
    }

    private void saveProduct(
            String name,
            String description,
            double price,
            int stockQuantity,
            String category,
            String brand,
            String imageUrl) {

        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setStockQuantity(stockQuantity);
        product.setCategory(category);
        product.setBrand(brand);
        product.setImageUrl(imageUrl);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
    }
}