package compiler.extensions;


import java.util.ArrayList;

public class LexemsTable  {
    private ArrayList<Lexem> lexems;



    public LexemsTable() {
        this.lexems = new ArrayList<>();
    }

    public void addLexem(Lexem lexem) throws Exception {
            this.lexems.add(lexem);

    }

    public String lastLexemType(){
        return this.lexems.get(this.lexems.size() - 1).getType();
    }

    public boolean isContain(Lexem lexem){
        for ( Lexem currentLex : this.lexems ) {
            if(currentLex.getName().equals(lexem.getName())) return  true;
        }
        return false;
    }

    public String getFirstLineLexem(int line){
        for ( Lexem currentLex : this.lexems  ) {
            if (currentLex.getLine() == line) return currentLex.getName();
        }
        return "";
    }

    public boolean isDeclaration(Lexem lexem){
        int line = lexem.getLine();


        if(this.getFirstLineLexem(line).equals("int") || this.getFirstLineLexem(line).equals("float")) return true;

        return false;
    }


    public ArrayList<Lexem> getLexems() {
        return this.lexems;
    }
}
