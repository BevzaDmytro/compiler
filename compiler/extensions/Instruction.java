package compiler.extensions;

public class Instruction {
    private String mark;
    private int beta;
    private int stackNum;
    private String  func;

    public Instruction(String mark, int beta, int stackNum, String func) {
        this.mark = mark;
        this.beta = beta;
        this.stackNum = stackNum;
        this.func = func;
    }

    public Instruction(String mark, String func) {
        this.mark = mark;
        this.func = func;
    }

    public Instruction(String mark, int beta, int stackNum) {
        this.mark = mark;
        this.beta = beta;
        this.stackNum = stackNum;
    }

    public Instruction(String mark, int beta) {
        this.mark = mark;
        this.beta = beta;
    }

    public Instruction(String func) {
        this.func = func;
    }

    public String getMark() {
        return mark;
    }

    public int getBeta() {
        return beta;
    }

    public int getStackNum() {
        return stackNum;
    }

    public String getFunc() {
        return func;
    }
}
