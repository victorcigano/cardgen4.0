package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.*;
import com.sinodal.CardGeneratorVictorNicolauNeto.factory.CardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static UserController instance;
    private final CardFactory cardFactory = CardFactory.getInstance();
    
    @Autowired
    private UserRepository userRepository;
    
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
            return userRepository.findAll();
        } catch (Exception e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
            logger.debug("Erro no método listar(): " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @PostMapping("/cadastrar")
    public User cadastrar(@RequestBody User user) {
        try {
            System.out.println("=== CADASTRO DEBUG ===");
            System.out.println("Dados recebidos:");
            System.out.println("Nome: " + user.getNome());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Login: " + user.getLogin());
            System.out.println("Senha: " + user.getSenha());
            
            // Verifica se login já existe
            User existingUser = userRepository.findByLogin(user.getLogin());
            if (existingUser != null) {
                System.out.println("Login já existe: " + user.getLogin());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login já existe");
            }
            
            // Define data de criação se não estiver definida
            if (user.getDataCriacao() == null) {
                user.setDataCriacao(LocalDateTime.now());
            }
            
            System.out.println("Salvando usuário no banco...");
            User savedUser = userRepository.save(user);
            System.out.println("Usuário salvo com ID: " + savedUser.getId());
            System.out.println("=====================");
            
            logger.debug("Cadastro realizado para: " + user.getLogin());
            return savedUser;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.println("Erro inesperado no cadastro: " + ex.getMessage());
            ex.printStackTrace();
            logger.debug("Erro durante cadastro: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno: " + ex.getMessage());
        }
    }
    
    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Tentativa de login: " + loginRequest.getLogin());
            logger.debug("Login solicitado para: " + loginRequest.getLogin());
            
            if (loginRequest.getLogin() == null || loginRequest.getSenha() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login e senha são obrigatórios");
            }
            
            User user = userRepository.findByLoginAndSenha(loginRequest.getLogin().trim(), loginRequest.getSenha().trim());
            
            if (user == null) {
                System.out.println("Login falhado para: " + loginRequest.getLogin());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos");
            }
            
            // Atualiza último login
            user.setUltimoLogin(LocalDateTime.now());
            userRepository.save(user);
            
            System.out.println("Login bem-sucedido: " + user.getNome());
            logger.debug("Login realizado com sucesso para: " + user.getLogin());
            
            return user;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Erro no login: " + e.getMessage());
            logger.debug("Erro no método login(): " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }
    
    @GetMapping("/{id}")
    public User buscar(@PathVariable Long id) {
        try {
            System.out.println("Buscando usuário ID: " + id);
            logger.debug("Método buscar() executado para ID: " + id);
            return userRepository.findById(id).orElse(null);
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
            System.out.println("Usuário atualizado: " + id);
            logger.debug("Usuário atualizado com ID: " + id);
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            logger.debug("Erro no método atualizar(): " + e.getMessage());
            throw e;
        }
    }
    
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            System.out.println("Usuário deletado: " + id);
            logger.debug("Usuário deletado com ID: " + id);
        } catch (Exception e) {
            System.out.println("Erro ao deletar usuário: " + e.getMessage());
            logger.debug("Erro no método deletar(): " + e.getMessage());
        }
    }
}