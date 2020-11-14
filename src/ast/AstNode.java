package ast;

import Ex1.SymbolTables.SymbolTable;
import javax.xml.bind.annotation.XmlElement;

public abstract class AstNode {
    @XmlElement(required = false)
    public Integer lineNumber;
    public SymbolTable symbolTable;
    public AstNode() {
        lineNumber = null;
        symbolTable = null;
    }

    public AstNode(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    abstract public void accept(Visitor v);

    public void setSymbolTable(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
    }

    public SymbolTable symbolTable(){
        return this.symbolTable;
    }
}
