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

    private static final String ADMIN_PASS = "123";

    @Autowired
    private CardRepository repository;

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> request,
                                        @RequestHeader(value = "X-CG-USER", required = false) String userHeader) {
        if (userHeader == null || userHeader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String password = request.get("password");
        if (ADMIN_PASS.equals(password)) {
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private void checkAdmin(String adminHeader, String userHeader) {
        if (userHeader == null || userHeader.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing registration");
        }
        if (adminHeader == null || !"authenticated".equals(adminHeader)) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN, "Admin authentication required");
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
