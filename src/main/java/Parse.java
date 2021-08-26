import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
    public static void main(String[] args) throws Exception {
        while (true) {
            Document page = getPage();
            Element newsBoard = page.select("div[class=box list lastnews]").first();

            assert newsBoard != null;
            Element topNews = newsBoard.select("h4[class=b-article__title]").first();

            assert topNews != null;
            String newsLinkString = topNews.toString();

            Date dateNow = new Date();

            String newsLink = getNewsLinkFromHtml(newsLinkString);
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss a");
            System.out.println(formatForDateNow.format(dateNow) + " " + topNews.text() + ", link: " + newsLink);

            try {
                Thread.sleep(60000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Document getPage() throws IOException {
        String url = "https://ukraine.segodnya.ua/";

        return Jsoup.parse(new URL(url), 3000);
    }

    private final static Pattern newsLinkPattern = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)");

    public static String getNewsLinkFromHtml(String newsLinkString) throws Exception {
        Matcher matcher = newsLinkPattern.matcher(newsLinkString);

        if (matcher.find()) {
            return matcher.group();
        }

        throw new Exception("Can't extract link from the string!");
    }

}
