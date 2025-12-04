package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Card;
import com.sinodal.CardGeneratorVictorNicolauNeto.factory.CardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static AdminController instance;
    private final CardFactory cardFactory = CardFactory.getInstance();
    private final Map<Long, Map<String, Object>> adminActions = new HashMap<>();
    private Long nextId = 1L;
    
    public static AdminController getInstance() {
        if (instance == null) {
            instance = new AdminController();
        }
        return instance;
    }
    
    @GetMapping
    public List<Map<String, Object>> listar() {
        try {
            System.out.println("Listando todas as ações admin");
            logger.debug("Método listar() executado");
            return new ArrayList<>(adminActions.values());
        } catch (Exception e) {
            System.out.println("Erro ao listar ações admin: " + e.getMessage());
            logger.debug("Erro no método listar(): " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @PostMapping
    public Map<String, Object> criar(@RequestBody Map<String, Object> action) {
        try {
            action.put("id", nextId);
            action.put("timestamp", new Date());
            adminActions.put(nextId++, action);
            System.out.println("Ação admin criada: " + action.get("type"));
            logger.debug("Ação admin criada com ID: " + action.get("id"));
            return action;
        } catch (Exception e) {
            System.out.println("Erro ao criar ação admin: " + e.getMessage());
            logger.debug("Erro no método criar(): " + e.getMessage());
            return null;
        }
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> buscar(@PathVariable Long id) {
        try {
            System.out.println("Buscando ação admin ID: " + id);
            logger.debug("Método buscar() executado para ID: " + id);
            return adminActions.get(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar ação admin: " + e.getMessage());
            logger.debug("Erro no método buscar(): " + e.getMessage());
            return null;
        }
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> atualizar(@PathVariable Long id, @RequestBody Map<String, Object> action) {
        try {
            action.put("id", id);
            action.put("timestamp", new Date());
            adminActions.put(id, action);
            System.out.println("Ação admin atualizada: " + id);
            logger.debug("Ação admin atualizada com ID: " + id);
            return action;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar ação admin: " + e.getMessage());
            logger.debug("Erro no método atualizar(): " + e.getMessage());
            return null;
        }
    }
    
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        try {
            adminActions.remove(id);
            System.out.println("Ação admin deletada: " + id);
            logger.debug("Ação admin deletada com ID: " + id);
        } catch (Exception e) {
            System.out.println("Erro ao deletar ação admin: " + e.getMessage());
            logger.debug("Erro no método deletar(): " + e.getMessage());
        }
    }
}