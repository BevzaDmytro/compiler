package compiler.analyzers;

import compiler.extensions.IdentificatorsTable;
import compiler.views.MyFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;



public class PolizHandler {
    private ArrayList<String> poliz;
    private Stack<String> polizResult;
    private IdentificatorsTable identificatorsTable;
    private MyFrame frame;
    private Map<String, Double> helpers;


    private Map<String, Double> variales;
//    private ArrayList<PolizInterpretorTable> table;

//    public ArrayList<PolizInterpretorTable> getTable() {
//        return table;
//    }

    public PolizHandler(ArrayList<String> poliz, IdentificatorsTable identificatorsTable, MyFrame frame) {
        this.poliz = poliz;
        this.variales = new HashMap<>();
        this.helpers = new HashMap<>();
        this.polizResult = new Stack<>();
        this.identificatorsTable = identificatorsTable;
        this.frame = frame;
//        this.table = new ArrayList<>();
    }


    public double calculatePoliz() {
//        JOptionPane jOptionPane = new JOptionPane();
        double result = - 99999;
        String element;
        this.frame.setStatus(" ");

//        ArrayList<String> currentPoliz = new ArrayList<>();
//        currentPoliz.addAll(this.poliz);
//        for (String element : this.poliz) {
        for (int i = 0; i<poliz.size();i++) {
            element = poliz.get(i);
            if(isNotOperand(element))   {
//                if(element.matches("^\\D+$"))
//                    element = this.askIdentificator(element);
                this.polizResult.push(element);
            }
            else {
                if(this.isMathOperation(element)) {

                    String firstOperand;
                    String secondOperand;
                    Double sec = (double) -999;
                    Double first = (double) -999;
//                    Double first;
                    secondOperand = this.polizResult.pop();
                    firstOperand = this.polizResult.pop();

                    first = this.getValueOfVariable(firstOperand);
                    sec = this.getValueOfVariable(secondOperand);

                    Double temp = this.calculateTwoOperands(first, element, sec);
                    this.polizResult.push(temp.toString());
                }
                else if(this.isDeclaration(element)){
                    while (!this.polizResult.empty()){
                        this.variales.put(this.polizResult.pop(), null);
                    }
                }
                else if(element.equals("=")){
                    //записуємо в таблицю ідентифікаторів значення
                    String value = this.polizResult.pop();
                    String var = this.polizResult.pop();
                    if(var.matches("^r\\d+$") && !this.variales.containsKey(var)){
                        if(this.helpers.containsKey(var)){
                            this.helpers.replace(var, Double.valueOf(value));
                        }
                        else {
                            this.helpers.put(var, Double.valueOf(value));
                        }
                        continue;
                    }

                    this.variales.replace(var, Double.valueOf(value));
                }
                else if(this.isLogicalOperation(element)){
                    //без врахування and or not
                    // в якусь змінну записую тру або false, щоб потім по УПХ або бп піти в мітку
                    if(element.equals("OR") || element.equals("AND")){
                        String first = this.polizResult.pop();
                        String second = this.polizResult.pop();
                        if(this.evaluateAndOr(first,element,second)){
                            this.polizResult.push("true");
                        }
                        else{
                            this.polizResult.push("false");
                        }
                    }
                    else if(element.equals("NOT")){
                        String exp = this.polizResult.pop();
                        if(exp.equals("true")){
                            this.polizResult.push("false");
                        }
                        else this.polizResult.push("true");
                    }
                    else {
                        String firstOperand;
                        String secondOperand;
                        Double sec = (double) -999;
                        Double first = (double) -999;
//                    Double first;
                        secondOperand = this.polizResult.pop();
                        firstOperand = this.polizResult.pop();


                        first = this.getValueOfVariable(firstOperand);
                        sec = this.getValueOfVariable(secondOperand);

                        boolean label = this.evaluateLogicalExp(first, element, sec);
//                    Double temp = this.calculateTwoOperands(first, element, sec);
                        if (label)
                            this.polizResult.push("true");
                        else
                            this.polizResult.push("false");
                    }
                }
                else if(this.isMoveToLabel(element)){
                    //перехід по БП або УПХ
                    if(element.contains("УПХ")){
                        element = element.replace("УПХ", "");
                        if(this.polizResult.pop().equals("false")){
                           i = this.goToLabel(element);
                        }

                    }
                    else {
                        element = element.replace("БП", "");

                            i = this.goToLabel(element);

                    }
                }
                else if(this.isInputOrOutput(element)){
                    if(element.equals("PRINT")){
                        StringBuffer onPrint = new StringBuffer("");

                        while (!this.polizResult.empty()){
//                            onPrint += this.polizResult.pop() + " ";
                            String variable = "";
                            if(this.polizResult.peek().matches("^\\D+$")){
                                 variable =  this.variales.get(this.polizResult.pop()).toString();
                            }
                            else{
                                 variable = this.polizResult.pop();
                            }
                            onPrint.insert(0,variable + " ");
                        }

                        this.frame.printMess(onPrint.toString());
                    }
                    else{
                        while (!this.polizResult.empty()){
                            String curEl = this.polizResult.pop();
                            Double val = Double.valueOf(this.askIdentificator(curEl));
                            this.variales.replace(curEl, val);
                        }
                    }
                }
                else if(element.contains("r") && element.matches("^r\\d+$")){
                    this.polizResult.push(element);
                }
                // по присвоєнню, вводу, виводу - звільняти стек
            }
//            currentPoliz.remove(0);
//            this.table.add(new PolizInterpretorTable(this.polizResult.toString(), currentPoliz.toString()));
        }
//        result = Double.valueOf(this.polizResult.pop());
//        this.variales.clear();
//        savePolizSteps();
        return result;
    }

    private boolean evaluateAndOr(String first, String element, String second) {
        if(element.equals("AND")){
            return Boolean.valueOf(first) && Boolean.valueOf(second);
        }
        else
            return Boolean.valueOf(first) || Boolean.valueOf(second);
    }

    private Double getValueOfVariable(String firstOperand) {
        if(firstOperand.matches("^\\D+$")){
            if(this.variales.get(firstOperand) != null){
//                            first = Double.valueOf(firstOperand);
                return this.variales.get(firstOperand);
            }
            else return (double) -999;
        }
        else if(firstOperand.matches("^r\\d+$")){
            if(this.helpers.get(firstOperand) != null){
                return this.helpers.get(firstOperand);
            }
            else return (double) -999;
        }
        else return  Double.valueOf(firstOperand);
    }

    private int goToLabel(String element) {
        for (int i = 0; i<poliz.size();i++) {
            String plz = poliz.get(i);
            if(plz.equals(element+":"))
            return i;
        }
        return -1;
    }


    private boolean evaluateLogicalExp(Double first, String element, Double sec) {
        if(element.equals("<")){
            return first < sec;
        }
        if(element.equals(">")){
            return first > sec;
        }
        if(element.equals("<=")){
            return first <= sec;
        }
        if(element.equals(">=")){
            return first >= sec;
        }
        if(element.equals("==")){
//            return fabs(first – sec) <= 0.00000001;
            return Double.compare(first,sec) == 0;
        }
        return false;
    }

    private boolean isDeclaration(String element) {
        return  element.equals("int") || element.equals("float");
    }

    private boolean isInputOrOutput(String element) {
        return element.equals("PRINT") || element.equals("INPUT");
    }

    private boolean isMoveToLabel(String element) {
        int index = element.lastIndexOf("УПХ");
        int index2 = element.lastIndexOf("БП");
        return !(index == -1 && index2 == -1);
//        return false;
    }

    private boolean isLogicalOperation(String element) {
        return (element.equals(">") ||  element.equals("<") || element.equals(">=") || element.equals("<=") || element.equals("==") || element.equals("OR") || element.equals("AND") || element.equals("NOT"));
    }

    private boolean isMathOperation(String element) {
        return (element.equals("*") ||  element.equals("/") || element.equals("+") || element.equals("-"));
    }

    private String askIdentificator(String element) {
        Double value = (double) 0;

            try {
                value = Double.valueOf(JOptionPane.showInputDialog("Визначте значення параметра " + element));

            } catch (NumberFormatException e) {
                System.err.println("Введіть число!");
            }
            variales.put(element, value);

        return value.toString();
    }

    private double calculateTwoOperands(Double firstOperand, String element, Double secondOperand) {
        switch (element){
            case "*":
                return firstOperand*secondOperand;


            case "/":
                return firstOperand/secondOperand;

            case "+":
                return firstOperand+secondOperand;

            case "-":
                return firstOperand-secondOperand;

        }
        return 0;
    }

    private boolean isNotOperand(String element) {
//        return !(element.equals("*") ||  element.equals("/") || element.equals("+") || element.equals("-") || element.equals("int") || element.equals("float")  || element.equals(">=")  || element.equals(">")  || element.equals("<=") || element.equals("<") || element.equals("==") || element.equals("AND") || element.equals("="));
        return this.identificatorsTable.containsVariable(element) || element.matches("((-|\\\\+)?[0-9]+(\\\\.[0-9]+)?)+");
    }
}
