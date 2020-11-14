package Ex1.tests;

import Ex1.Inheritance.InheritanceTrees;
import Ex1.SymbolTables.SymbolTable;
import Ex1.SymbolTables.SymbolTableBuilder;
import ast.*;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SymbolTableTest {

    @Test
    public void myExample1Test(){
        Program prog;
        AstXMLSerializer xmlSerializer = new AstXMLSerializer();
        prog = xmlSerializer.deserialize(new File("/home/pc/IdeaProjects/compi/src/Ex1/tests/myExample1.java.xml"));
        SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
        symbolTableBuilder.build(prog);

        for (ClassDecl classdecl : prog.classDecls()) {
            assertNotNull(classdecl.symbolTable);

            for (VarDecl field : classdecl.fields()) {
                assertNull(field.symbolTable);
            }
            for (MethodDecl methodDecl : classdecl.methoddecls()){
                assertNotNull(methodDecl.symbolTable);
            }
        }

    }

}


