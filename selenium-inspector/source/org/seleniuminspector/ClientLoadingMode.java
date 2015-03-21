package org.seleniuminspector;

/**
 * @author Eugene Goncharov
 */
public class ClientLoadingMode extends LoadingMode {
    private static LoadingMode loadingMode = new ClientLoadingMode();

    private ClientLoadingMode() {
    }

    public static LoadingMode getInstance() {
        return loadingMode;
    }

    public void waitForLoad() {

    }
}
