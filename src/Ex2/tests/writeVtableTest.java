package Ex2.tests;

import Ex1.SearchInContext;
import Ex2.*;
import ast.AstXMLSerializer;
import ast.Program;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class writeVtableTest {
    AstXMLSerializer xmlSerializer;
    Program prog;

    @Before
    public void setUp() {
        this.xmlSerializer = new AstXMLSerializer();
    }

    public String stringFromFile(String path){
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(path)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;
    }

    public void test_vtable(String outName, String expectedName, int index) throws IOException {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/Classes.java.xml";
        prog = xmlSerializer.deserialize(new File(filePath));

        CodeGenerator codeGenerator = new CodeGenerator(prog, "");
        FileWriter fileWriter = codeGenerator.createOutFile("/home/pc/IdeaProjects/compi/examples/ex1/testExamples/" + outName);

        SearchInContext searchInContext = new SearchInContext(this.prog);
        VtablesMapBuilder vtablesMapBuilder = new VtablesMapBuilder(searchInContext);
        HashMap<String, Vtable> class2vtable = vtablesMapBuilder.build();
        FieldOffsetsMapBuilder fieldOffsetsMapBuilder = new FieldOffsetsMapBuilder(searchInContext);
        HashMap<String, FieldOffsets> class2FieldOffsets = fieldOffsetsMapBuilder.build();

        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor(fileWriter, class2vtable, class2FieldOffsets);
        codeGenerationVisitor.writeVtable(prog.classDecls().get(index));
        fileWriter.close();

        String actual = stringFromFile("/home/pc/IdeaProjects/compi/examples/ex1/testExamples/" + outName + ".ll");
        String expected = stringFromFile("/home/pc/IdeaProjects/compi/examples/ex1/testExamples/" + expectedName + ".ll");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void vtables() throws IOException {
        test_vtable("Classes_out", "Classes_out_expected", 0);
        test_vtable("Base_out", "Base_out_expected", 1);
        test_vtable("Derived_out", "Derived_out_expected", 2);
    }
}
