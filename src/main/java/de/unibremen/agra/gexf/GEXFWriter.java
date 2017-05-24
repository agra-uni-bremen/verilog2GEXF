package de.unibremen.agra.gexf;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Kenneth Schmitz on 08.11.16.
 */
public class GEXFWriter {
    String circuitName;
    List<Gate> gates;
    int edgeID;
    String filename;
    String timeStamp;
    Config config;

    public GEXFWriter(List<Gate> gates, String circuitName, int maxGateID, Config config) {
        this.circuitName = circuitName;
        this.gates = gates;
        this.edgeID = maxGateID;
        timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        this.config = config;
    }

    public void printGexf() {
        try {
            PrintWriter writer = new PrintWriter(filename + ".gexf", "UTF-8");
            // Fileheader
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\">");
            writer.println("    <meta lastmodifieddate=\""+timeStamp+"\">");
            writer.println("        <creator>Verilog2GEXF</creator>");
            writer.println("        <description> " + circuitName + " </description>");
            writer.println("    </meta>");
            writer.println("    <graph mode=\"static\" defaultedgetype=\"directed\">");
            // Nodes
            writer.println("        <nodes>");
            for (Gate currentGate : gates) {
                if (currentGate.outputs.size() == 0 && currentGate.isInput) continue;
                writer.println("            <node id=\"" + currentGate.ID + "\" label=\"" + currentGate.name + "\" level=\"" + currentGate.level + "\" />");
            }
            writer.println("        </nodes>");
            // Edges
            writer.println("        <edges>");
            for (Gate currentGate : gates) {
                //if (currentGate.type.equals("fanout")) {
                if(currentGate.outputs.size() > 1) {
                    for (Gate fanOutput : currentGate.outputs) {
                        writer.println("            <edge id=\"" + edgeID++ + "\" source=\"" + currentGate.ID + "\" target=\"" + fanOutput.ID + "\" />");
                    }
                } else {
                    if (!currentGate.isOutput) {
                        if (currentGate.outputs.size() > 0) {
                            writer.println("            <edge id=\"" + edgeID++ + "\" source=\"" + currentGate.ID + "\" target=\"" + currentGate.outputs.get(0).ID + "\" />");
                        }
                    }
                }
            }
            writer.println("        </edges>");
            writer.println("    </graph>");
            writer.println("</gexf>");
            writer.close();
        } catch (Exception e) {
            System.out.println("ERROR: couldn't write file");
        }
    }
}


