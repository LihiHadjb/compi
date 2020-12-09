package Ex2.tests;

import Ex2.CodeGenerator;
import ast.AstXMLSerializer;
import ast.Program;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class GenerationTest {
    String xmlsFolder = "/home/pc/IdeaProjects/compi/examples/";
    CodeGenerator codeGenerator;

    AstXMLSerializer xmlSerializer;
    Program prog;

    @Before
    public void setUp() {
        this.xmlSerializer = new AstXMLSerializer();
    }


    public void doTest(String xmlPath){
        String allPath = xmlsFolder + xmlPath;
        String outPath = xmlsFolder + xmlPath + "_out";

        //remove ".java.xml"
        String expectedOutPath = allPath;
        expectedOutPath = expectedOutPath.substring(0, expectedOutPath.lastIndexOf('.'));
        expectedOutPath = expectedOutPath.substring(0, expectedOutPath.lastIndexOf('.'));
        expectedOutPath = expectedOutPath + ".ll";

        prog = xmlSerializer.deserialize(new File(allPath));
        codeGenerator = new CodeGenerator(prog, outPath);
        codeGenerator.generate();

        String actual = Utils.stringFromFile(outPath);
        String expected = Utils.stringFromFile(expectedOutPath);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("lli " + outPath);

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void test_1_vars(){
        doTest("1_vars/Simple.java.xml");
    }

    @Test
    public void test_2_vars_type(){
        doTest("2_vars_type/VarType.java.xml");
    }
    //TODO: what did he say about VarTypeBad?

    @Test
    public void test_3_simple_expr(){
        doTest("3_simple_expr/SimpleExpr.java.xml");
    }

    @Test
    public void test_4_compound_expr(){
        doTest("4_compound_expr/CompoundExpr.java.xml");
    }

    @Test
    public void test_5_if(){
        doTest("5_if/If.java.xml");
    }

    @Test
    public void test_6_and(){
        doTest("6_and/And.java.xml");
    }

    @Test
    public void test_6_lowering_oo_Arrays(){
        doTest("6_lowering_oo/Arrays.java.xml");
    }

    @Test
    public void test_6_lowering_oo_Classes(){
        doTest("6_lowering_oo/Classes.java.xml");
    }

    @Test
    public void test_7_arrays(){
        doTest("7_arrays/Arrays.java.xml");
    }

    @Test
    public void BinarySearch(){
        doTest("ex2/BinarySearch.java.xml");
    }

    @Test
    public void BinaryTree(){
        doTest("ex2/BinaryTree.java.xml");
    }

    @Test
    public void BubbleSort(){
        doTest("ex2/BubbleSort.java.xml");
    }

    @Test
    public void Factorial(){
        doTest("ex2/Factorial.java.xml");
    }

    @Test
    public void LinearSearch(){
        doTest("ex2/LinearSearch.java.xml");
    }

    @Test
    public void LinkedList(){
        doTest("ex2/LinkedList.java.xml");
    }

    @Test
    public void QuickSort(){
        doTest("ex2/QuickSort.java.xml");
    }

    @Test
    public void TreeVisitor(){
        doTest("ex2/TreeVisitor.java.xml");
    }













}
