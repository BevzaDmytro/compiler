package compiler.extensions;

import compiler.extensions.Lexem;

import java.util.ArrayList;

public class IdentificatorsTable {
    private ArrayList<Lexem> lexems;


    public ArrayList<Lexem> getLexems() {
        return lexems;
    }

    public IdentificatorsTable() {
        this.lexems = new ArrayList<>(1);
    }

    public void addIdentificator(Lexem lexem){
        this.lexems.add(lexem);
    }

    public boolean isDefined(Lexem lexem){
        for ( Lexem currentLex : this.lexems ) {
            if(currentLex.getName().equals(lexem.getName())) return  true;
        }
        return false;
    }

    public int getNumberOfLexem(Lexem lexem){
        int i = 0;
        for (Lexem lex : this.lexems ) {
            if(lex.getName().equals(lexem.getName())) return i+1;
            i++;
        }
        return i;
    }



}
