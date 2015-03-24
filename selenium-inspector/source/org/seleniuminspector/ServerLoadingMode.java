package org.seleniuminspector;

import com.thoughtworks.selenium.Selenium;

/**
 * @author Eugene Goncharov
 */
public class ServerLoadingMode extends LoadingMode {
    private static LoadingMode loadingMode = new ServerLoadingMode();

    private ServerLoadingMode() {
    }

    public static LoadingMode getInstance() {
        return loadingMode;
    }

    public void waitForLoad() {
        Selenium selenium = SeleniumHolder.getInstance().getSelenium();
        selenium.waitForPageToLoad("30000");
        sleep(1000);
    }
}
