package de.unibremen.agra.gexf;

import java.util.*;

/**
 * Created by Kenneth Schmitz on 26.10.16.
 */
public class Preprocessor {
    String moduleName;
    List<Gate> inputs;
    List<Gate> outputs;
    List<Gate> gates;
    List<Gate> fanouts;
    HashMap<String, List<String>> transitions;
    HashMap<String, Gate> nameToGate;

    int ID = 0;

    Preprocessor() {
        inputs = new Vector<>();
        outputs = new Vector<>();
        gates = new Vector<>();
        fanouts = new Vector<>();
        transitions = new HashMap<>();
        nameToGate = new HashMap<>();
    }

    private class gateComparator implements Comparator<Gate> {
        @Override
        public int compare(Gate x, Gate y) {
            return x.level < y.level ? -1 : x.level > y.level ? 1 : 0;
        }

    }

    public void connectCircuitGraph() {
        System.out.print("INFO: Wiring up circuit graph... ");
        for (Map.Entry<String, List<String>> from : transitions.entrySet()) {
            Gate currentGate = nameToGate.get(from.getKey());
            for (String to : from.getValue()) {
                Gate inGate = nameToGate.get(to);
                inGate.inputs.add(currentGate);
                currentGate.outputs.add(inGate);
            }
        }
        for (Gate output : outputs) {
            Gate currentGate = nameToGate.get(output.inputName);
            currentGate.outputs.add(output);
            output.inputs.add(currentGate);
        }
        System.out.println("DONE");
    }

    public void createFanouts(int ID) {
        System.out.print("INFO: Remodeling multi-output gate(s) as FANOUT(s)... ");
        for (Gate currentGate : gates) {
            if (currentGate.outputs.size() > 1) {
                Gate fanout = new Gate();
                fanout.name = currentGate.name + "_FANOUT";
                fanout.ID = ID++;
                fanout.type = "fanout";
                fanout.isInput = currentGate.isInput;
                if (currentGate.isInput) currentGate.isInput = false;
                fanout.outputs.addAll(currentGate.outputs);
                currentGate.outputs.clear();
                currentGate.outputs.add(fanout);
                fanout.inputs.add(currentGate);
                fanouts.add(fanout);
                for (Gate successorGates : fanout.outputs) {
                    successorGates.inputs.add(fanout);
                    successorGates.inputs.remove(currentGate);
                }
            }
        }
        gates.addAll(fanouts);
        System.out.println("DONE (" + fanouts.size() + " Gate(s) remodeled)");
    }

    public void levelize() {
        System.out.print("INFO: Levelizing circuit... ");
        Gate currentGate;
        Queue<Gate> worklist = new LinkedList<Gate>();
        worklist.addAll(inputs);
        while (!worklist.isEmpty()) {
            currentGate = worklist.poll();
            int currentLevel = currentGate.level;
            currentGate.setLevel();
            if (currentGate.level != currentLevel) {
                for (Gate output : currentGate.outputs) {
                    if (!currentGate.visited) {
                        worklist.add(output);
                        if (currentGate.type.equals("dff"))
                            currentGate.visited = true;
                    }
                }
            }
        }
        Collections.sort(gates, new gateComparator());
        System.out.println("DONE (Max Lvl.: " + gates.get(gates.size() - 1).level + ")");
    }
}
