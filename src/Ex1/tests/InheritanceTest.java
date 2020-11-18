package Ex1.tests;

import Ex1.Inheritance.InheritanceTrees;
import ast.AstXMLSerializer;
import ast.Program;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.*;
import static org.junit.Assert.*;

public class InheritanceTest {


    @Test
    public void myExample2Test(){
        Program prog;
        AstXMLSerializer xmlSerializer = new AstXMLSerializer();
        prog = xmlSerializer.deserialize(new File("/home/pc/IdeaProjects/compi/src/Ex1/tests/myExample2.java.xml"));
        InheritanceTrees trees = new InheritanceTrees(prog);

//        //check flatmap
//        HashSet<String> expectedFlatClasseskeys = new HashSet<>();
//        expectedFlatClasseskeys.add("A");
//        expectedFlatClasseskeys.add("B");
//        expectedFlatClasseskeys.add("C");
//        expectedFlatClasseskeys.add("D");
//        expectedFlatClasseskeys.add("E");
        //Set<String> actualFlatClassesKeys = trees.flatClasses().keySet();
        //assertTrue(actualFlatClassesKeys.equals(expectedFlatClasseskeys));

        assertNotNull(trees.className2InheritanceNode("A"));
        assertNotNull(trees.className2InheritanceNode("B"));
        assertNotNull(trees.className2InheritanceNode("C"));
        assertNotNull(trees.className2InheritanceNode("D"));
        assertNotNull(trees.className2InheritanceNode("E"));
        assertNull(trees.className2InheritanceNode("F"));

        //check tree
        System.out.println(trees);
    }

}
