package compiler;

import compiler.analyzers.Parser;
import compiler.analyzers.*;
import compiler.extensions.IdentificatorsTable;
import compiler.extensions.PriorityTable;
import compiler.views.MyFrame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;

public class Controller {
    private Parser parser;
    private MyFrame frame;
    private SyntaxAnalyzer2 syntaxAnalyzer2;


    public Controller() {
        this.frame = new MyFrame(this);

    }

    public Controller(MyFrame frame) {
        this.parser = new Parser();
        this.frame = frame;
    }

    public Parser getParser() {
        return parser;
    }




    public void execute(){
        frame.input();
    }

    public void analyzeFile(String path){
        this.parser = new Parser();
        try {
            this.parser.parse(new BufferedReader(new FileReader(new File(path))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.analyzeSyntax();
        this.makePoliz(this.parser.getIdentificatorsTable());
    }
    public void analyzeText(String text){
        this.parser = new Parser();
        try {
            this.parser.parse(new BufferedReader(new StringReader(text)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.analyzeSyntax();
        this.makePoliz(this.parser.getIdentificatorsTable());
    }

    private void analyzeSyntax(){
        this.syntaxAnalyzer2 = new SyntaxAnalyzer2(this.getParser().getLexemsTable());
        try {
            syntaxAnalyzer2.analyze();
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.show(this.getParser().getLexemsTable().getLexems(), this.getParser().getIdentificatorsTable().getLexems(), this.getParser().getConstantsTable().getLexems(),syntaxAnalyzer2.getConfigurationView(),syntaxAnalyzer2.getStateController().getStates());

    }

    private void makePoliz(IdentificatorsTable identificatorsTable){
     PolizCreator polizCreator = new PolizCreator(this.getParser().getLexemsTable(),new PriorityTable(), this.frame);
     polizCreator.run();
        ArrayList<String> plz =  polizCreator.getPolizAsArray();
        PolizHandler handler = new PolizHandler(plz, identificatorsTable, this.frame);
        handler.calculatePoliz();
    }

}
