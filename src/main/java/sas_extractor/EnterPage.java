package sas_extractor;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnterPage {

    static Logger log = Logger.getLogger("com.gargoylesoftware");

    public static void main(String[] args) throws Exception {
        // set logging info off
        log.setLevel(Level.OFF);

        submittingForm();
        goingToNext();
    }


    public static void submittingForm() throws Exception {

        WebClient webClient = new WebClient();

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        // Get the first page
        HtmlPage page1 = webClient.getPage("https://classic.flysas.com/en/de/");

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
        HtmlForm form = page1.getFormByName("ctl00$FullRegion$MainRegion$ContentRegion$ContentFullRegion$ContentLeftRegion$CEPGroup1$CEPActive$cepNDPRevBookingArea$predictiveSearch$hiddenFrom");
        form.setNameAttribute("Stockholm, Sweden - Arlanda (ARN)");
        System.out.println("du");

        HtmlForm form2 = page1.getFormByName("ctl00$FullRegion$MainRegion$ContentRegion$ContentFullRegion$ContentLeftRegion$CEPGroup1$CEPActive$cepNDPRevBookingArea$predictiveSearch$txtFrom");
        form2.setNameAttribute("London, United Kingdom - Heathrow (LHR)");

        // Get submit button
        final HtmlSubmitInput button = form.getInputByName("ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_Searchbtn_ButtonLink");

        // Push submit button
        final HtmlPage page2 = button.click();
    }


    public static void goingToNext() throws Exception {

        WebDriver driver = new HtmlUnitDriver(false);

        // Get the first page
        driver.get("https://classic.flysas.com/en/de/");

        System.out.println(((HtmlUnitDriver) driver).isJavascriptEnabled());

        // Print all page to console
        List<WebElement> el = driver.findElements(By.cssSelector("*"));
        for (WebElement e : el) {
            System.out.println(e);
        }


        // Find element by name
        WebElement element = driver.findElement(By.name("ctl00$FullRegion$MainRegion$ContentRegion$ContentFullRegion$ContentLeftRegion$CEPGroup1$CEPActive$cepNDPRevBookingArea$predictiveSearch$txtFrom"));
        // send String to name
        element.sendKeys("Stockholm, Sweden - Arlanda (ARN)");

        // Find element by name
        WebElement element1 = driver.findElement(By.name("ctl00$FullRegion$MainRegion$ContentRegion$ContentFullRegion$ContentLeftRegion$CEPGroup1$CEPActive$cepNDPRevBookingArea$predictiveSearch$txtTo"));
        // Send String to name
        element1.sendKeys("London, United Kingdom - Heathrow (LHR)");

        // Find button
        WebElement element2 = driver.findElement(By.id("ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_Searchbtn"));
        element2.click();

        System.out.println(driver.getCurrentUrl());

        driver.quit();
    }


}
