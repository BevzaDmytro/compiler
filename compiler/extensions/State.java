package compiler.extensions;

import java.util.ArrayList;

public class State {
    private int number;
    private String defaultFunc;
    private ArrayList<Instruction> instructions;



    public State(int number, ArrayList<Instruction> instructions, String defaultFunc) {
        this.number = number;
        this.instructions = instructions;
        this.defaultFunc = defaultFunc;
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public String getDefaultFunc() {
        return defaultFunc;
    }

}
