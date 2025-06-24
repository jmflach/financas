package com.flach.app;

import com.flach.app.financiamento.Financiamento;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        Financiamento financiamento = new Financiamento(
            20d, 
            100000d, 
            360, 
            0.09d, 
            300000d, 
            1088d, 
            12, 
            0.03, 
            0.01);
        financiamento.simular();
        System.out.println(financiamento);
    }
}
