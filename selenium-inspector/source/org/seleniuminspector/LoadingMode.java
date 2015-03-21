package org.seleniuminspector;

/**
 * @author Eugene Goncharov
 */
public abstract class LoadingMode {

    public abstract void waitForLoad();

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
