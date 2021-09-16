package fr.b4.apps.clients.repositories;

import fr.b4.apps.clients.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
