package de.unibremen.agra.gexf;

import de.unibremen.agra.gexf.generated.*;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.util.List;
import java.util.Vector;

/**
 * Created by Kenneth Schmitz on 24.10.16.
 */
public class Verilog2Gexf {

    List<Gate> gates;

    Verilog2Gexf() {
        gates = new Vector<>();
    }

    public static void main(String[] args) throws Exception {
        int ID = 0;

        String filename = "";
        File file;
        InputStream IS = null;

        boolean fileNameNext = false;

        if (args.length != 1) {
            System.out.println("ERROR: Input file must be specified.\n \t $ java -jar verilog2gexf.jar /path/to/file.v \n \t generated *.gexf file will be written to source directory");
            System.exit(0);
        }

        assert args.length == 1;
        
        for (String arg : args) {
            filename = arg;
            file = new File(filename);

            if (!file.isFile() || !file.canRead()) {
                System.out.println("ERROR: File not present or inaccessible");
                System.exit(-1);
            } else {
                try {
                    IS = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    System.out.println("ERROR: Can not open " + filename);
                    System.exit(-1);
                }
            }
        }

        ANTLRInputStream input = null;

        try {
            input = new ANTLRInputStream(IS);
        } catch (IOException e) {
            System.out.println("ERROR: Can not read netlist");
            System.exit(-1);
        }

	// ANTLR default usage
        Verilog2001_netlistLexer lexer = new Verilog2001_netlistLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Verilog2001_netlistParser parser = new Verilog2001_netlistParser(tokens);
        ParseTree tree = parser.module_declaration();
        ParseTreeWalker walker = new ParseTreeWalker();
        VerilogListener listener = new VerilogListener();

	// Pass initial ID to preprocessor
        listener.ID = ID;
        listener.preprocessor = new Preprocessor();
	// Walk parse tree to trigger callbacks from VerilogListener
        walker.walk(listener, tree);
	// Rescue final ID from parser
        ID = listener.ID;

	// Connect circuit, create fanouts and levelize circuit
        listener.preprocessor.connectCircuitGraph();
        listener.preprocessor.createFanouts(ID);
        listener.preprocessor.levelize();

	// Create GEXF XML file
        GEXFWriter writer = new GEXFWriter(listener.preprocessor.gates, listener.preprocessor.moduleName, ID);
        writer.filename = filename;
        writer.printGexf();
    }
}
