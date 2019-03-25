package compiler.extensions;

import java.util.HashMap;
import java.util.Map;

public class PriorityTable {
    private Map<String,Integer> priorities;

    public PriorityTable() {
        this.priorities = new HashMap<String,Integer>();
        this.createPriorities();
    }

    private void createPriorities() {
        this.setPriorities("for",0);
        this.setPriorities("(",0);
        this.setPriorities("[",0);
        this.setPriorities("if",0);

        this.setPriorities(")",1);
        this.setPriorities("]",1);
        this.setPriorities("fi",1);
        this.setPriorities("then",1);
        this.setPriorities("rof",1);
        this.setPriorities("rof",1);
        this.setPriorities("by",1);
        this.setPriorities("to",1);
        this.setPriorities("do",1);
        this.setPriorities("cout",1);
        this.setPriorities("cin",1);
//        this.setPriorities("cin",1);
        this.setPriorities("¶",1);

        this.setPriorities("=",2);

        this.setPriorities("OR",3);
        this.setPriorities("AND",4);
        this.setPriorities("NOT",5);

        this.setPriorities("<",6);
        this.setPriorities("<=",6);
        this.setPriorities(">=",6);
        this.setPriorities(">",6);
        this.setPriorities("==",6);

        this.setPriorities("+",7);
        this.setPriorities("-",7);

        this.setPriorities("*",8);
        this.setPriorities("/",8);

    }


    public void setPriorities(String key, int value) {
        this.priorities.put(key,value);
    }


}
