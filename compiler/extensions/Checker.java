package compiler.extensions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {

    private char symbol;

    public boolean isWhiteDelimiter(char ch){
        return (ch == ' ');
    }

    public boolean isEnter(char ch){
        return (ch == '\n');
    }

    public boolean isLetter(char ch){
        return ( (ch >= 'a' && ch <= 'z') || (ch >='A' && ch <= 'Z') );
    }

    public boolean isNumber(char ch){
        return ( (ch >= '0' && ch <= '9'));
    }

    public boolean isDot(char ch){
        return (ch == '.');
    }

    public boolean isOneCharDelimiter(char ch){
        return (ch == '@' || ch == '(' || ch == ')' || ch == ',' || ch =='{' || ch == '}' || ch == '*' || ch == '/' || ch == '+' || ch == '-' || ch == ':' || ch == '?' || ch =='\n' || ch=='\r' || ch == '[' || ch == ']');
    }
    public boolean isNewLine(char ch){
        return (ch == '\r' || ch == '\n');
    }
    public boolean isPossibleDoubleSeparator(char ch){
        return (ch == '<' || ch == '>' || ch == '=');
    }

    public boolean isPlusOrMinus(char ch){
        return (ch == '+' || ch == '-');
    }

    public boolean isLessMark(char ch){
        return ( ch == '<' );
    }

    public boolean isGreatMark(char ch){
        return ( ch == '>' );
    }
    public boolean isEqualMark(char ch){
        return ( ch == '=' );
    }

    public boolean isNoMark(char ch){
        return ( ch == '!' );
    }

    public boolean isDoubleSeparator(String string){
        return ( string.equals(">>") || string.equals("<<") || string.equals(">=") || string.equals("<=") || string.equals("==") || string.equals("!="));
    }

    public boolean isKeyword(String string){
        return (string.equals("int") || string.equals("float") || string.equals("for") || string.equals("rof") ||
                string.equals("by") ||  string.equals("do") ||  string.equals("to") ||  string.equals("if") ||
                string.equals("then") ||  string.equals("fi") || string.equals("cin") ||  string.equals("cout")
                ||  string.equals("OR") || string.equals("AND") ||  string.equals("NOT"));
    }
}
