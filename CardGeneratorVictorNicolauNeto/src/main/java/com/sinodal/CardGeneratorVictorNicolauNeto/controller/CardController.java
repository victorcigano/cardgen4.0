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
    public Card gerarCard(@RequestHeader(value = "X-CG-USER", required = false) String headerUser, @RequestParam(required = false) String nomeTitular) {
        String user = (nomeTitular != null && !nomeTitular.isEmpty()) ? nomeTitular : headerUser;
        if (user == null || user.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing registration");
        }
        Card card = generator.gerarCard(user);
        if (CardValidator.validar(card)) {
            repository.save(card);
        }
        return card;
    }

    @GetMapping("/listar")
    public List<Card> listarCards() {
        return repository.findAll();
    }

    @DeleteMapping("/{numero}")
    public void removerCard(@PathVariable String numero, @RequestHeader(value = "X-CG-USER", required = false) String headerUser) {
        if (headerUser == null || headerUser.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing registration");
        }
        if (!repository.existsById(numero)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Card not found");
        }
        repository.deleteById(numero);
    }

    @DeleteMapping
    public void removerTodos(@RequestHeader(value = "X-CG-USER", required = false) String headerUser) {
        if (headerUser == null || headerUser.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Missing registration");
        }
        repository.deleteAll();
    }
}

