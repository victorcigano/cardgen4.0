package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.*;
import com.sinodal.CardGeneratorVictorNicolauNeto.factory.CardFactory;
import com.sinodal.CardGeneratorVictorNicolauNeto.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/cartoes")
public class CardController {
    
    private static final Logger logger = LoggerFactory.getLogger(CardController.class);
    private static CardController instance;
    private final CardFactory cardFactory = CardFactory.getInstance();

    @Autowired
    private CardGenerator generator;

    @Autowired
    private CardRepository repository;
    
    @Autowired
    private CardService cardService;
    
    public static CardController getInstance() {
        if (instance == null) {
            instance = new CardController();
        }
        return instance;
    }

    @PostMapping("/gerar")
    public Card gerarCartao(@RequestHeader(value = "X-CG-USER", required = false) String headerUser, 
                           @RequestParam(required = false) String nomeTitular,
                           @RequestParam(required = false, defaultValue = "Visa") String bandeira) {
        try {
            System.out.println("Iniciando geração de cartão");
            logger.debug("Método gerarCartao() executado");
            
            // Verifica qual nome usar
            String usuario = nomeTitular != null ? nomeTitular : headerUser;
            
            if (usuario == null || usuario.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do titular é obrigatório");
            }
            
            // Valida bandeira
            if (!isValidBandeira(bandeira)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bandeira inválida. Use: Visa, MasterCard, American Express, Elo, Hipercard");
            }
            
            // Gera o cartão usando Factory e Interface
            Card novoCartao = cardService.gerarCard(usuario.trim(), bandeira);
            
            System.out.println("Cartão " + bandeira + " gerado para: " + usuario);
            
            // Valida usando a interface
            if (!cardService.validarCard(novoCartao)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cartão inválido gerado");
            }
            

            logger.debug("Cartão criado com sucesso para: " + usuario);
            
            // Salva no banco
            return repository.save(novoCartao);
        } catch (Exception e) {
            System.out.println("Erro ao gerar cartão: " + e.getMessage());
            logger.debug("Erro no método gerarCartao(): " + e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public List<Card> buscarTodos() {
        try {
            System.out.println("Listando todos os cartões");
            logger.debug("Método buscarTodos() executado");
            return repository.findAll();
        } catch (Exception e) {
            System.out.println("Erro ao listar cartões: " + e.getMessage());
            logger.debug("Erro no método buscarTodos(): " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{numero}")
    public String deletarCartao(@PathVariable String numero) {
        try {
            System.out.println("Removendo cartão: " + numero);
            logger.debug("Método deletarCartao() executado para: " + numero);
            repository.deleteById(numero);
            return "Cartão removido com sucesso";
        } catch (Exception e) {
            System.out.println("Erro ao remover cartão: " + e.getMessage());
            logger.debug("Erro no método deletarCartao(): " + e.getMessage());
            return "Erro ao remover cartão: " + e.getMessage();
        }
    }

    @DeleteMapping("/limpar")
    public String limparTodos() {
        try {
            System.out.println("Limpando todos os cartões");
            logger.debug("Método limparTodos() executado");
            repository.deleteAll();
            return "Todos os cartões foram removidos";
        } catch (Exception e) {
            System.out.println("Erro ao limpar cartões: " + e.getMessage());
            logger.debug("Erro no método limparTodos(): " + e.getMessage());
            return "Erro ao limpar cartões: " + e.getMessage();
        }
    }
    
    @PutMapping("/{numero}")
    public Card atualizarCartao(@PathVariable String numero, @RequestBody Card card) {
        try {
            System.out.println("Atualizando cartão: " + numero);
            logger.debug("Método atualizarCartao() executado");
            card.setNumero(numero);
            return repository.save(card);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar cartão: " + e.getMessage());
            logger.debug("Erro no método atualizarCartao(): " + e.getMessage());
            throw e;
        }
    }
    
    private boolean isValidBandeira(String bandeira) {
        return bandeira != null && 
               (bandeira.equals("Visa") || 
                bandeira.equals("MasterCard") || 
                bandeira.equals("American Express") || 
                bandeira.equals("Elo") || 
                bandeira.equals("Hipercard"));
    }
    
    @GetMapping("/bandeiras")
    public List<String> listarBandeiras() {
        try {
            System.out.println("Listando bandeiras disponíveis");
            logger.debug("Método listarBandeiras() executado");
            return List.of("Visa", "MasterCard", "American Express", "Elo", "Hipercard");
        } catch (Exception e) {
            System.out.println("Erro ao listar bandeiras: " + e.getMessage());
            logger.debug("Erro no método listarBandeiras(): " + e.getMessage());
            return List.of();
        }
    }
}

