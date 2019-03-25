package compiler.extensions;


import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class StateController extends ArrayList<State> {
    private ArrayList<State> states;


    public StateController(){
        this.states = new ArrayList<>();
    }

    public void newState(int number, ArrayList<Instruction> instructions, String func){
        this.states.add(new State(number, instructions, func));
    }

   public void createStates() throws FileNotFoundException {
       Gson gson = new Gson();
       this.states = gson.fromJson(new FileReader("src/config.json"),StateController.class);
       System.out.println("OK");
   }

    public ArrayList<State> getStates() {
        return states;
    }

    public State getStateByNum(int number){
        for (State state : this.getStates()   ) {
            if(state.getNumber() == number) return state;
        }
        return null;
    }
}
