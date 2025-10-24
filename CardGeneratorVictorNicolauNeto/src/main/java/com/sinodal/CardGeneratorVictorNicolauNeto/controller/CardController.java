package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Card;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardGenerator;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardRepository;
import com.sinodal.CardGeneratorVictorNicolauNeto.model.CardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardGenerator generator;

    @Autowired
    private com.sinodal.CardGeneratorVictorNicolauNeto.model.CardRepository repository;

    @PostMapping("/gerar")
    public Card gerarCard(@RequestHeader(value = "X-CG-USER", required = false) String headerUser, 
                         @RequestParam(required = false) String nomeTitular) {
        String user = (nomeTitular != null && !nomeTitular.trim().isEmpty()) ? 
                     nomeTitular.trim() : 
                     (headerUser != null ? headerUser.trim() : null);
        
        if (user == null || user.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED, "User registration required");
        }
        
        if (user.length() > 100) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, "Name too long (max 100 characters)");
        }
        
        Card card = generator.gerarCard(user);
        if (CardValidator.validar(card)) {
            repository.save(card);
        } else {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate valid card");
        }
        return card;
    }

    @GetMapping("/listar")
    public List<Card> listarCards() {
        return repository.findAll();
    }

    @DeleteMapping("/{numero}")
    public void removerCard(@PathVariable String numero, 
                           @RequestHeader(value = "X-CG-USER", required = false) String headerUser) {
        if (headerUser == null || headerUser.trim().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED, "User registration required");
        }
        
        if (numero == null || numero.trim().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, "Card number is required");
        }
        
        if (!repository.existsById(numero.trim())) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Card not found");
        }
        repository.deleteById(numero.trim());
    }

    @DeleteMapping
    public void removerTodos(@RequestHeader(value = "X-CG-USER", required = false) String headerUser) {
        if (headerUser == null || headerUser.trim().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED, "User registration required");
        }
        repository.deleteAll();
    }
}

