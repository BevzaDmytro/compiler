package compiler.extensions;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.net.IDN;

public class Lexem {
    private String name;
    private int code;
    private String type;
    private String idType;
    private int line;
    private int IDNCode = 0;
    private int CONCode = 0;
//    private String id;

    public Lexem(String name, String type, int line) {
        this.name = name;
        this.type = type;
        this.line = line;
    }
    public Lexem(String name, String type, int line, int IDNCode, int CONCode) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.IDNCode = IDNCode;
        this.CONCode = CONCode;
    }
    public Lexem(String name, String type, int line , String idType) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.idType = idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setIDNCode(int IDNCode) {
        this.IDNCode = IDNCode;
    }

    public void setCONCode(int CONCode) {
        this.CONCode = CONCode;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getCode() {
        return code;
    }

    public String getIdType() {
        return idType;
    }

    public int getIDNCode() {
        return IDNCode;
    }

    public int getCONCode() {
        return CONCode;
    }
}
