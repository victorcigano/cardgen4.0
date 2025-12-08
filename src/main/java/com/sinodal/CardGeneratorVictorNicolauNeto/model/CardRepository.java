package com.sinodal.CardGeneratorVictorNicolauNeto.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    // JpaRepository already provides save, findAll, deleteById, deleteAll
}
