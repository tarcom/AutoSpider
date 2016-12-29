package com.skov.autoSpider;

/**
 * Created by aogj on 20-12-2016.
 */

import java.io.IOException;
import java.util.TreeMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoSpider {

    public static void main(String[] args) throws Exception {
        doFetchAutos(false);
    }

    public static void doFetchAutos(boolean doItQuicklyNDirty) throws Exception {

        int i = 0;

        //String uri = "http://www.bilbasen.dk/brugt/bil?YearFrom=2012&YearTo=2016&PriceFrom=25000&PriceTo=50000&MileageFrom=10000&MileageTo=75000&ZipCode=9310&IncludeEngrosCVR=False&IncludeSellForCustomer=True&IncludeWithoutVehicleRegistrationTax=False&IncludeLeasing=False&HpFrom=&HpTo=";
        String uri = "http://www.bilbasen.dk/brugt/bil?YearFrom=2008&YearTo=2016&PriceFrom=25000&PriceTo=50000&MileageFrom=10000&MileageTo=125000&ZipCode=9310&IncludeEngrosCVR=False&IncludeSellForCustomer=True&IncludeWithoutVehicleRegistrationTax=False&IncludeLeasing=False&HpFrom=&HpTo=&SortBy=price&SortOrder=asc";
        Connection connect = Jsoup.connect(uri).userAgent("Mozilla");
        connect.cookie("PageSize", "9999");
        Document docAutoListPage = connect.get();

        Elements divsOnAutoListPage = docAutoListPage.select("div");
        for (Element divSpecificAutoOnListPage : divsOnAutoListPage) {
            if (divSpecificAutoOnListPage.attr("class").equals("row listing listing-discount bb-listing-clickable")) {

                try {


                    String baseHeadline = divSpecificAutoOnListPage.getElementsByClass("listing-heading").get(0).text().trim();
                    int price = extractInt(divSpecificAutoOnListPage.getElementsByClass("col-xs-3").get(1).text());
                    int year = extractInt(divSpecificAutoOnListPage.getElementsByClass("col-xs-2").get(3).text());
                    int km = extractInt(divSpecificAutoOnListPage.getElementsByClass("col-xs-2").get(2).text());
                    int kml = extractInt(divSpecificAutoOnListPage.getElementsByClass("variableDataColumn").text());
                    int greenEA = extractInt(divSpecificAutoOnListPage.getElementsByAttribute("data-moth").attr("data-moth"));
                    int distance = extractInt(divSpecificAutoOnListPage.getElementsByClass("col-xs-2").get(1).text());
                    int hk = extractInt(divSpecificAutoOnListPage.getElementsByAttribute("data-hk").attr("data-hk"));
                    int acc100 = extractInt(divSpecificAutoOnListPage.getElementsByAttribute("data-kmt").attr("data-kmt"));
                    String text = divSpecificAutoOnListPage.text().trim();
                    String town = divSpecificAutoOnListPage.getElementsByClass("col-xs-2").get(4).text().trim();
                    String thumbImg = divSpecificAutoOnListPage.getElementsByAttribute("data-echo").attr("data-echo");

                    String urlDetail = "http://www.bilbasen.dk" + divSpecificAutoOnListPage.getElementsByClass("listing-heading").get(0).attr("href");

                    //quickly:
                    String headline1 = baseHeadline;
                    String headline2 = "";
                    int indregYear = -1;
                    int indregMonth = -1;
                    int nypris = -1;

                    if (!doItQuicklyNDirty) {
                        Document docAutoDetailPage = Jsoup.connect(urlDetail).get();

                        headline1 = docAutoDetailPage.select("H1").select("span").text().trim();
                        headline2 = docAutoDetailPage.select("H1").text().replaceAll(headline1, "").trim();
                        indregYear = extractInt(docAutoDetailPage.getElementById("bbVipDescriptionFacts").select("li").get(0).text().replaceAll("Indreg.", "").trim().split("/")[1]);
                        indregMonth = extractInt(docAutoDetailPage.getElementById("bbVipDescriptionFacts").select("li").get(0).text().replaceAll("Indreg.", "").trim().split("/")[0]);
                        nypris = extractInt(docAutoDetailPage.getElementsByClass("odd").get(0).text());
                    }


                    AutoWrapper auto = new AutoWrapper(uri, urlDetail, headline1, headline2, price, year, km, kml, nypris, distance, town, greenEA, hk, acc100, thumbImg, text, baseHeadline, indregYear, indregMonth);

                    //System.out.println(Util.getAge(auto) + " - " + auto.getBaseHeadline());
                    i++;
                    System.out.print(".");
                    //auto.printAuto();
                    DBHandler.insert(auto);
                    //Util.doPrintCalculationDetails(auto);
                    //break;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println();
        System.out.println("fetched " + i + " cars.");

    }

    public static int extractInt(String txt) {
        try {
            return Integer.parseInt(txt.replaceAll("[\\D]", ""));
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }


}
