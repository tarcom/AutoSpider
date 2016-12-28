package com.skov.autoSpider;

/**
 * Created by aogj on 20-12-2016.
 */

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoSpider {

    public static void main(String[] args) throws Exception {

        int i = 0;
        String uri = "http://www.bilbasen.dk/brugt/bil?YearFrom=2012&YearTo=2016&PriceFrom=25000&PriceTo=50000&MileageFrom=10000&MileageTo=75000&ZipCode=9310&IncludeEngrosCVR=False&IncludeSellForCustomer=True&IncludeWithoutVehicleRegistrationTax=False&IncludeLeasing=False&HpFrom=&HpTo=";

        Connection connect = Jsoup.connect(uri).data("PageSize", "96").userAgent("Mozilla");
        Document doc = connect.get();

        Elements divs = doc.select("div");
        for (Element div : divs) {
            if (div.attr("class").equals("row listing listing-discount bb-listing-clickable")) {

                try {

                    String urlDetail = "http://www.bilbasen.dk" + div.getElementsByClass("listing-heading").get(0).attr("href");
                    Document docDetail = Jsoup.connect(urlDetail).get();

                    String headline1 = docDetail.select("H1").select("span").text().trim();
                    String headline2 = docDetail.select("H1").text().replaceAll(headline1, "").trim();
                    String baseHeadline = div.getElementsByClass("listing-heading").get(0).text().trim();
                    int price = extractInt(div.getElementsByClass("col-xs-3").get(1).text());
                    int year = extractInt(div.getElementsByClass("col-xs-2").get(3).text());
                    int indregYear = extractInt(docDetail.getElementById("bbVipDescriptionFacts").select("li").get(0).text().replaceAll("Indreg.", "").trim().split("/")[1]);
                    int indregMonth = extractInt(docDetail.getElementById("bbVipDescriptionFacts").select("li").get(0).text().replaceAll("Indreg.", "").trim().split("/")[0]);
                    int km = extractInt(div.getElementsByClass("col-xs-2").get(2).text());
                    int kml = extractInt(div.getElementsByClass("variableDataColumn").text());
                    int nypris = extractInt(docDetail.getElementsByClass("odd").get(0).text());
                    int greenEA = extractInt(div.getElementsByAttribute("data-moth").attr("data-moth"));
                    int distance = extractInt(div.getElementsByClass("col-xs-2").get(1).text());
                    int hk = extractInt(div.getElementsByAttribute("data-hk").attr("data-hk"));
                    int acc100 = extractInt(div.getElementsByAttribute("data-kmt").attr("data-kmt"));
                    String text = div.text().trim();
                    String town = div.getElementsByClass("col-xs-2").get(4).text().trim();
                    String thumbImg = div.getElementsByAttribute("data-echo").attr("data-echo");

                    AutoWrapper auto = new AutoWrapper(uri, urlDetail, headline1, headline2, price, year, km, kml, nypris, distance, town, greenEA, hk, acc100, thumbImg, text, baseHeadline, indregYear, indregMonth);

                    System.out.println(Util.getAge(auto) + " - " + auto.getBaseHeadline());
                    i++;
                    auto.printAuto();
                    Util.doPrintCalculationDetails(auto);
                    break;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

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
