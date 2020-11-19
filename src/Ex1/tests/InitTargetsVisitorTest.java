package Ex1.tests;

import Ex1.InitTargetsVisitor;
import ast.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class InitTargetsVisitorTest {
    AstXMLSerializer xmlSerializer;
    Program prog;
    AstNode targetAstNode;
    ClassDecl targetClassNode;
    MethodDecl targetMethodNode;

    @Before
    public void setUp() {
        this.xmlSerializer = new AstXMLSerializer();
    }

    private void init_targets(String oldName, String lineNumber, String filePath, boolean isMethod){
        prog = xmlSerializer.deserialize(new File(filePath));
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber, isMethod);
        prog.accept(initTargetsVisitor);
        targetAstNode = initTargetsVisitor.targetAstNode();
        targetClassNode = initTargetsVisitor.lastClassSeen();
        targetMethodNode = initTargetsVisitor.lastMethodSeen();
    }

    @Test
    public void method_whenCorrecttMethodNameAndLineNumber_shouldSaveCorrectTargets() {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/method.java.xml";
        String oldName = "run";
        String lineNumber = "13";
        init_targets(oldName, lineNumber, filePath, true);

        assertNotNull(targetAstNode);
        assertNotNull(targetClassNode);
        assertNotNull(targetMethodNode);
        assertEquals(targetAstNode, targetMethodNode);

        assertEquals(targetMethodNode.lineNumber, (Integer)13);
        assertEquals(targetClassNode.name(), "Example");
        assertEquals(targetMethodNode.name(), "run");
    }

    @Test
    public void method_whenCorrectVariableNameAndLineNumber_shouldSaveCorrectTargets() {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/method.java.xml";
        String oldName = "e";
        String lineNumber = "10";
        init_targets(oldName, lineNumber, filePath, false);

        assertNotNull(targetAstNode);
        assertNotNull(targetClassNode);
        assertNotNull(targetMethodNode);
        assertNotEquals(targetAstNode, targetClassNode);
        assertNotEquals(targetAstNode, targetMethodNode);

        assertEquals(targetAstNode.lineNumber, (Integer)10);
        assertEquals(((VariableIntroduction)targetAstNode).name(), "e");
        assertEquals(targetClassNode.name(), "Example");
        assertEquals(targetMethodNode.name(), "run");
    }

    @Test
    public void forInitTargets_method_inSuper(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forInitTargets.java.xml";
        String oldName = "foo1";
        String lineNumber = "3";
        init_targets(oldName, lineNumber, filePath, true);

        assertNotNull(targetAstNode);
        assertNotNull(targetClassNode);
        assertNotNull(targetMethodNode);
        assertNotEquals(targetAstNode, targetClassNode);
        assertEquals(targetAstNode, targetMethodNode);

        assertEquals(targetAstNode.lineNumber, (Integer)3);
        assertEquals(((MethodDecl)targetAstNode).name(), "foo1");
        assertEquals(targetClassNode.name(), "A2");
        assertEquals(targetMethodNode.name(), "foo1");

    }

    @Test
    public void forInitTargets_method_inInherited(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forInitTargets.java.xml";
        String oldName = "foo1";
        String lineNumber = "21";
        init_targets(oldName, lineNumber, filePath, true);

        assertNotNull(targetAstNode);
        assertNotNull(targetClassNode);
        assertNotNull(targetMethodNode);
        assertNotEquals(targetAstNode, targetClassNode);
        assertEquals(targetAstNode, targetMethodNode);

        assertEquals(targetAstNode.lineNumber, (Integer)21);
        assertEquals(((MethodDecl)targetAstNode).name(), "foo1");
        assertEquals(targetClassNode.name(), "B2");
        assertEquals(targetMethodNode.name(), "foo1");

    }


//    @Test
//    public void forInitTargets_field_inSuper() {
//        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/method.java.xml";
//        String oldName = "Example";
//        String lineNumber = "10";
//        init_targets(oldName, lineNumber, filePath);
//
//        assertNotNull(targetAstNode);
//        assertNotNull(targetClassNode);
//        assertNotNull(targetMethodNode);
//        assertNotEquals(targetAstNode, targetClassNode);
//        assertNotEquals(targetAstNode, targetMethodNode);
//
//        assertEquals(targetAstNode.lineNumber, (Integer)10);
//        assertEquals(((VariableIntroduction)targetAstNode).name(), "e");
//        assertEquals(targetClassNode.name(), "Example");
//        assertEquals(targetMethodNode.name(), "run");
//    }



    @Test
    public void forInitTargets_field_Hiding() {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forInitTargets.java.xml";
        String oldName = "a2";
        String lineNumber = "23";
        init_targets(oldName, lineNumber, filePath, false);

        assertNotNull(targetAstNode);
        assertNotNull(targetClassNode);
        assertNotNull(targetMethodNode);
        assertNotEquals(targetAstNode, targetClassNode);
        assertNotEquals(targetAstNode, targetMethodNode);

        assertEquals(targetAstNode.lineNumber, (Integer)23);
        assertEquals(((VariableIntroduction)targetAstNode).name(), "a2");
        assertEquals(targetClassNode.name(), "B2");
        assertEquals(targetMethodNode.name(), "foo1");
    }

    @Test
    public void forInitTargets_formal() {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forInitTargets.java.xml";
        String oldName = "a";
        String lineNumber = "22";
        init_targets(oldName, lineNumber, filePath, false);

        assertNotNull(targetAstNode);
        assertNotNull(targetClassNode);
        assertNotNull(targetMethodNode);
        assertNotEquals(targetAstNode, targetClassNode);
        assertNotEquals(targetAstNode, targetMethodNode);

        assertEquals(targetAstNode.lineNumber, (Integer)22);
        assertEquals(((VariableIntroduction)targetAstNode).name(), "a");
        assertEquals(targetClassNode.name(), "B2");
        assertEquals(targetMethodNode.name(), "foo1");
    }


}
