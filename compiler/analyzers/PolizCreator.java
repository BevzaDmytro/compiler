package compiler.analyzers;

import compiler.extensions.Lexem;
import compiler.extensions.LexemsTable;
import compiler.extensions.PriorityTable;
import compiler.views.MyFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class PolizCreator {
    private LexemsTable lexems;
    private PriorityTable priorityTable;
    private Stack<String> stack;
    private ArrayList<String> poliz;
    private ArrayList<String> labels;
    private int labelNum = 1;
    private Stack<String> labelsStack;
    private Stack<String> r;
    private int rCounter = 1;
    private Stack <String> cycleCounters;
    private boolean cycle = false;
    private MyFrame frame;
    private String ternExp;
    private boolean tern;

    public PolizCreator(LexemsTable lexems, PriorityTable priorityTable) {
        this.lexems = lexems;
        this.priorityTable = priorityTable;
        this.stack = new Stack<>();
        this.poliz = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.labelsStack =  new Stack<>();
        this.r =  new Stack<>();
        this.cycleCounters =  new Stack<>();
    }

    public PolizCreator(LexemsTable lexemsTable, PriorityTable priorityTable, MyFrame frame) {
        this.lexems = lexemsTable;
        this.priorityTable = priorityTable;
        this.stack = new Stack<>();
        this.poliz = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.labelsStack =  new Stack<>();
        this.r =  new Stack<>();
        this.cycleCounters =  new Stack<>();
        this.frame = frame;
    }


    public ArrayList<String> getPolizAsArray(){

        ArrayList<String> listOfStrings = new ArrayList<String>();

        Collections.addAll(listOfStrings, this.getPolizAsString().split(" +"));
        return listOfStrings;
    }

    public void run(){
        //ідентифікатори і константи від входу на вихід
        //якщо пріоритет операції в стеку >= за пріоритет вхідної, операція зі стеку на вихід, і так поки в стеку не буде операція з меншим пріоритетом
        //якщо стек порожній, операція в стек
        //якщо ланцюжок порожній, всі операції зі стеку по черзі на вихід за ознакою кінця

        for (Lexem currentLexem : this.lexems.getLexems() ) {
            if(this.lexemOnDelete(currentLexem.getName())){

                continue;
            }
            if(this.checkIdOrConst(currentLexem)){
                if(cycle) {
                    this.cycleCounters.push(currentLexem.getName());
                    this.cycle = false;
                }
                this.poliz.add(currentLexem.getName());

                continue;
            }
//            else if(this.checkIfOperation(currentLexem.getName())){
            else {
                if(stack.empty()){
                    this.pushOperationIntoStack(currentLexem.getName());
//                    stack.push(currentLexem.getName());
                }
                else{
                    while (this.lastStackOperationHasLessPriority(currentLexem.getName(),stack.peek())){
//                        this.poliz.add(stack.pop());
                        this.makePolizFromOperation();
                        if (stack.empty()) break;
                    }
//                    stack.push(currentLexem.getName());
                    this.pushOperationIntoStack(currentLexem.getName());
                }
            }
        }
        if(!stack.empty()){
            while (!stack.empty())
                this.poliz.add(stack.pop());
        }

        String polizResult = this.getPolizAsString();
//        this.frame.setStatus("Poliz: \n" +polizResult);
        System.out.println(polizResult);
    }

    private String getPolizAsString(){
        StringBuilder plz = new StringBuilder();
        for (String elem:this.poliz      ) {
            plz.append(elem).append(" ");
        }
        return plz.toString();
    }

    private boolean lexemOnDelete(String name) {
        return /*name.equals("{") || name.equals("}") ||*/ name.equals(",")/* || name.equals("¶")*/;
    }

    private void pushOperationIntoStack(String name) {
        if(name.equals("{") || name.equals("}")) return;
        if(name.equals("<<") || name.equals(">>")) return;

        if(this.isKeyword(name)){
            //по ключовим словам генеруємо мітки,поліз
            if(isConditional(name)){
                this.makePolizFromConditional(name);
            }
            else if(isCycle(name)){
                this.makePolizFromCycle(name);
            }
            else if(isTern(name)){
                this.makePolizFromTern(name);
            }
            else{
                this.stack.push(name);
            }

        }
        else{
            if(name.equals("¶")){
                if(tern){
                    this.poliz.add("=");
                    this.poliz.add(this.labelsStack.pop() +": ");
                    tern = false;
                }
                return;
            }
            this.stack.push(name);
        }
    }

    private boolean isTern(String name) {
        return name.equals("@") || name.equals("?") || name.equals(":");
    }

    private void makePolizFromCycle(String name) {
        if(name.equals("for")){
            //генерує три мітки
            for(int i =0; i<3;i++) {
                this.labelsStack.push("m" + this.labelNum);
                this.labelNum++;
            }
            this.stack.push(name);
            this.cycle = true;
        }
        if(name.equals("by")){
            //генерує робочі комірки
            // Rj 1 = Mi : Rj+1
            String m3 = this.labelsStack.pop();
            String m2 = this.labelsStack.pop();
            String toPoliz = "r" + this.rCounter + " 1 = " + this.labelsStack.peek() + ": ";
            this.r.push("r"+this.rCounter);
            this.rCounter++;
            this.r.push("r"+this.rCounter);
            toPoliz += "r"+this.rCounter;
            this.rCounter++;

            this.poliz.add(toPoliz);
            this.labelsStack.push(m2);
            this.labelsStack.push(m3);
        }
        if(name.equals("to")){
            // = Rj 0 == Mi+1 УПХ k k Rj+1 + = Mi+1: Rj 0 =  k (k - лічильник циклу)

            String r2 = this.r.pop();
            String m3 = this.labelsStack.pop();
            String toPoliz = "= " + r.peek() + " 0 == " + this.labelsStack.peek() + "УПХ " + this.cycleCounters.peek() + " " + this.cycleCounters.peek()+" " + r2 + " + = " + this.labelsStack.peek() +
                    ": " + this.r.peek() + " 0 = " + this.cycleCounters.peek();
            this.poliz.add(toPoliz);

            this.labelsStack.push(m3);
            this.r.push(r2);
        }
        if(name.equals("do")){
            //  - Rj+1 * 0 <= Mi+2 УПХ
            String toPoliz = " - " +this.r.peek() + " * 0 <= " + this.labelsStack.peek() + "УПХ ";
            this.poliz.add(toPoliz);
        }
        if(name.equals("rof")){
            // Mi БП Mi+2:
            String m3 = this.labelsStack.pop();
            String m2 = this.labelsStack.pop();
            String toPoliz = this.labelsStack.pop() + "БП " + m3+": ";
            this.poliz.add(toPoliz);
            this.r.pop();
            this.r.pop();
            this.stack.pop();
        }
    }

    private boolean isCycle(String name) {
        return name.equals("for") || name.equals("by") || name.equals("to") || name.equals("do")|| name.equals("rof");
    }

    private boolean isKeyword(String name) {
        return name.equals("for") || name.equals("to") || name.equals("by") || name.equals("do")|| name.equals("while")|| name.equals("rof") || name.equals("if") || name.equals("then") || name.equals("fi") || name.equals("cout") || name.equals("cin") || name.equals("@") || name.equals("?") || name.equals(":");
    }

    private void makePolizFromOperation() {
        String possiblePoliz = stack.pop();
        if(!isKeyword(possiblePoliz)) {
            this.poliz.add(possiblePoliz);
        }
        else if (possiblePoliz.equals("cout")){
            this.poliz.add("PRINT");
        }
        else if (possiblePoliz.equals("cin")){
            this.poliz.add("INPUT");
        }


        //перевірка на цикд
        //перевірка на тернарний оператор
        //внесення в поліз
    }

    private void makePolizFromConditional(String name) {
        // if a > b then a = b fi => ab> m1 УПХ ab= m1:
        if(name.equals("if")){
//            this.poliz.add("");
//            this.stack.push(name + " m"+this.labelNum );
            this.stack.push(name  );
            this.labels.add("m"+this.labelNum);
            this.labelsStack.push("m"+this.labelNum);
            this.labelNum++;
        }
        if(name.equals("then")){
            this.poliz.add(this.labelsStack.peek() + "УПХ");
        }
        if(name.equals("fi")){
            this.poliz.add(this.labelsStack.pop() + ": ");
            this.stack.pop();
        }
    }

    private void makePolizFromTern(String name) {

        if(name.equals("@")){
            this.stack.push(name  );
            this.labels.add("m"+this.labelNum);
            this.labelsStack.push("m"+this.labelNum);
            this.labelNum++;


             ternExp = this.poliz.get(this.poliz.size()-2) ;
             this.poliz.remove(this.poliz.size()-1);
             this.poliz.remove(this.poliz.size()-1);
        }
        if(name.equals("?")){
            this.poliz.add(this.labelsStack.peek() + "УПХ");
            this.poliz.add(ternExp);
        }
        if(name.equals(":")){
            this.labels.add("m"+this.labelNum);
            this.labelsStack.push("m"+this.labelNum);
            this.labelNum++;

            this.tern = true;
            this.poliz.add("=");

            this.poliz.add(this.labelsStack.peek() + "БП");

            String label = this.labelsStack.pop();

            this.stack.pop();
            this.poliz.add(this.labelsStack.pop() +": ");
            this.poliz.add(ternExp);
            this.labelsStack.push(label);
        }
    }

    private boolean isConditional(String pop) {
        return pop.equals("if") || pop.equals("then") || pop.equals("fi");
    }

    private boolean lastStackOperationHasLessPriority(String name, String peek) {
        return this.priorityTable.getPriorities().get(name) < this.priorityTable.getPriorities().get(peek);

    }


    private boolean checkIdOrConst(Lexem lexem) {
        return lexem.getCode() == 100 || lexem.getCode() == 101;
    }

}
