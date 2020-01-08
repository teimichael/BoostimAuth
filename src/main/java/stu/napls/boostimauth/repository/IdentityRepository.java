package stu.napls.boostimauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stu.napls.boostimauth.model.Identity;

public interface IdentityRepository extends JpaRepository<Identity, Long> {
    Identity findByUsername(String username);

    Identity findByUuid(String uuid);
}
