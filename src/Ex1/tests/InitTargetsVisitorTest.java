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

    private void init_targets(String oldName, String lineNumber, String filePath){
        prog = xmlSerializer.deserialize(new File(filePath));
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber);
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
        init_targets(oldName, lineNumber, filePath);

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
        String oldName = "Example";
        String lineNumber = "10";
        init_targets(oldName, lineNumber, filePath);

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

}
