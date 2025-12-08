package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    User findByLoginAndSenha(String login, String senha);
}