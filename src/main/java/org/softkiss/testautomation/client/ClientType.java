package org.softkiss.testautomation.client;

/**
 * Created by v.shcherbanyuk on 12/26/2014.
 */
public enum ClientType {
    IE("ie"),
    FF("ff"),
    GC("gc"),
    PJS("pjs");

    private String clientName;

    private ClientType(final String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return clientName;
    }
}
