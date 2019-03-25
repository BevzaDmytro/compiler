package compiler.views;

import compiler.extensions.Lexem;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ShowIDNTable extends AbstractTableModel {

    private ArrayList<Lexem> lexems;

    ShowIDNTable(ArrayList<Lexem> lexems) {
        super();
        this.lexems = lexems;
    }

    public int getRowCount() {
        return this.lexems.size();
    }
    @Override
    public int getColumnCount() {
        return 3;
    }
    public Object getValueAt(int r, int c) {
        switch (c) {
            case 0:
                return r;
            case 1:
                return this.lexems.get(r).getName();
            case 2:
                return this.lexems.get(r).getIdType();

            default:
                return "Other Column";
        }
    }

    @Override
    public String getColumnName(int c) {
        switch (c) {
            case 0:
                return "No";
            case 1:
                return "Name";
            case 2:
                return "Type";

            default:
                return "Other Column";
        }
    }

}