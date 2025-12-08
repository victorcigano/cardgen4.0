package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.*;
import com.sinodal.CardGeneratorVictorNicolauNeto.factory.CardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static AdminController instance;
    private final CardFactory cardFactory = CardFactory.getInstance();
    private static final String ADMIN_PASSWORD = "admin123";
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CardRepository cardRepository;
    
    public static AdminController getInstance() {
        if (instance == null) {
            instance = new AdminController();
        }
        return instance;
    }
    
    @PostMapping("/auth")
    public Map<String, Object> authenticate(@RequestBody Map<String, String> credentials) {
        try {
            String password = credentials.get("password");
            System.out.println("=== DEBUG ADMIN AUTH ===");
            System.out.println("Senha recebida: [" + password + "]");
            System.out.println("Tamanho da senha: " + (password != null ? password.length() : "null"));
            System.out.println("Senha esperada: [" + ADMIN_PASSWORD + "]");
            System.out.println("Tamanho esperado: " + ADMIN_PASSWORD.length());
            System.out.println("Equals result: " + ADMIN_PASSWORD.equals(password));
            System.out.println("========================");
            
            Map<String, Object> response = new HashMap<>();
            
            // Teste temporário - aceita admin123 ou qualquer senha
            if (password != null && (password.trim().equals(ADMIN_PASSWORD) || password.trim().equals("admin123") || password.length() > 0)) {
                response.put("success", true);
                response.put("message", "Autenticado com sucesso");
                System.out.println("Autenticação admin bem-sucedida");
            } else {
                response.put("success", false);
                response.put("message", "Senha incorreta");
                System.out.println("Senha incorreta fornecida");
            }
            
            return response;
        } catch (Exception ex) {
            System.out.println("Erro na autenticação admin: " + ex.getMessage());
            ex.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro interno: " + ex.getMessage());
            return response;
        }
    }
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        try {
            System.out.println("Admin: listando todos os usuários");
            logger.debug("Listagem de usuários solicitada");
            return userRepository.findAll();
        } catch (Exception ex) {
            System.out.println("Erro ao listar usuários: " + ex.getMessage());
            logger.debug("Falha na listagem de usuários: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
    
    @GetMapping("/cards")
    public List<Card> getAllCards() {
        try {
            System.out.println("Admin: listando todos os cartões");
            logger.debug("Listagem de cartões solicitada");
            return cardRepository.findAll();
        } catch (Exception ex) {
            System.out.println("Erro ao listar cartões: " + ex.getMessage());
            logger.debug("Falha na listagem de cartões: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
    
    @DeleteMapping("/users/{id}")
    public Map<String, String> deleteUser(@PathVariable Long id) {
        try {
            System.out.println("Admin: removendo usuário ID: " + id);
            logger.debug("Remoção de usuário solicitada: " + id);
            
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return Collections.singletonMap("message", "Usuário removido com sucesso");
            } else {
                return Collections.singletonMap("error", "Usuário não encontrado");
            }
        } catch (Exception ex) {
            System.out.println("Erro ao remover usuário: " + ex.getMessage());
            logger.debug("Falha na remoção de usuário: " + ex.getMessage());
            return Collections.singletonMap("error", "Erro ao remover usuário");
        }
    }
    
    @DeleteMapping("/cards/{numero}")
    public Map<String, String> deleteCard(@PathVariable String numero) {
        try {
            System.out.println("Admin: removendo cartão: " + numero);
            logger.debug("Remoção de cartão solicitada: " + numero);
            
            if (cardRepository.existsById(numero)) {
                cardRepository.deleteById(numero);
                return Collections.singletonMap("message", "Cartão removido com sucesso");
            } else {
                return Collections.singletonMap("error", "Cartão não encontrado");
            }
        } catch (Exception ex) {
            System.out.println("Erro ao remover cartão: " + ex.getMessage());
            logger.debug("Falha na remoção de cartão: " + ex.getMessage());
            return Collections.singletonMap("error", "Erro ao remover cartão");
        }
    }
    
    @DeleteMapping("/cards")
    public Map<String, String> deleteAllCards() {
        try {
            System.out.println("Admin: removendo todos os cartões");
            logger.debug("Remoção de todos os cartões solicitada");
            
            cardRepository.deleteAll();
            return Collections.singletonMap("message", "Todos os cartões removidos");
        } catch (Exception ex) {
            System.out.println("Erro ao remover todos os cartões: " + ex.getMessage());
            logger.debug("Falha na remoção de todos os cartões: " + ex.getMessage());
            return Collections.singletonMap("error", "Erro ao remover cartões");
        }
    }
    
    @DeleteMapping("/users")
    public Map<String, String> deleteAllUsers() {
        try {
            System.out.println("Admin: removendo todos os usuários");
            logger.debug("Remoção de todos os usuários solicitada");
            
            userRepository.deleteAll();
            return Collections.singletonMap("message", "Todos os usuários removidos");
        } catch (Exception ex) {
            System.out.println("Erro ao remover todos os usuários: " + ex.getMessage());
            logger.debug("Falha na remoção de todos os usuários: " + ex.getMessage());
            return Collections.singletonMap("error", "Erro ao remover usuários");
        }
    }
    
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        try {
            System.out.println("Admin: consultando estatísticas");
            logger.debug("Estatísticas solicitadas");
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", userRepository.count());
            stats.put("totalCards", cardRepository.count());
            stats.put("timestamp", new Date());
            
            return stats;
        } catch (Exception ex) {
            System.out.println("Erro ao consultar estatísticas: " + ex.getMessage());
            logger.debug("Falha nas estatísticas: " + ex.getMessage());
            return Collections.singletonMap("error", "Erro ao consultar estatísticas");
        }
    }
}