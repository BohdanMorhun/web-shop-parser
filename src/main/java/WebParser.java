import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebParser {

    public static void main(String[] args) throws IOException {

        List<Product> productList = new CopyOnWriteArrayList<>();

        Document doc = Jsoup.connect("https://www.aboutyou.de/maenner/bekleidung")
                .userAgent("Chrome/79.0.3945.79")
                .referrer("http://www.google.com")
                .get();

        /*
         parsing HTML
         */
        Elements elements = doc.getElementsByClass("sc-1qheze-0 iqQUjr");

        elements.forEach(element -> {
            String brand = element.select("p").attr("data-test-id", "BrandName").text();

            String name = element.select("a").attr("data-test-id", "ProductTile")
                    .attr("href");

            String price = element.getElementsByClass("sc-137x7zs-0 x3voc9-0 ddcEYK").text();

            String articleId = element.select("a").attr("data-test-id",
                    "ProductTile").attr("id");

            Elements colorList = element.select("li").attr("data-test-id", "ColorContainer");

            List<String> productColors = new ArrayList<>();
            colorList.forEach(colors -> productColors.add(colors.attr("color")));
            productList.add(new Product(name, brand, productColors, price, articleId));
        });

     /*
       creating JSON file
     */
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("products.json"), productList);
        System.out.println("Amount of extracted products " + productList.size());
    }
}




