package de.unibremen.agra.gexf;

import de.unibremen.agra.gexf.generated.*;

import java.util.List;
import java.util.Vector;

/**
 * Created by Kenneth Schmitz on 25.10.16.
 */
public class VerilogListener extends Verilog2001_netlistBaseListener {

    Preprocessor preprocessor;
    int ID;

    @Override
    public void enterModule_identifier(Verilog2001_netlistParser.Module_identifierContext ctx) {
        preprocessor.moduleName = ctx.getText();
    }

    @Override
    public void enterInputs(Verilog2001_netlistParser.InputsContext ctx) {

        for (int i = 0; i < ctx.getChild(1).getChildCount(); i++) {
            Gate g = new Gate();
            g.name = "IN_" + ctx.getChild(1).getChild(i).getText();
            g.ID = this.ID++;
            g.type = "in";
            g.isInput = true;
            g.isOutput = false;
            preprocessor.gates.add(g);
            preprocessor.inputs.add(g);
            preprocessor.nameToGate.put(ctx.getChild(1).getChild(i).getText(), g);
        }
    }

    @Override
    public void enterOutputs(Verilog2001_netlistParser.OutputsContext ctx) {
        for (int i = 0; i < ctx.getChild(1).getChildCount(); i++) {
            Gate g = new Gate();
            g.name = "OUT_" + ctx.getChild(1).getChild(i).getText();
            g.ID = this.ID++;
            g.type = "out";
            g.isInput = false;
            g.isOutput = true;
            g.inputName = ctx.getChild(1).getChild(i).getText();
            preprocessor.gates.add(g);
            preprocessor.outputs.add(g);
            preprocessor.nameToGate.put(ctx.getChild(1).getChild(i).getText(), g);
        }
    }

    @Override
    public void enterGate(Verilog2001_netlistParser.GateContext ctx) {
        if (ctx.getChild(0).getText().equals("dff")) {
            Gate g = new Gate();
            g.name = ctx.getChild(1).getText();
            g.ID = this.ID++;
            g.type = ctx.getChild(0).getText();
            g.isInput = false;
            g.isOutput = false;
            preprocessor.gates.add(g);
            preprocessor.nameToGate.put(ctx.getChild(3).getChild(1).getText(), g);
            if (ctx.getChild(3).getChildCount() > 3) System.out.println("WARN: No multi-out FFs supported");
            for (int i = 2; i < ctx.getChild(3).getChildCount(); i++) {
                if (!preprocessor.transitions.containsKey(ctx.getChild(3).getChild(i).getText())) {
                    List<String> connectedGates = new Vector<>();
                    connectedGates.add(ctx.getChild(3).getChild(1).getText());
                    preprocessor.transitions.put(ctx.getChild(3).getChild(i).getText(), connectedGates);
                } else {
                    preprocessor.transitions.get(ctx.getChild(3).getChild(i).getText()).add(ctx.getChild(3).getChild(0).getText());
                }
            }
        } else {
            Gate g = new Gate();
            g.name = ctx.getChild(1).getText();
            g.ID = this.ID++;
            g.type = ctx.getChild(0).getText();
            g.isInput = false;
            g.isOutput = false;
            preprocessor.gates.add(g);
            preprocessor.nameToGate.put(ctx.getChild(3).getChild(0).getText(), g);
            for (int i = 1; i < ctx.getChild(3).getChildCount(); i++) {
                if (!preprocessor.transitions.containsKey(ctx.getChild(3).getChild(i).getText())) {
                    List<String> connectedGates = new Vector<>();
                    connectedGates.add(ctx.getChild(3).getChild(0).getText());
                    preprocessor.transitions.put(ctx.getChild(3).getChild(i).getText(), connectedGates);
                } else {
                    preprocessor.transitions.get(ctx.getChild(3).getChild(i).getText()).add(ctx.getChild(3).getChild(0).getText());
                }
            }
        }
    }
}
