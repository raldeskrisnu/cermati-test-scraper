package com.raldes.webscrapping.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static List<String> getTexts(String strHtml) {
        List<String> textList = new ArrayList<>();
        Document document = Jsoup.parse(strHtml);
        Elements paragraphs = document.select("p");
        Elements lists = document.select("ul");
        for (Element p : paragraphs) {
            textList.add(p.text());
        }
        for (Element ul : lists) {
            for (Element li : ul.select("li")) {
                textList.add(li.text());
            }
        }
        return textList;
    }
}
