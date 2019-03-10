package org.urzednicza.pylon.models;

import java.util.ArrayList;

public class NgrokList {
    ArrayList < Tunnel > tunnels = new ArrayList< Tunnel >();
    private String uri;


    // Getter Methods

    public String getUri() {
        return uri;
    }

    public ArrayList<Tunnel> getTunnels() {
        return tunnels;
    }

    // Setter Methods

    public void setUri(String uri) {
        this.uri = uri;
    }
}