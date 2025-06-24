package com.flach.app.financiamento;

import java.util.ArrayList;

public final class Financiamento {

    Double valorTotal;

    Double percentualEntradaMinima;

    Double taxaJurosAnual;

    Double taxaJurosMensal;

    int prazoMeses;

    int duracaoTotal;

    Double valorFinanciado;

    Double entradaReal;

    Double parcelaReal;

    Double fgts;

    Double valorTotalAmortizadoFgts;

    Double valorTotalPago;

    Double valorTotalAmortizado;

    Double valorFinanciadoRestante;

    Double valorTotalPagoParcelas;

    Double valorItbi;

    Double valorDocumentacao;

    int periodoAmortizacaoFgts;

    Double taxasBurocracia;

    Double entradaMinima;

    ArrayList<Parcela> parcelas;

    public Financiamento(Double percentualEntradaMinima, Double valorInicial, int prazoMeses, Double taxaJurosAnual, Double valorTotal, Double fgts, int periodoAmortizacaoFgts, Double taxaItbi, Double taxaDocumentacao) throws Exception {
        this.percentualEntradaMinima = percentualEntradaMinima;
        this.prazoMeses = prazoMeses;
        this.taxaJurosAnual = taxaJurosAnual;
        this.valorTotal = valorTotal;

        this.percentualEntradaMinima = percentualEntradaMinima;
        this.entradaMinima = valorTotal * this.percentualEntradaMinima / 100;
        this.valorItbi = calcularITBI(taxaItbi);
        this.valorDocumentacao = calcularValorDocumentacao(taxaDocumentacao);
        this.taxasBurocracia = this.valorItbi + this.valorDocumentacao;
        this.entradaReal = valorInicial - this.taxasBurocracia;

        if(entradaReal < entradaMinima) {
            throw new Exception("Entrada deve ser maior que a entrada mínima.");
        }

        this.valorFinanciado = valorTotal - entradaReal;
        this.parcelaReal = valorFinanciado / this.prazoMeses;
        
        this.taxaJurosAnual = taxaJurosAnual;
        this.taxaJurosMensal = calcularTaxaJurosMensal(this.taxaJurosAnual);
        
        this.fgts = fgts;
        this.periodoAmortizacaoFgts = periodoAmortizacaoFgts;
        this.valorTotalAmortizadoFgts = 0d;

        this.valorFinanciadoRestante = valorFinanciado;
        this.valorTotalPago = 0d;
        this.valorTotalPagoParcelas = 0d;
        this.valorTotalAmortizado = 0d;
        this.valorTotalAmortizadoFgts = 0d;
    }

    public Double calcularITBI(Double taxaITBI) {
        return this.valorTotal * taxaITBI;
    }

    public Double calcularValorDocumentacao(Double taxaDocumentacao) {
        return this.valorTotal * taxaDocumentacao;
    }

    public static Double calcularTaxaJurosMensal(Double taxaJurosAnual) {
        return Math.pow(1 + taxaJurosAnual, 1/12d) - 1;
    }

    public static Double calcularJuros(Double taxaJuros, Double valor) {
        return valor * taxaJuros;
    } 

    public void simular() {
        this.parcelas = new ArrayList<>();

        Double parcelaTotal;
        Double amortizacaoFgts;
        Double amortizacao;
        int mes = 1;
        while (this.valorFinanciadoRestante > 0) {

            if (mes % this.periodoAmortizacaoFgts == 0) {
                Double valorAmortizacao = fgts * this.periodoAmortizacaoFgts;
                amortizacaoFgts = amortizarFGTS(valorAmortizacao);
            }
            else {
                amortizacaoFgts = 0d;
            }

            amortizacao = amortizar(calcularAmortizacao());

            parcelaTotal = pagarParcela(mes);


            this.parcelas.add(new Parcela(
                mes, 
                this.parcelaReal, 
                calcularJuros(this.taxaJurosMensal, this.valorFinanciadoRestante), 
                parcelaTotal, 
                amortizacao, 
                amortizacaoFgts,
                parcelaTotal + amortizacao,
                this.valorFinanciadoRestante));

            mes++;
        }

        this.duracaoTotal = mes;
        printFinanciamento();
    }

    public Double pagarParcela(int mes) {
        Double valorParcela;
        Double juros;

        juros = calcularJuros(this.taxaJurosMensal, this.valorFinanciadoRestante);
        valorParcela = this.parcelaReal + juros;
        this.valorFinanciadoRestante -= this.parcelaReal;
        this.valorTotalPago += valorParcela;
        this.valorTotalPagoParcelas += valorParcela;

        System.out.println("Mês " + mes + ". Parcela: " + valorParcela);
        return valorParcela;
    }

    public Double calcularValorFuturo(Double valorPresente, Double valorizacaoAnual, int meses) {
        int anos = meses / 12;
        Double valorFuturo = valorPresente;

        for (int i = 0; i < anos; i++) {
            valorFuturo += valorFuturo * (valorizacaoAnual / 100);
        }

        return valorFuturo;
    }

    public Double amortizarFGTS(Double valorAmortizacao) {
        this.valorFinanciadoRestante -= valorAmortizacao;
        this.valorTotalPago += valorAmortizacao;
        this.valorTotalAmortizadoFgts += valorAmortizacao;
        System.out.println("Amortizando " + valorAmortizacao + ". Valor total amortizado FGTS: " + this.valorTotalAmortizadoFgts);

        return valorAmortizacao;
    }

    public Double amortizar(Double valorAmortizacao) {
        this.valorFinanciadoRestante -= valorAmortizacao;
        this.valorTotalPago += valorAmortizacao;
        this.valorTotalAmortizado += valorAmortizacao;

        return valorAmortizacao;
    }

    public Double calcularAmortizacao() {
        Double valorParcela;
        Double juros;

        juros = calcularJuros(this.taxaJurosMensal, this.valorFinanciadoRestante);
        valorParcela = this.parcelaReal + juros;
        return 3000d - valorParcela;
    }

    public String mesParaAno(int mes) {
        int ano;
        int mesesRestantes;

        ano = mes / 12;
        mesesRestantes = mes % 12;

        return "" + ano + " anos e " + mesesRestantes + " meses";
    }

    public void printParcelas(ArrayList<Parcela> parcelas) {
        for (Parcela parcela : parcelas) {
            System.out.println(parcela);
        }
    }

    public void printFinanciamento() {
        System.out.println("Valor financiado: " + this.valorFinanciado + ".");
        System.out.println("Financiamento quitado em " + this.duracaoTotal + " meses ou " + mesParaAno(this.duracaoTotal));
        System.out.println("Valor total pago (sem entrada): " + this.valorTotalPago + ".");
        System.out.println("Valor pago normal: " + this.valorTotalPagoParcelas + ".");
        System.out.println("Valor pago amortização fgts: " + this.valorTotalAmortizadoFgts + ".");
        System.out.println("Valor pago amortização: " + this.valorTotalAmortizado + ".");
        System.out.println("Diferença total paga: " + (this.valorTotalPago - this.valorFinanciado) + ".");
        System.out.println("Porcentagem a mais paga: " + (((this.valorTotalPago - this.valorFinanciado) / this.valorFinanciado) * 100) + ".");
        System.out.println("Valor do imóvel atual: " + this.valorTotal + ".");
        System.out.println("Valor total pago: " + (this.valorTotalPago + this.entradaReal) + ".");
        System.out.println("Valor do imóvel futuro: " + calcularValorFuturo(this.valorTotal, 5d, this.duracaoTotal) + ".");
        printParcelas(this.parcelas);
    }
}