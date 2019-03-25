package compiler.analyzers;

import compiler.extensions.*;

import java.io.*;


public class Parser {
    private int state;
    private Checker checker;
    private String lexem;
    private boolean hasToRead;
    private boolean EOF;
    private LexemsTable lexemsTable;
    private IdentificatorsTable identificatorsTable;
    private ConstantsTable constantsTable;
    private InputLexemTable inputLexemTable;

    public Parser() {
        this.lexemsTable = new LexemsTable();
        this.identificatorsTable = new IdentificatorsTable();
        this.constantsTable = new ConstantsTable();
        this.inputLexemTable = new InputLexemTable();
        this.state = 1;
        this.checker = new Checker();
        this.hasToRead = true;
        this.EOF = false;
        this.lexem = "";
    }

    



    public void parse(BufferedReader reader) throws Exception {
        boolean definition = true;

        char symbol = ' ';
        this.state = 1;
        int line = 1;
        while (!this.EOF) {
            if(this.hasToRead) {
                int c = 0;
                try {
                    c = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(c == -1) {this.EOF = false;break;}
                 symbol = (char) c;
            }

            switch(this.state){
                case 1:
                    if(!this.hasToRead){
                        this.lexem = "";
                        this.hasToRead = true;
                    }
                    if(checker.isWhiteDelimiter(symbol)){
                        continue;
                    }

                    if(checker.isLetter(symbol)) {
                        this.lexem += Character.toString(symbol);
                        this.state = 2;
                        continue;
                    }
                    else if(checker.isNumber(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 3;
                        continue;
                    }
                    else if(checker.isDot(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 6;
                    }
                    else if(checker.isOneCharDelimiter(symbol)){
                        if(checker.isPlusOrMinus(symbol) && !(this.lexemsTable.getLexems().get(this.lexemsTable.getLexems().size()-1).getName().equals(")") ||  this.lexemsTable.lastLexemType().equals("IDN") || this.lexemsTable.lastLexemType().equals("CON"))){
                           this.lexem += Character.toString(symbol);
                           this.state = 4;
                           continue;
                        }
                        if(checker.isNewLine(symbol)){
                            if(symbol == '\n') {
                                this.lexem += Character.toString('¶');
                                Lexem newLex = new Lexem(this.lexem, "OneCharDelimiter", line);
                                newLex.setCode(this.inputLexemTable.getCode(newLex));
                                this.lexemsTable.addLexem(newLex);
                                line++;
                                this.lexem = "";
                            }
                            continue;
                        }
                        this.lexem += Character.toString(symbol);
                        Lexem newLex = new Lexem(this.lexem, "OneCharDelimiter", line);
                        if(this.inputLexemTable.isContain(newLex)) {
                            newLex.setCode(this.inputLexemTable.getCode(newLex));
                            this.lexemsTable.addLexem(newLex);
                            this.lexem = "";
                        }
                        else throw new Exception("Not defined in lexems table");
                    }
                    else if(checker.isLessMark(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 7;
                        continue;
                    }
                    else if(checker.isGreatMark(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 8;
                        continue;
                    }
                    else if(checker.isEqualMark(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 9;
                        continue;
                    }
                    else if(checker.isNoMark(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 10;
                        continue;
                    }
                    else {
                        this.EOF = true;
                        throw new Exception("Undefined symbol " + symbol + " Line: " + line);
                    }
                    break;

                case 2:
                    if(checker.isNumber(symbol) || checker.isLetter(symbol)){
                        this.lexem += Character.toString(symbol);
                        Lexem newLex = new Lexem(this.lexem, "keyword", line);
                        if(this.inputLexemTable.isContain(newLex) ){
                            newLex.setCode(this.inputLexemTable.getCode(newLex));
                            this.lexemsTable.addLexem(newLex);
                            this.lexem = "";
                            this.state = 1;
                        }
                    }
                    else {
                        Lexem newLex = new Lexem(this.lexem, "IDN", line);
//                        if( this.lexemsTable.isDeclaration( newLex ) ) {
                        if( definition ) {
                            String type = this.lexemsTable.getFirstLineLexem(line);
                            newLex.setIdType(type);
                            if (!this.identificatorsTable.isDefined(newLex)) {
                                this.identificatorsTable.addIdentificator(newLex);
                            }
                            else {
                                throw new Exception("Variable " + this.lexem + "is already defined(line " + line + " )");
                            }
                        }
                        else{
                            if (!this.identificatorsTable.isDefined(newLex)) {
//                                throw new Exception("Variable " + this.lexem + " is not defined");
                            }
                        }
                        newLex.setCode(100);
                        newLex.setIDNCode(this.identificatorsTable.getNumberOfLexem(newLex));
                        this.lexemsTable.addLexem(newLex);
                        this.hasToRead = false;
                        this.state = 1;
                    }
                    break;

                case 3:
                    if(checker.isNumber(symbol)){
                        this.lexem += Character.toString(symbol);
                    }
                    else if(checker.isDot(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 5;
                    }
                    else{
                        newConstant(line);
                    }
                    break;

                case 4:
                    if(checker.isNumber(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 3;
                    }
                    else if(checker.isDot(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state = 6;
                    }
                    else{
                        throw new Exception("Unexpected symbol after " + this.lexem + " on line " + line);
                    }
                    break;

                case 5:
                    if(checker.isNumber(symbol)){
                        this.lexem += Character.toString(symbol);
                    }
                    else{
                        newConstant(line);
                    }
                    break;

                case 6:
                    if(checker.isNumber(symbol)){
                        this.lexem += Character.toString(symbol);
                        this.state =5;
                    }
                    else{
                        newConstant(line);
                    }
                    break;

                case 7:
                    Lexem newLex = new Lexem(this.lexem, "Delimiter", line);
                    if(checker.isLessMark(symbol) || checker.isEqualMark(symbol)){
                        newDelimiter(symbol, line);
                    }
                    else{
                        if(this.inputLexemTable.isContain(newLex)) {
                            newLex.setCode(this.inputLexemTable.getCode(newLex));
                            this.lexemsTable.addLexem(newLex);
                        }
                        else throw new Exception("Not declarated in lexems table");
                        this.hasToRead = false;
                        this.state = 1;
                    }
                    break;

                case 8:
                     newLex = new Lexem(this.lexem, "Delimiter", line);
                    if(checker.isGreatMark(symbol) || checker.isEqualMark(symbol)){
                        newDelimiter(symbol, line);
                    }
                    else{
                        if(this.inputLexemTable.isContain(newLex)) {
                            newLex.setCode(this.inputLexemTable.getCode(newLex));
                            this.lexemsTable.addLexem(newLex);
                        }
                        else throw new Exception("Not declarated in lexems table");
                        this.hasToRead = false;
                        this.state = 1;
                    }
                    break;

                case 9:
                    newLex = new Lexem(this.lexem, "Delimiter", line);
                    if(checker.isEqualMark(symbol)){
                        newDelimiter(symbol, line);
                    }
                    else{

                        if(this.inputLexemTable.isContain(newLex)) {
                            newLex.setCode(this.inputLexemTable.getCode(newLex));
                            this.lexemsTable.addLexem(newLex);
                        }
                        else throw new Exception("Not declarated in lexems table");

                        this.hasToRead = false;
                        this.state = 1;
                    }
                    break;

                case 10:
                    newLex = new Lexem(this.lexem, "Delimiter", line);
                    if(checker.isEqualMark(symbol)){
                        this.lexem += Character.toString(symbol);

                        newLex = new Lexem(this.lexem, "Delimiter", line);
                        if(this.inputLexemTable.isContain(newLex)) {
                            newLex.setCode(this.inputLexemTable.getCode(newLex));
                            this.lexemsTable.addLexem(newLex);
                        }
                        else throw new Exception("Not declarated in lexems table: " + this.lexem);


                        this.lexem = "";
                        this.state = 1;
                    }
                    else{

                        if(this.inputLexemTable.isContain(newLex)) {
                            newLex.setCode(this.inputLexemTable.getCode(newLex));
                            this.lexemsTable.addLexem(newLex);
                        }
                        else throw new Exception("Excepted '=' on line "+ line);

                        this.hasToRead = false;
                        this.state = 1;
                    }
                    break;

                  default:
                      break;
            }

            if(symbol == '{') definition = false;

        }


    }

    private void newDelimiter(char symbol, int line) throws Exception {
        Lexem newLex;
        this.lexem += Character.toString(symbol);
        newLex = new Lexem(this.lexem, "Delimiter", line);
        if(this.inputLexemTable.isContain(newLex)) {
            newLex.setCode(this.inputLexemTable.getCode(newLex));
            this.lexemsTable.addLexem(newLex);
        }
        else throw new Exception("Not declarated in lexems table");
        this.lexem = "";
        this.state = 1;
    }

    private void newConstant(int line) throws Exception {
        Lexem newLex = new Lexem(this.lexem, "CON", line);
        this.constantsTable.constantType(newLex);
        this.constantsTable.addConstant(newLex);
        newLex.setCONCode(this.constantsTable.getNumberOfLexem(newLex));
        newLex.setCode(101);
        this.lexemsTable.addLexem(newLex);
        this.hasToRead = false;
        this.state = 1;
    }

    public LexemsTable getLexemsTable() {
        return lexemsTable;
    }

    public IdentificatorsTable getIdentificatorsTable() {
        return identificatorsTable;
    }

    public ConstantsTable getConstantsTable() {
        return constantsTable;
    }

    public InputLexemTable getInputLexemTable() {
        return inputLexemTable;
    }
}
