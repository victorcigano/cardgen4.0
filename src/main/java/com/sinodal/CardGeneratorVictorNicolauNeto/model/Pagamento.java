package com.sinodal.CardGeneratorVictorNicolauNeto.model;

// Classe abstrata para demonstrar polimorfismo
public abstract class Pagamento {
    protected double valor;
    protected String descricao;
    
    public Pagamento(double valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }
    
    // Método abstrato - polimorfismo
    public abstract boolean processar();
    
    public double getValor() { return valor; }
    public String getDescricao() { return descricao; }
}