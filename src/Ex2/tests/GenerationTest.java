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
        String outPath = xmlPath+"_out";

        //remove ".java.xml"
        String expectedOutPath = allPath.substring(0, allPath.lastIndexOf('.')).substring(0, allPath.lastIndexOf('.')) + ".ll";

        prog = xmlSerializer.deserialize(new File(allPath));
        codeGenerator = new CodeGenerator(prog, xmlPath+"_out");
        codeGenerator.generate();

        String actual = Utils.stringFromFile(outPath);
        String expected = Utils.stringFromFile(expectedOutPath);
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

    @Test
    public void test_3_simple_expr(){
        doTest("3_simple_expr/SimpleExpr.java.xml");
    }







}
