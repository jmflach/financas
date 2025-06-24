package com.flach.app.financiamento;

public record Parcela (int mes, Double parcelaReal, Double juros, Double parcelaTotal, Double amortizacao, Double amortizacaoFGTS, Double valorTotalMes, Double valorRestante) {

    private static final String SEP = "\t";

    @Override
    public String toString() {
        return "" + mes + SEP + parcelaReal + SEP + juros + SEP + parcelaTotal + SEP + amortizacao + SEP + amortizacaoFGTS + SEP + valorTotalMes + SEP + valorRestante;
    }
    
}