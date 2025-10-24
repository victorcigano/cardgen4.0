package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Card;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final String ADMIN_PASS = "admin123";

    @Autowired
    private CardRepository repository;

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> request,
                                        @RequestHeader(value = "X-CG-USER", required = false) String userHeader) {
        if (userHeader == null || userHeader.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "User authentication required"));
        }
        
        String password = request.get("password");
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Password is required"));
        }
        
        if (ADMIN_PASS.equals(password.trim())) {
            return ResponseEntity.ok(Map.of("message", "Authentication successful"));
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(Map.of("error", "Invalid credentials"));
    }

    private void checkAdmin(String adminHeader, String userHeader) {
        if (userHeader == null || userHeader.trim().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "User registration required");
        }
        if (adminHeader == null || !"authenticated".equals(adminHeader.trim())) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.FORBIDDEN, "Admin authentication required");
        }
    }

    @GetMapping("/cards")
    public List<Card> listar(@RequestHeader(value = "X-CG-ADMIN", required = false) String adminHeader,
                             @RequestHeader(value = "X-CG-USER", required = false) String userHeader) {
        checkAdmin(adminHeader, userHeader);
        return repository.findAll();
    }

    @DeleteMapping("/cards/{numero}")
    public void remover(@PathVariable String numero,
                        @RequestHeader(value = "X-CG-ADMIN", required = false) String adminHeader,
                        @RequestHeader(value = "X-CG-USER", required = false) String userHeader) {
        checkAdmin(adminHeader, userHeader);
        if (!repository.existsById(numero)) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");
        }
        repository.deleteById(numero);
    }

    @DeleteMapping("/cards")
    public void removerTodos(@RequestHeader(value = "X-CG-ADMIN", required = false) String adminHeader,
                             @RequestHeader(value = "X-CG-USER", required = false) String userHeader) {
        checkAdmin(adminHeader, userHeader);
        repository.deleteAll();
    }
}
