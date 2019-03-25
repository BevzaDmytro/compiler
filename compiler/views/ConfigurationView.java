package compiler.views;

import compiler.extensions.Configuration;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ConfigurationView extends AbstractTableModel {
    private ArrayList<Configuration> configurations;

    public ConfigurationView(){
        super();
        this.configurations = new ArrayList<>();
    }

    public void addConfiguration(Configuration configuration){
        this.configurations.add(configuration);
    }

    @Override
    public int getRowCount() {
        return this.configurations.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return rowIndex+1;

            case 1:
                return this.configurations.get(rowIndex).getInputLexem();

            case 2:
                return this.configurations.get(rowIndex).getStates();
            case 3:
                return this.configurations.get(rowIndex).getStack();
        }
        return null;
    }
}
