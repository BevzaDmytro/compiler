package compiler.analyzers;

import compiler.extensions.Lexem;
import compiler.extensions.LexemsTable;
import compiler.extensions.PriorityTable;

import java.util.ArrayList;
import java.util.Stack;

public class PolizCreator {
    private LexemsTable lexems;
    private PriorityTable priorityTable;
    private Stack<String> stack;
    private ArrayList<String> poliz;

    public PolizCreator(LexemsTable lexems, PriorityTable priorityTable) {
        this.lexems = lexems;
        this.priorityTable = priorityTable;
        this.stack = new Stack<>();
        this.poliz = new ArrayList<>();
    }

    public void run(){
        //ідентифікатори і константи від входу на вихід
        //якщо пріоритет операції в стеку >= за пріоритет вхідної, операція зі стеку на вихід, і так поки в стеку не буде операція з меншим пріоритетом
        //якщо стек порожній, операція в стек
        //якщо ланцюжок порожній, всі операції зі стеку по черзі на вихід за ознакою кінця

        for (Lexem currentLexem : this.lexems.getLexems() ) {
            if(this.checkIdOrConst(currentLexem.getName())){
                this.poliz.add(currentLexem.getName());
                continue;
            }
            else if(this.checkIfOperation(currentLexem.getName())){
                if(stack.empty()){
//                    this.pushOperationIntoStack(currentLexem.getName());
                    stack.push(currentLexem.getName());
                }
                else{
                    while (this.lastStackOperationHasLessPriority(currentLexem.getName(),stack.peek())){
//                        this.poliz.add(stack.pop());
                        this.makePolizFromOperation(stack.pop());
                    }
                    stack.push(currentLexem.getName());
//                    this.pushOperationIntoStack(currentLexem.getName());
                }
            }
        }
        if(!stack.empty()){
            while (!stack.empty())
                this.poliz.add(stack.pop());
        }
    }

    private void makePolizFromOperation(String pop) {
        //перевірка на умовний оператор
        //перевірка на цикд
        //перевірка на тернарний оператор
        //внесення в поліз
    }

    private boolean lastStackOperationHasLessPriority(String name, String peek) {
        return false;
    }

    private boolean checkIfOperation(String name) {
        return false;
    }

    private boolean checkIdOrConst(String name) {
        return true;
    }

}
