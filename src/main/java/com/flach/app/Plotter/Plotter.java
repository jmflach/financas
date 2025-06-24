package com.flach.app.Plotter;

import java.util.List;
import java.util.stream.Collectors;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;


public class Plotter {

    public Plotter() {
        List<Double> x = NumpyUtils.linspace(-Math.PI, Math.PI, 256);
        List<Double> C = x.stream().map(xi -> Math.cos(xi)).collect(Collectors.toList());
        List<Double> S = x.stream().map(xi -> Math.sin(xi)).collect(Collectors.toList());

        Plot plt = Plot.create();
        plt.plot().add(x, C);
        plt.plot().add(x, S);
        try{
            plt.show();
        }
        catch (Exception e) {System.out.println(e);}

    }
}