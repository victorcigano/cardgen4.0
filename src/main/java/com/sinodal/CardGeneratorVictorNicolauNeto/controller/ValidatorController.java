package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Card;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/validator")
public class ValidatorController {
    
    private static final Logger logger = LoggerFactory.getLogger(ValidatorController.class);
    private static ValidatorController instance;
    
    private final Map<Long, Map<String, Object>> validations = new HashMap<>();
    private Long nextId = 1L;
    
    public static ValidatorController getInstance() {
        if (instance == null) {
            instance = new ValidatorController();
        }
        return instance;
    }
    
    @GetMapping
    public List<Map<String, Object>> listar() {
        try {
            System.out.println("Listando todas as validações");
            logger.debug("Método listar() executado");
            return new ArrayList<>(validations.values());
        } catch (Exception e) {
            System.out.println("Erro ao listar validações: " + e.getMessage());
            logger.debug("Erro no método listar(): " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @PostMapping
    public Map<String, Object> validar(@RequestBody Card card) {
        try {
            boolean isValid = CardValidator.validar(card);
            Map<String, Object> result = new HashMap<>();
            result.put("id", nextId);
            result.put("card", card);
            result.put("valid", isValid);
            result.put("timestamp", new Date());
            
            validations.put(nextId++, result);
            System.out.println("Validação realizada para cartão: " + card.getNumero());
            logger.debug("Validação criada com ID: " + result.get("id"));
            return result;
        } catch (Exception e) {
            System.out.println("Erro ao validar cartão: " + e.getMessage());
            logger.debug("Erro no método validar(): " + e.getMessage());
            return null;
        }
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> buscar(@PathVariable Long id) {
        try {
            System.out.println("Buscando validação ID: " + id);
            logger.debug("Método buscar() executado para ID: " + id);
            return validations.get(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar validação: " + e.getMessage());
            logger.debug("Erro no método buscar(): " + e.getMessage());
            return null;
        }
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> atualizar(@PathVariable Long id, @RequestBody Card card) {
        try {
            boolean isValid = CardValidator.validar(card);
            Map<String, Object> result = new HashMap<>();
            result.put("id", id);
            result.put("card", card);
            result.put("valid", isValid);
            result.put("timestamp", new Date());
            
            validations.put(id, result);
            System.out.println("Validação atualizada: " + id);
            logger.debug("Validação atualizada com ID: " + id);
            return result;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar validação: " + e.getMessage());
            logger.debug("Erro no método atualizar(): " + e.getMessage());
            return null;
        }
    }
    
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        try {
            validations.remove(id);
            System.out.println("Validação deletada: " + id);
            logger.debug("Validação deletada com ID: " + id);
        } catch (Exception e) {
            System.out.println("Erro ao deletar validação: " + e.getMessage());
            logger.debug("Erro no método deletar(): " + e.getMessage());
        }
    }
}