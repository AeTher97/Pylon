package org.urzednicza.pylon.models;

public class Metrics {
    Conns ConnsObject;
    Http HttpObject;


    // Getter Methods

    public Conns getConns() {
        return ConnsObject;
    }

    public Http getHttp() {
        return HttpObject;
    }

    // Setter Methods

    public void setConns(Conns connsObject) {
        this.ConnsObject = connsObject;
    }

    public void setHttp(Http httpObject) {
        this.HttpObject = httpObject;
    }
}