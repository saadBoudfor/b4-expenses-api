package fr.b4.apps.common.repositories;

import fr.b4.apps.common.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

 public interface ProductRepository extends JpaRepository<Product, Long> {
     /**
      * Filter products by name
      *
      * @param name searched product name
      * @return products with  given name
      */
    List<Product> findByNameContains(String name);
    Product findFirstByName(String name);
}
