package stu.napls.boostimauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stu.napls.boostimauth.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByContent(String content);
}
