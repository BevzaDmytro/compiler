package compiler.extensions;



import java.util.ArrayList;

public class ConstantsTable {
    private ArrayList<Lexem> lexems;


    public ConstantsTable() {
        this.lexems = new ArrayList<>();
    }

    public void addConstant(Lexem lexem){
        this.lexems.add(lexem);
    }

    public int getNumberOfLexem(Lexem lexem){
        int i = 0;
        for (Lexem lex : this.lexems ) {
            if(lex.getName().equals(lexem.getName())) return i+1;
            i++;
        }
        return i;
    }
    public void constantType(Lexem lexem){
        if(lexem.getName().contains(".")) lexem.setIdType("float");
        else lexem.setIdType("int");
    }

    public ArrayList<Lexem> getLexems() {
        return lexems;
    }


}
