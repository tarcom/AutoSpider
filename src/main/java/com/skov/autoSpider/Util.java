package com.skov.autoSpider;


import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;

/**
 * Created by aogj on 20-12-2016.
 */
public class Util {

    public static final double AFSKRIVNING_PR_YEAR_PERCENT = 0.16;
    public static final double NEW_PRICE_OK_VALUE_PERCENT = 0.9;
    public static final int NORMAL_NUMBER_OF_KM_PR_YEAR = 22000;
    public static final double KM_TILLAEG = 0.25;


    public static void main(String[] args) {
        AutoWrapper auto = new AutoWrapper("", "", "", "",75000, 2011,
                187000, 22.7, 280000, 1, "", 1, 1, 1, "",
                "","",2011, 12 );

        doPrintCalculationDetails(auto);
    }

    public static void doPrintCalculationDetails(AutoWrapper auto) {

        System.out.format("age: %10.2f%n", getAge(auto));
        System.out.format("afskrivningAgeProcent: %10.1f%%%n", afskrivningAgeProcent(auto)*100);
        System.out.format("getNewPriceMinusOK: %10.0f%n", getNewPriceMinusOK(auto));
        System.out.format("afskrivningAgePriceReduction: %10.0f%n", afskrivningAgePriceReduction(auto));
        System.out.format("getKmTooMuchOrTooLittle: %10.0f%n", getKmTooMuchOrTooLittle(auto));
        System.out.format("getKmTooMuchOrTooLittlePriceReduction: %10.0f%n", getKmTooMuchOrTooLittlePriceReduction(auto));
        System.out.format("getCalculatedPrice: %10.0f%n", getCalculatedPrice(auto));
        System.out.format("getScoreProcentage: %10.0f%%%n", getScoreProcentage(auto)*100);

    }

    public static double getAge(AutoWrapper auto) {

        Period age = Period.between(auto.getIndregDate(), LocalDate.now());
        double years = age.getYears();
        double decimalMonths = age.getMonths() / 12.0;

        double duration = years + decimalMonths;
        double format = Double.parseDouble(new DecimalFormat("#.0").format(duration).replace(',', '.'));

        return format;

    }

    public static double afskrivningAgeProcent(AutoWrapper auto) {
        double res = Math.pow((1 - AFSKRIVNING_PR_YEAR_PERCENT), getAge(auto));
        return res;
    }

    public static double getNewPriceMinusOK(AutoWrapper auto) {
        if (auto.getNewPrice() == -1) {
            return 0;
        }
        double res = auto.getNewPrice() * NEW_PRICE_OK_VALUE_PERCENT;
        return res;
    }

    public static double afskrivningAgePriceReduction(AutoWrapper auto) {
        double res = afskrivningAgeProcent(auto) * getNewPriceMinusOK(auto);
        return res;
    }

    public static double getKmTooMuchOrTooLittle(AutoWrapper auto) {
        double res = ((getAge(auto) * NORMAL_NUMBER_OF_KM_PR_YEAR)) - auto.getKm();
            return res;
        }

    public static double getKmTooMuchOrTooLittlePriceReduction(AutoWrapper auto) {
        double res = getKmTooMuchOrTooLittle(auto) * KM_TILLAEG;
        return res;
    }

    public static double getCalculatedPrice(AutoWrapper auto) {
        if (auto.getNewPrice() == -1) {
            return 0;
        }
        double res = afskrivningAgePriceReduction(auto) + getKmTooMuchOrTooLittlePriceReduction(auto);
        return res;
    }

    public static double getScoreProcentage(AutoWrapper auto) {
        if (auto.getNewPrice() == -1) {
            return 0;
        }
        double res = (getCalculatedPrice(auto) / auto.getPrice()) - 1;
        return res;
    }


}