package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.model.Banco;
import com.sinodal.CardGeneratorVictorNicolauNeto.factory.CardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/bancos")
public class BancoController {
    
    private static final Logger logger = LoggerFactory.getLogger(BancoController.class);
    private static BancoController instance;
    private final CardFactory cardFactory = CardFactory.getInstance();
    private final Map<Long, Banco> bancos = new HashMap<>();
    private Long nextId = 1L;
    
    public static BancoController getInstance() {
        if (instance == null) {
            instance = new BancoController();
        }
        return instance;
    }
    
    @GetMapping
    public List<Banco> listar() {
        try {
            System.out.println("Listando todos os bancos");
            logger.debug("Método listar() executado");
            return new ArrayList<>(bancos.values());
        } catch (Exception e) {
            System.out.println("Erro ao listar bancos: " + e.getMessage());
            logger.debug("Erro no método listar(): " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @PostMapping
    public Banco criar(@RequestBody Banco banco) {
        try {
            banco.setId(nextId++);
            bancos.put(banco.getId(), banco);
            System.out.println("Banco criado: " + banco.getNome());
            logger.debug("Banco criado com ID: " + banco.getId());
            return banco;
        } catch (Exception e) {
            System.out.println("Erro ao criar banco: " + e.getMessage());
            logger.debug("Erro no método criar(): " + e.getMessage());
            return null;
        }
    }
    
    @GetMapping("/{id}")
    public Banco buscar(@PathVariable Long id) {
        try {
            System.out.println("Buscando banco ID: " + id);
            logger.debug("Método buscar() executado para ID: " + id);
            return bancos.get(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar banco: " + e.getMessage());
            logger.debug("Erro no método buscar(): " + e.getMessage());
            return null;
        }
    }
    
    @PutMapping("/{id}")
    public Banco atualizar(@PathVariable Long id, @RequestBody Banco banco) {
        try {
            banco.setId(id);
            bancos.put(id, banco);
            System.out.println("Banco atualizado: " + id);
            logger.debug("Banco atualizado com ID: " + id);
            return banco;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar banco: " + e.getMessage());
            logger.debug("Erro no método atualizar(): " + e.getMessage());
            return null;
        }
    }
    
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        try {
            bancos.remove(id);
            System.out.println("Banco deletado: " + id);
            logger.debug("Banco deletado com ID: " + id);
        } catch (Exception e) {
            System.out.println("Erro ao deletar banco: " + e.getMessage());
            logger.debug("Erro no método deletar(): " + e.getMessage());
        }
    }
}