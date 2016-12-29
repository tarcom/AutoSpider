package com.skov.autoSpider;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by aogj on 29-12-2016.
 */
public class AutoAnalyzer {

    public static void main(String[] args) {
        doAnalyze();
    }

    public static void doAnalyze() {

        TreeMap<Double, AutoWrapper> analyzedAutos = new TreeMap<Double, AutoWrapper>();
        ArrayList<AutoWrapper> autos = DBHandler.selectAllAutos();
        for (AutoWrapper auto : autos) {
            analyzedAutos.put(Util.getScoreProcentage(auto), auto);
        }

        for (AutoWrapper auto : analyzedAutos.values()) {
            System.out.println("score: " + Util.getScoreProcentage(auto) + " - " + auto.toString());
        }

        AutoWrapper bestAuto = analyzedAutos.lastEntry().getValue();
        System.out.println("best auto:  score: " + Util.getScoreProcentage(bestAuto) + " - " + bestAuto);
        Util.doPrintCalculationDetails(bestAuto);


        AutoWrapper worstAuto = analyzedAutos.firstEntry().getValue();
        System.out.println("worst auto: score: " + Util.getScoreProcentage(worstAuto) + " - " + worstAuto);
        Util.doPrintCalculationDetails(worstAuto);

    }
}
