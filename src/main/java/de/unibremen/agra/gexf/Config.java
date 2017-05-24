package de.unibremen.agra.gexf;

/**
 * Created by kenneth on 24.05.17.
 */
public class Config {
    String filepath;
    boolean createFans;
    boolean levelize;
    boolean noOutput;

    // Constructor sets default configuration
    public Config() {
        this.filepath = "";
        this.createFans = true;
        this.levelize = true;
        this.noOutput = false;
    }
}
