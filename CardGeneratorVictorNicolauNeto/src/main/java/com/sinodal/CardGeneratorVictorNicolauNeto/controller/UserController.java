package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.User;
import com.sinodal.CardGeneratorVictorNicolauNeto.factory.CardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static UserController instance;
    private final CardFactory cardFactory = CardFactory.getInstance();
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;
    
    // Singleton
    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }
    
    @GetMapping
    public List<User> listar() {
        try {
            System.out.println("Listando todos os usuários");
            logger.debug("Método listar() executado");
            return new ArrayList<>(users.values());
        } catch (Exception e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
            logger.debug("Erro no método listar(): " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @PostMapping
    public User criar(@RequestBody User user) {
        try {
            user.setId(nextId++);
            users.put(user.getId(), user);
            System.out.println("Usuário criado: " + user.getNome());
            logger.debug("Usuário criado com ID: " + user.getId());
            return user;
        } catch (Exception e) {
            System.out.println("Erro ao criar usuário: " + e.getMessage());
            logger.debug("Erro no método criar(): " + e.getMessage());
            return null;
        }
    }
    
    @GetMapping("/{id}")
    public User buscar(@PathVariable Long id) {
        try {
            System.out.println("Buscando usuário ID: " + id);
            logger.debug("Método buscar() executado para ID: " + id);
            return users.get(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
            logger.debug("Erro no método buscar(): " + e.getMessage());
            return null;
        }
    }
    
    @PutMapping("/{id}")
    public User atualizar(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            users.put(id, user);
            System.out.println("Usuário atualizado: " + id);
            logger.debug("Usuário atualizado com ID: " + id);
            return user;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            logger.debug("Erro no método atualizar(): " + e.getMessage());
            return null;
        }
    }
    
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        try {
            users.remove(id);
            System.out.println("Usuário deletado: " + id);
            logger.debug("Usuário deletado com ID: " + id);
        } catch (Exception e) {
            System.out.println("Erro ao deletar usuário: " + e.getMessage());
            logger.debug("Erro no método deletar(): " + e.getMessage());
        }
    }
}