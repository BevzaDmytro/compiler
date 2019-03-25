package compiler.extensions;

public class Configuration {
    private String inputLexem;
    private String states;
    private String stack;


    public Configuration(String inputLexem, String states, String stack) {
        this.inputLexem = inputLexem;
        this.states = states;
        this.stack = stack;
    }

    public String getInputLexem() {
        return inputLexem;
    }

    public String getStates() {
        return states;
    }

    public String getStack() {
        return stack;
    }
}
