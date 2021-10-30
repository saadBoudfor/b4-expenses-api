package fr.b4.apps.common.repositories;

import fr.b4.apps.common.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
