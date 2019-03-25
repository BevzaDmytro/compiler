package compiler.analyzers;

import compiler.extensions.*;
import compiler.views.ConfigurationView;

import java.util.ArrayList;
import java.util.Stack;

public class SyntaxAnalyzer2 {
    private LexemsTable lexems;
    private int currentState;
    private ConfigurationView configurationView = new ConfigurationView();
    private boolean isExit = false;
    StateController stateController;

    public ConfigurationView getConfigurationView() {
        return configurationView;
    }

    public SyntaxAnalyzer2(LexemsTable lexemsTable) {
        this.lexems = lexemsTable;
    }

    public StateController getStateController() {
        return stateController;
    }

    public void analyze() throws Exception {
        stateController = new StateController();
        stateController.createStates();
        Stack stack = new Stack();
        Lexem lexem;
        int i = 0;

        String inputLexem = "";
        String states = "";
        String stackString = "";
        currentState = 1;
        while(i < lexems.getLexems().size()){
            lexem = lexems.getLexems().get(i);

            Instruction instruction = getInstruction(stateController.getStateByNum(currentState), lexem.getName());
            if(!instruction.getMark().equals("") && !this.isExit) {
                inputLexem = lexem.getName();
                states = String.valueOf(currentState);
            }
            if(hasState(stateController.getStateByNum(currentState), lexem.getName())){
                this.isExit =  false;
                if(instruction.getStackNum() != 0){
                    stack.push(instruction.getStackNum());
                    stackString = stack.toString();
                }
                if(instruction.getBeta() != 0){
                    currentState = instruction.getBeta();
                }
                else if(instruction.getFunc().equals("exit")){
                    if(!stack.empty()) {
                        currentState = (int) stack.pop();
                        states += "," + String.valueOf(currentState);
                    }
                    else {
                        stackString = "";
                        configurationView.addConfiguration(new Configuration(inputLexem, states, stackString));
//                        System.out.println("Stack is empty, programm finished");
                    }
                }
                i++;
                if(i<lexems.getLexems().size()) {
                    if (!lexems.getLexems().get(i).equals("")) {
                        configurationView.addConfiguration(new Configuration(inputLexem, states, stackString));
                    }
                }

            }
            else{
                if(stateController.getStateByNum(currentState).getDefaultFunc().equals("exit")){ //nezr
                    this.isExit = true;
                    currentState = (int) stack.pop();
                    states += "," + String.valueOf(currentState);
//                    configurationView.addConfiguration(new Configuration(inputLexem, states, stackString));
                    continue;
                }
                else if(stateController.getStateByNum(currentState).getDefaultFunc().equals("err")){
                    throw new Exception("Error in state "+currentState+ " before lexem "+lexem.getName());
                }
                else {
                    String str = stateController.getStateByNum(currentState).getDefaultFunc();
                    currentState =Integer.parseInt(str.substring(str.indexOf('<') + 1, str.indexOf('>')));
                    int toStack =  Integer.parseInt(str.substring(str.indexOf(',')+1));
                    stack.push(toStack);
                    stackString = stack.toString();
                    continue;
//                    stack.push(stateController.getStates().get(i).getStack());
//                    currentState = stateController.getStates().get(i).getBeta();
                }
            }

        }
        if(stack.empty() && currentState==5) {
            System.out.println("Well done");
        }
        else throw new Exception("You have not finished programm");
    }

    private boolean hasState(State state, String name){
        ArrayList<Instruction> instructions = state.getInstructions();

        for (Lexem lexem: this.lexems.getLexems() ) {
            if(lexem.getName().equals(name) && lexem.getCode()==100) {name="id"; break;}
            if(lexem.getName().equals(name) && lexem.getCode()==101) {name="con"; break;}
        }

        for (Instruction instruction : instructions ) {
            if(instruction.getMark().equals(name)) return true;
        }


        return false;
    }

    public Instruction getInstruction(State state, String name){
        ArrayList<Instruction> instructions = state.getInstructions();
        int i=0;

        for (Lexem lexem: this.lexems.getLexems() ) {
            if(lexem.getName().equals(name) && lexem.getCode()==100) {name="id"; break;}
            if(lexem.getName().equals(name) && lexem.getCode()==101) {name="con"; break;}
        }

        for (Instruction instruction : instructions ) {
            i++;
            if(instruction.getMark().equals(name)) return instruction;
        }
        return state.getInstructions().get(0);
    }

}
