package fr.b4.apps.common.repositories;

import fr.b4.apps.common.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByNameContains(String name);
    public boolean existsByName(String name);
    public Product findFirstByName(String name);
}
