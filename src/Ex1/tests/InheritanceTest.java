package Ex1.tests;

import Ex1.InheritanceNode;
import Ex1.InheritanceTrees;
import ast.AstXMLSerializer;
import ast.Program;
import org.junit.*;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class InheritanceTest {

    @Test
    public void myExample1Test(){
        Program prog;
        AstXMLSerializer xmlSerializer = new AstXMLSerializer();
        prog = xmlSerializer.deserialize(new File("myExample1"));
        InheritanceTrees trees = new InheritanceTrees(prog);

        HashSet<String> expectedFlatClasseskeys = new HashSet<>();
        expectedFlatClasseskeys.add("A");
        expectedFlatClasseskeys.add("B");
        expectedFlatClasseskeys.add("C");
        expectedFlatClasseskeys.add("D");
        expectedFlatClasseskeys.add("E");

        assertTrue(trees.keySet().equals(expectedFlatClasseskeys);

        System.out.println(trees);
    }

}
