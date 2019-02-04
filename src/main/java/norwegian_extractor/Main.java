package norwegian_extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main{

    public static void main(String[] args) {

        // setting for HTTP analyzer to catch connections
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyPort", "8888");

        BufferedWriter writer = null;

        try {

            // file creation
            String timeLog = new SimpleDateFormat("yyyy-MM-dd_HH:mm").format(Calendar.getInstance().getTime());
            File logFile = new File(timeLog + ".txt");
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("\n" + timeLog);

            // for-cycle form 1 till 28 day
            for (int i = 1; i <29 ; i++) {

                Document doc = Jsoup.connect("https://www.norwegian.com/us/ipc/availability/avaday?D_City=OSL&A_City=RIX&TripType=1&D_Day=0" + i +"&D_Month=201902&D_SelectedDay=01&R_Day=01&R_Month=201902&R_SelectedDay=01&dFlight=DY740OSLTRDDY1078TRDRIX&dCabinFareType=1&IncludeTransit=false&AgreementCodeFK=-1&CurrencyCode=USD&rnd=41264&processid=80441&mode=ab")
                        .header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(600000)
                        .get();

                Element script = doc.select("*").first();

                // paterns and matchers for regex of information in html
                Pattern a = Pattern.compile("<td.align=.right..valign=*[^>]*?>.*?td>");
                Pattern b = Pattern.compile("flightInboundDate....(..........)");
                Pattern c = Pattern.compile("flightOriginId....(...)");
                Pattern d = Pattern.compile("flightDestId....(...)");
                Pattern e = Pattern.compile("flightOutboundTime....(.....)");
                Pattern f = Pattern.compile("<td.class=.arrdest.>\\n.*emphasize.>\\n.*(..:..)");
                Pattern g = Pattern.compile("<span>.Taxes..Fees.*>\\n.*<.div><.td>\\n.*<td.class=.rightcell.emphasize.*>.*(......)<.td>");

                Matcher am = a.matcher(script.html());
                Matcher bm = b.matcher(script.html());
                Matcher cm = c.matcher(script.html());
                Matcher dm = d.matcher(script.html());
                Matcher em = e.matcher(script.html());
                Matcher fm = f.matcher(script.html());
                Matcher gm = g.matcher(script.html());

                // String for collected information
                String price = "";
                String date = "";
                String originDest = "";
                String flightDest = "";
                String outboundTime = "";
                String inboundTime = "";
                String taxes = "";

                //while-cycle to gather all information
                while (am.find() && bm.find() && cm.find() && dm.find() && em.find() && fm.find() && gm.find()) {

                    price = am.group();
                    date = bm.group(1);
                    originDest = cm.group(1);
                    flightDest = dm.group(1);
                    outboundTime = em.group(1);
                    inboundTime = fm.group(1);
                    taxes = gm.group(1);

                }


                if (price.equals("")) {

                    System.out.println();
                    if (i < 10) {
                        writer.write("\n" + "\n2019-02-0" + i + ":");
                        System.out.println("2019-02-0"+i);
                    } else {
                        writer.write("\n" + "\n2019-02-" + i+ ":");
                        System.out.println("2019-02-" + i);
                    }
                    writer.write("\n" + "There are no direct flights this day");
                    System.out.println("There are no direct flights this day");

                } else {

                    price = priceTag(price);

                    System.out.println();
                    writer.write("\n" + "\n" + date + ":");
                    System.out.println(date + ":");
                    writer.write("\n" + "From " + originDest + " to " + flightDest +". Flight leaves " + outboundTime + ", arrives " + inboundTime + ": Lowest Price = " + price + ", taxes: " + taxes);
                    System.out.println("From " + originDest + " to " + flightDest +". Flight leaves " + outboundTime + ", arrives " + inboundTime + ": Lowest Price = " + price + ", taxes: " + taxes);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // method to substract price form string
    static String priceTag(String initialCurrency) {

        String replace = "<td align=\"right\" valign=\"bottom\" class=\"rightcell totalfarecell\" nowrap>";
        String replace1 = "</td>";
        String replace2 = "";
        initialCurrency = initialCurrency.replaceFirst(replace, replace2);
        initialCurrency = initialCurrency.replaceFirst(replace1, replace2);
        return initialCurrency;
    }


}