package org.seleniuminspector.webriver;

import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.seleniuminspector.SeleniumFactory;

/**
 * @author Vladislav Lubenskiy
 */
public class WebDriverBasedSeleniumFactory extends SeleniumFactory {

    public WebDriverBasedSeleniumFactory(String serverHost, int serverPort, String browserPath, String browserUrl, boolean addNamespacesToXpath) {
        super(serverHost, serverPort, browserPath, browserUrl, addNamespacesToXpath);
    }

    public WebDriverBasedSeleniumFactory(String serverHost, int serverPort, String browserPath, String browserUrl) {
        super(serverHost, serverPort, browserPath, browserUrl);
    }

    @Override
    public Selenium getSelenium() {
        WebDriver driver = new FirefoxDriver();
        return new WebDriverBasedSelenium(driver, "http://localhost:8080");
    }
}
