package org.urzednicza.pylon.models;

public class Tunnel {
    private String name;
    private String uri;
    private String public_url;
    private String proto;
    Config ConfigObject;
    Metrics MetricsObject;


    // Getter Methods

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getPublic_url() {
        return public_url;
    }

    public String getProto() {
        return proto;
    }

    public Config getConfig() {
        return ConfigObject;
    }

    public Metrics getMetrics() {
        return MetricsObject;
    }

    // Setter Methods

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPublic_url(String public_url) {
        this.public_url = public_url;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public void setConfig(Config configObject) {
        this.ConfigObject = configObject;
    }

    public void setMetrics(Metrics metricsObject) {
        this.MetricsObject = metricsObject;
    }
}



