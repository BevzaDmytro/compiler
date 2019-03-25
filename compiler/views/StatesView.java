package compiler.views;

import compiler.extensions.Configuration;
import compiler.extensions.Instruction;
import compiler.extensions.State;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class StatesView extends AbstractTableModel {
    private ArrayList<State> states;

    public StatesView(ArrayList<State> states){
        super();
        this.states = states;
    }



    @Override
    public int getRowCount() {
        return this.states.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String str;
        switch (columnIndex){
            case 0:
                return this.states.get(rowIndex).getNumber();

            case 1:
                str = "<html>";
                for (Instruction instruction: this.states.get(rowIndex).getInstructions()    ) {
                    str += instruction.getMark() + "<br>";
                }
                return str;

            case 2:
                str = "<html>";
                for (Instruction instruction: this.states.get(rowIndex).getInstructions()    ) {
                    str += instruction.getBeta()!=0 ? String.valueOf( instruction.getBeta()) : ' ';
                    str += "<br>";
                }
                return str+"<html>";

            case 3:
                str = "<html>";
                for (Instruction instruction: this.states.get(rowIndex).getInstructions()    ) {
                    str += instruction.getStackNum()!=0 ? String.valueOf( instruction.getStackNum()) : ' ';
                            str += "<br>";
                }
                return str;

            case 4:
                str = "<html>";
                for (Instruction instruction: this.states.get(rowIndex).getInstructions()    ) {
                    str +=  instruction.getFunc()!=null ? "[=]"+instruction.getFunc() :" ";
                    str+=  "<br>";
                }
                return str+"[!=]"+this.states.get(rowIndex).getDefaultFunc();

        }
        return null;
    }
}
