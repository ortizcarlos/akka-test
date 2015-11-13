package co.agileventure.akka;

import java.io.Serializable;

public class LogLineMessage implements Serializable {

    private String data;

    public LogLineMessage(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
