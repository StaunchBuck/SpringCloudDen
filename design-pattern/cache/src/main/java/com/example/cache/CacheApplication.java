package com.example.cache;

import io.github.resilience4j.cache.Cache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class CacheApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }

//    JCache provides an in-memory Map-like cache.
//      Resilience4j wraps it inside its Cache component.
//      decorateSupplier ensures:
//    If cache has the key → return cached value
//      Else → call function + store in cache
    @Override
    public void run(String... args) throws Exception {
        // Create a standard JCache (JSR-107) CacheManager
        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();

        // Create cache configuration
        MutableConfiguration<String, String> config = new MutableConfiguration<>();
        config.setStoreByValue(false); // store objects by reference

        // Create an in-memory cache instance
        javax.cache.Cache<String, String> jsr107Cache =
                cacheManager.createCache("myCache", config);

        // Wrap it with Resilience4j Cache
        Cache<String, String> cache = Cache.of(jsr107Cache);

        // Original supplier
        Supplier<String> originalSupplier = () -> fetchDataFromRemote();

        // Decorate supplier with caching
        Function<String,String> cachedSupplier =
                Cache.decorateSupplier(cache, originalSupplier);

        // Run multiple times
        System.out.println("First call  = " + cachedSupplier.apply(""));
        System.out.println("Second call = " + cachedSupplier.apply(""));
        System.out.println("Third call  = " + cachedSupplier.apply(""));
    }

    static String fetchDataFromRemote() {
        System.out.println("⏳ Calling remote service...");
        return "Response at " + LocalTime.now();
    }
}
