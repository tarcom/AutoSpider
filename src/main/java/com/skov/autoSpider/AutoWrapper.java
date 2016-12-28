package com.skov.autoSpider;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aogj on 20-12-2016.
 */
public class AutoWrapper {

    private String urlBase;
    private String urlDetail;

    private String headline1;
    private String headline2;
    private int price;
    private int year;
    private int km;
    private double kml;
    private int newPrice;
    private int distance;
    private String town;
    private int greenEA;
    private int hk;
    private int acc100;
    private String thumbImg;
    private String text;
    private String baseHeadline;
    private LocalDate indregDate;

    public AutoWrapper(String urlBase, String urlDetail, String headline1, String headline2, int price, int year, int km, double kml, int newPrice, int distance, String town, int greenEA, int hk, int acc100, String thumbImg, String text, String baseHeadline, int indregYear, int indregMonth) {
        this.urlBase = urlBase;
        this.urlDetail = urlDetail;
        this.headline1 = headline1;
        this.headline2 = headline2;
        this.price = price;
        this.year = year;
        this.km = km;
        this.kml = kml;
        this.newPrice = newPrice;
        this.distance = distance;
        this.town = town;
        this.greenEA = greenEA;
        this.hk = hk;
        this.acc100 = acc100;
        this.thumbImg = thumbImg;
        this.text = text;
        this.baseHeadline = baseHeadline;

        if (indregYear != year) {
            System.out.println("warning: indregYear is different from year! indregYear=" + indregYear + ", year=" + year + " - " + urlDetail);
            indregYear = year;
            indregMonth = 6; //average I guess...
        }

        LocalDate ld = LocalDate.of(indregYear, indregMonth, 1);
        this.indregDate = ld;

    }

    public void printAuto() {
        System.out.println("headline1: " + headline1);
        System.out.println("headline2: " + headline2);
        System.out.println("price    : " + price);
        System.out.println("year     : " + year);
        System.out.println("km       : " + km);
        System.out.println("km/l     : " + kml);
        System.out.println("distance : " + distance);
        System.out.println("town     : " + town);
        System.out.println("grøn e.a.: " + greenEA);
        System.out.println("hk       : " + hk);
        System.out.println("0-100    : " + acc100);
        System.out.println("text     : " + text);
        System.out.println("thumbImg : " + thumbImg);
        System.out.println("url      : " + urlDetail);
        System.out.println("newPrice : " + newPrice);
    }

    public String getUrlBase() {
        return urlBase;
    }

    public String getUrlDetail() {
        return urlDetail;
    }

    public String getHeadline1() {
        return headline1;
    }

    public String getHeadline2() {
        return headline2;
    }

    public int getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public int getKm() {
        return km;
    }

    public double getKml() {
        return kml;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public int getDistance() {
        return distance;
    }

    public String getTown() {
        return town;
    }

    public int getGreenEA() {
        return greenEA;
    }

    public int getHk() {
        return hk;
    }

    public int getAcc100() {
        return acc100;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public String getText() {
        return text;
    }

    public String getBaseHeadline() {
        return baseHeadline;
    }

    public LocalDate getIndregDate() {
        return indregDate;
    }

}
