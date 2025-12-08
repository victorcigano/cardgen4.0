package com.sinodal.CardGeneratorVictorNicolauNeto.controller;

import com.sinodal.CardGeneratorVictorNicolauNeto.factory.CardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private static ReportController instance;
    private final CardFactory cardFactory = CardFactory.getInstance();
    private final Map<Long, Map<String, Object>> reports = new HashMap<>();
    private Long nextId = 1L;
    
    public static ReportController getInstance() {
        if (instance == null) {
            instance = new ReportController();
        }
        return instance;
    }
    
    @GetMapping
    public List<Map<String, Object>> listar() {
        try {
            System.out.println("Listando todos os relatórios");
            logger.debug("Método listar() executado");
            return new ArrayList<>(reports.values());
        } catch (Exception e) {
            System.out.println("Erro ao listar relatórios: " + e.getMessage());
            logger.debug("Erro no método listar(): " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @PostMapping
    public Map<String, Object> criar(@RequestBody Map<String, Object> report) {
        try {
            report.put("id", nextId);
            report.put("timestamp", new Date());
            reports.put(nextId++, report);
            System.out.println("Relatório criado: " + report.get("title"));
            logger.debug("Relatório criado com ID: " + report.get("id"));
            return report;
        } catch (Exception e) {
            System.out.println("Erro ao criar relatório: " + e.getMessage());
            logger.debug("Erro no método criar(): " + e.getMessage());
            return null;
        }
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> buscar(@PathVariable Long id) {
        try {
            System.out.println("Buscando relatório ID: " + id);
            logger.debug("Método buscar() executado para ID: " + id);
            return reports.get(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar relatório: " + e.getMessage());
            logger.debug("Erro no método buscar(): " + e.getMessage());
            return null;
        }
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> atualizar(@PathVariable Long id, @RequestBody Map<String, Object> report) {
        try {
            report.put("id", id);
            report.put("timestamp", new Date());
            reports.put(id, report);
            System.out.println("Relatório atualizado: " + id);
            logger.debug("Relatório atualizado com ID: " + id);
            return report;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar relatório: " + e.getMessage());
            logger.debug("Erro no método atualizar(): " + e.getMessage());
            return null;
        }
    }
    
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        try {
            reports.remove(id);
            System.out.println("Relatório deletado: " + id);
            logger.debug("Relatório deletado com ID: " + id);
        } catch (Exception e) {
            System.out.println("Erro ao deletar relatório: " + e.getMessage());
            logger.debug("Erro no método deletar(): " + e.getMessage());
        }
    }
}