package compiler.extensions;

import java.util.ArrayList;
import java.util.HashMap;

public class InputLexemTable {
    private HashMap<Integer, String> lexems;

    public InputLexemTable() {
        this.lexems = new HashMap<>();
        this.lexems.put(1,"int");
        this.lexems.put(2,"float");
        this.lexems.put(3,"for");
        this.lexems.put(4,"rof");
        this.lexems.put(5,"by");
        this.lexems.put(6,"to");
        this.lexems.put(7,"do");
        this.lexems.put(8,"if");
        this.lexems.put(9, "then");
        this.lexems.put(10, "fi");
        this.lexems.put(11, "cin");
        this.lexems.put(12, "cout");
        this.lexems.put(13, "{");
        this.lexems.put(14, "}");
        this.lexems.put(15, "(");
        this.lexems.put(16, ")");
        this.lexems.put(17, "=");
        this.lexems.put(18, "<<");
        this.lexems.put(19, ">>");
        this.lexems.put(20, ">");
        this.lexems.put(21, "<");
        this.lexems.put(22, ">=");
        this.lexems.put(23, "<=");
        this.lexems.put(24, "==");
        this.lexems.put(25, "!=");
        this.lexems.put(26, "?");
        this.lexems.put(27, ":");
        this.lexems.put(28, ",");
        this.lexems.put(29, "+");
        this.lexems.put(30, "-");
        this.lexems.put(31, "*");
        this.lexems.put(32, "/");
        this.lexems.put(33, "\n");
        this.lexems.put(34, "AND");
        this.lexems.put(35, "OR");
        this.lexems.put(36, "NOT");
        this.lexems.put(37, "[");
        this.lexems.put(38, "]");
        this.lexems.put(39, "@");
        this.lexems.put(100, "IDN");
        this.lexems.put(101,"101");


//        ArrayList arrayList = new ArrayList("int","float","for","rof","by","to","do","if","then","fi","cin","cout",);




    }

    public boolean isContain(Lexem lexem){
//        for (String curLex: this.lexems ) {
        boolean flag = false;
        for (HashMap.Entry<Integer, String> pair: this.lexems.entrySet()) {
            if(lexem.getName().equals(pair.getValue())) return true; //lexems.indexOf(curLex)
        };
        return false;
    }

    public int getCode(Lexem lexem){
        int code = 0;

        for (HashMap.Entry<Integer, String> pair: this.lexems.entrySet()) {
            if(lexem.getName().equals(pair.getValue())) code = pair.getKey();
        };
        return code;
    }
}
