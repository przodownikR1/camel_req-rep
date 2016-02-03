package pl.java.scalatech;

public class Worker {

    public String reverse(String body) {
        return new StringBuilder(body).reverse().toString();
    }

}
