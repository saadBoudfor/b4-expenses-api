package fr.b4.apps.common.repositories;

import fr.b4.apps.common.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
