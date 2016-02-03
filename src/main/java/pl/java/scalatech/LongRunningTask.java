package pl.java.scalatech;

public class LongRunningTask {

    public String reverse(String body) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        return new StringBuilder(body).reverse().toString();
    }

}
