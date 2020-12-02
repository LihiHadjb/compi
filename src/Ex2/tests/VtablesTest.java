package Ex2.tests;

import Ex1.SearchInContext;
import Ex2.Vtable;
import Ex2.VtablesMapBuilder;
import ast.AstXMLSerializer;
import ast.Program;
import org.junit.Assert;
import org.junit.Before;
//import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VtablesTest {
    AstXMLSerializer xmlSerializer;
    Program prog;

    @Before
    public void setUp() {
        this.xmlSerializer = new AstXMLSerializer();
    }

    @Test
    public void forClassesToCheck() throws Exception {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck.java.xml";
        prog = xmlSerializer.deserialize(new File(filePath));
        SearchInContext searchInContext = new SearchInContext(this.prog);
        VtablesMapBuilder vtablesMapBuilder = new VtablesMapBuilder(searchInContext);
        HashMap<String, Vtable> class2vtable = vtablesMapBuilder.build();

        List<String> classNames = new ArrayList();
        classNames.add("A");
        classNames.add("B");
        classNames.add("C");
        classNames.add("D");
        classNames.add("E");
        classNames.add("F");

        for(String className : classNames){
            Assert.assertNotNull(class2vtable.get(className));
        }


        //A
        Vtable A_vtable = class2vtable.get("A");
        Assert.assertEquals("A", A_vtable.getImplementingClassName("foo"));

        //B
        Vtable B_vtable = class2vtable.get("B");
        Assert.assertEquals("B", B_vtable.getImplementingClassName("foo"));

        //C
        Vtable C_vtable = class2vtable.get("C");
        Assert.assertEquals("C", C_vtable.getImplementingClassName("foo"));

        //D
        Vtable D_vtable = class2vtable.get("D");
        Assert.assertEquals("D", D_vtable.getImplementingClassName("foo"));

        //E
        Vtable E_vtable = class2vtable.get("E");
        Assert.assertNull(E_vtable.getImplementingClassName("foo"));

        //F
        Vtable F_vtable = class2vtable.get("F");
        Assert.assertEquals("F", F_vtable.getImplementingClassName("foo"));
    }

    @Test
    public void forClassesToCheck_EhasFoo() throws Exception {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck_EhasFoo.java.xml";
        prog = xmlSerializer.deserialize(new File(filePath));
        SearchInContext searchInContext = new SearchInContext(this.prog);
        VtablesMapBuilder vtablesMapBuilder = new VtablesMapBuilder(searchInContext);
        HashMap<String, Vtable> class2vtable = vtablesMapBuilder.build();

        List<String> classNames = new ArrayList();
        classNames.add("A");
        classNames.add("B");
        classNames.add("C");
        classNames.add("D");
        classNames.add("E");
        classNames.add("F");

        for(String className : classNames){
            Assert.assertNotNull(class2vtable.get(className));
        }

        //A
        Vtable A_vtable = class2vtable.get("A");
        Assert.assertEquals("A", A_vtable.getImplementingClassName("foo"));

        //B
        Vtable B_vtable = class2vtable.get("B");
        Assert.assertEquals("B", B_vtable.getImplementingClassName("foo"));

        //C
        Vtable C_vtable = class2vtable.get("C");
        Assert.assertEquals("C", C_vtable.getImplementingClassName("foo"));

        //D
        Vtable D_vtable = class2vtable.get("D");
        Assert.assertEquals("D", D_vtable.getImplementingClassName("foo"));

        //E
        Vtable E_vtable = class2vtable.get("E");
        Assert.assertEquals("E", E_vtable.getImplementingClassName("foo"));

        //F
        Vtable F_vtable = class2vtable.get("F");
        Assert.assertEquals("F", F_vtable.getImplementingClassName("foo"));

    }

    @Test
    public void forClassesToCheck_EhasFooFAndBDont() throws Exception {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck_EhasFooFAndBFDont.java.xml";
        prog = xmlSerializer.deserialize(new File(filePath));
        SearchInContext searchInContext = new SearchInContext(this.prog);
        VtablesMapBuilder vtablesMapBuilder = new VtablesMapBuilder(searchInContext);
        HashMap<String, Vtable> class2vtable = vtablesMapBuilder.build();

        List<String> classNames = new ArrayList();
        classNames.add("A");
        classNames.add("B");
        classNames.add("C");
        classNames.add("D");
        classNames.add("E");
        classNames.add("F");

        for(String className : classNames){
            Assert.assertNotNull(class2vtable.get(className));
        }

        //A
        Vtable A_vtable = class2vtable.get("A");
        Assert.assertEquals("A", A_vtable.getImplementingClassName("foo"));

        //B
        Vtable B_vtable = class2vtable.get("B");
        Assert.assertEquals("A", B_vtable.getImplementingClassName("foo"));

        //C
        Vtable C_vtable = class2vtable.get("C");
        Assert.assertEquals("C", C_vtable.getImplementingClassName("foo"));

        //D
        Vtable D_vtable = class2vtable.get("D");
        Assert.assertEquals("A", D_vtable.getImplementingClassName("foo"));

        //E
        Vtable E_vtable = class2vtable.get("E");
        Assert.assertEquals("E", E_vtable.getImplementingClassName("foo"));

        //F
        Vtable F_vtable = class2vtable.get("F");
        Assert.assertEquals("E", F_vtable.getImplementingClassName("foo"));

    }

    @Test
    public void forClassesToCheck_FdoesntHaveFoo() throws Exception {

    }

    @Test
    public void forClassesToCheck_2roots() throws Exception {

    }

    @Test
    public void onlyMain(){

    }
}
