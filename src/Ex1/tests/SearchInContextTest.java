package Ex1.tests;

import Ex1.InitTargetsVisitor;
import Ex1.SearchInContext;
import ast.AstXMLSerializer;
import ast.MethodDecl;
import ast.Program;
import ast.VarDecl;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SearchInContextTest {
    AstXMLSerializer xmlSerializer;
    Program prog;

    @Before
    public void setUp() {
        this.xmlSerializer = new AstXMLSerializer();
    }


    @Test
    public void forClassesToCheck_method_inB(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck.java.xml";
        String oldName = "foo";
        String lineNumber = "35";
        prog = xmlSerializer.deserialize(new File(filePath));
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber);
        prog.accept(initTargetsVisitor);

        SearchInContext searchInContext = new SearchInContext(prog, true, oldName, lineNumber);
        Set<String> classesToCheck = searchInContext.getClassesToCheckForMethod((MethodDecl)initTargetsVisitor.targetAstNode());
        Set<String> expected = new HashSet<>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        expected.add("D");

        assertEquals(expected, classesToCheck);
    }

    @Test
    public void forClassesToCheck_method_inF(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck.java.xml";
        String oldName = "foo";
        String lineNumber = "25";
        prog = xmlSerializer.deserialize(new File(filePath));
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber);
        prog.accept(initTargetsVisitor);

        SearchInContext searchInContext = new SearchInContext(prog, true, oldName, lineNumber);
        Set<String> classesToCheck = searchInContext.getClassesToCheckForMethod((MethodDecl)initTargetsVisitor.targetAstNode());
        Set<String> expected = new HashSet<>();
        expected.add("F");

        assertEquals(expected, classesToCheck);
    }


    @Test
    public void forClassesToCheck_field_inE(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck.java.xml";
        String oldName = "field1";
        String lineNumber = "5";
        prog = xmlSerializer.deserialize(new File(filePath));
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber);
        prog.accept(initTargetsVisitor);

        SearchInContext searchInContext = new SearchInContext(prog, true, oldName, lineNumber);
        Set<String> classesToCheck = searchInContext.getClassesToCheckForField((VarDecl) initTargetsVisitor.targetAstNode());
        Set<String> expected = new HashSet<>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        expected.add("D");
        expected.add("E");
        expected.add("F");

        assertEquals(expected, classesToCheck);
    }

    @Test
    public void forClassesToCheck_field_inA(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck.java.xml";
        String oldName = "field2";
        String lineNumber = "10";
        prog = xmlSerializer.deserialize(new File(filePath));
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber);
        prog.accept(initTargetsVisitor);

        SearchInContext searchInContext = new SearchInContext(prog, true, oldName, lineNumber);
        Set<String> classesToCheck = searchInContext.getClassesToCheckForField((VarDecl) initTargetsVisitor.targetAstNode());
        Set<String> expected = new HashSet<>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        expected.add("D");

        assertEquals(expected, classesToCheck);
    }

}
