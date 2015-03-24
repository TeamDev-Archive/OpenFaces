package org.seleniuminspector.webriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;

/**
 * @author Vladislav Lubenskiy
 */
public class WebDriverBasedSelenium extends WebDriverBackedSelenium {
    public WebDriverBasedSelenium(WebDriver baseDriver, String baseUrl) {
        super(baseDriver, baseUrl);
    }
    @Override
    public boolean isTextPresent(String text) {
        return getWrappedDriver().getPageSource().contains(text);
    }
    @Override
    public void start() {

    }
}
