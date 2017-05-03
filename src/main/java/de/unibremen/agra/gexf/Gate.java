package de.unibremen.agra.gexf;

import java.util.List;
import java.util.Vector;

/**
 * Created by Kenneth Schmitz on 24.10.16.
 */
public class Gate {

    String name;
    String inputName; // Used for connection to outputs
    Integer ID;
    String type;
    boolean isInput;
    boolean isOutput;
    boolean visited;
    Integer level;

    List<Gate> inputs;
    List<Gate> outputs;

    public Gate() {
        inputs = new Vector<>();
        outputs = new Vector<>();
        level = -1;
        visited = false;
    }

    public Gate(String gateName, Integer gateID, String gateType) {
        name = gateName;
        ID = gateID;
        type = gateType;
        inputs = new Vector<>();
        outputs = new Vector<>();
        level = -1;
    }

    public void printGate() {
        System.out.print("Gate " + name + " (ID:" + ID + ") of type " + type);
        if (isInput) {
            System.out.println(" is input");
        } else if (isOutput) {
            System.out.println(" is output");
        } else
            System.out.println();

        System.out.print("\tInputs:");
        for (Gate g : inputs) {
            System.out.print(g.name + " ");
        }
        System.out.println();
        System.out.print("\tOutputs:");
        for (Gate g : outputs) {
            System.out.print(g.name + " ");
        }
        System.out.println();
    }

    public void setLevel() {
        if (type.equals("in")) // If first in circuit
        {
            level = 0;
        } else // In this case, there are predecessors
        {
            int max = 0;
            for (Gate input : inputs) {
                if (input.type.equals("dff")) continue;
                if (input.level > max) {
                    max = input.level;
                }
            }
            level = 1 + max; // set largest max value and increment by 1 -> next level
        }
    }

    @Override
    public String toString() {
        return name + "(" + ID + ":" + type + " @lvl:" + level + " ->" + inputs.size() + " " + outputs.size() + "->)";
    }
}
