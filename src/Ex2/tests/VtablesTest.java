package Ex2.tests;

import Ex1.SearchInContext;
import Ex2.Vtable;
import Ex2.VtablesMapBuilder;
import ast.AstXMLSerializer;
import ast.Program;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
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
    public void forClassesToCheck() {
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
    public void forClassesToCheck_EhasFoo() {
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
    public void forClassesToCheck_EhasFooBDFDont() {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck_EhasFooBDFDont.java.xml";
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
    public void forClassesToCheck_FdoesntHaveFoo() {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck_FDoesntHaveFoo.java.xml";
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
        Assert.assertNull(F_vtable.getImplementingClassName("foo"));

    }

    @Test
    public void forClassesToCheck_2roots() {
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck_2roots.java.xml";
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
        classNames.add("G");
        classNames.add("H");

        for(String className : classNames){
            System.out.println(className);
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

        //G
        Vtable G_vtable = class2vtable.get("G");
        Assert.assertEquals("G", G_vtable.getImplementingClassName("foo"));

        //H
        Vtable H_vtable = class2vtable.get("H");
        Assert.assertEquals("G", H_vtable.getImplementingClassName("foo"));

    }

    @Test
    public void onlyMain(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/onlyMain.java.xml";
        prog = xmlSerializer.deserialize(new File(filePath));
        SearchInContext searchInContext = new SearchInContext(this.prog);
        VtablesMapBuilder vtablesMapBuilder = new VtablesMapBuilder(searchInContext);
        HashMap<String, Vtable> class2vtable = vtablesMapBuilder.build();

        Assert.assertEquals(0, class2vtable.entrySet().size());

    }

    @Test
    public void forClassesToCheck_methodIndices(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck_methodIndices.java.xml";
        prog = xmlSerializer.deserialize(new File(filePath));
        SearchInContext searchInContext = new SearchInContext(this.prog);
        VtablesMapBuilder vtablesMapBuilder = new VtablesMapBuilder(searchInContext);
        HashMap<String, Vtable> class2vtable = vtablesMapBuilder.build();

        Vtable A_vtable = class2vtable.get("A");
        Vtable B_vtable = class2vtable.get("B");
        Vtable C_vtable = class2vtable.get("C");
        Vtable D_vtable = class2vtable.get("D");
        Vtable E_vtable = class2vtable.get("E");
        Vtable F_vtable = class2vtable.get("F");

        //A
        Assert.assertEquals((Integer)0, A_vtable.getIndex("foo"));
        Assert.assertEquals((Integer)1, A_vtable.getIndex("foo2"));
        Assert.assertEquals("A", A_vtable.getImplementingClassName("foo"));
        Assert.assertEquals("A", A_vtable.getImplementingClassName("foo2"));


        //B
        Assert.assertEquals((Integer)0, B_vtable.getIndex("foo"));
        Assert.assertEquals((Integer)1, B_vtable.getIndex("foo2"));
        Assert.assertEquals("B", B_vtable.getImplementingClassName("foo"));
        Assert.assertEquals("A", B_vtable.getImplementingClassName("foo2"));

        //C
        Assert.assertEquals((Integer)0, C_vtable.getIndex("foo"));
        Assert.assertEquals((Integer)1, C_vtable.getIndex("foo2"));
        Assert.assertEquals((Integer)2, C_vtable.getIndex("foo3"));
        Assert.assertEquals("C", C_vtable.getImplementingClassName("foo"));
        Assert.assertEquals("A", C_vtable.getImplementingClassName("foo2"));
        Assert.assertEquals("C", C_vtable.getImplementingClassName("foo3"));

        //D
        Assert.assertEquals((Integer)0, D_vtable.getIndex("foo"));
        Assert.assertEquals((Integer)1, D_vtable.getIndex("foo2"));
        Assert.assertEquals((Integer)2, D_vtable.getIndex("foo4"));
        Assert.assertNull(D_vtable.getIndex("foo3"));
        Assert.assertEquals("B", D_vtable.getImplementingClassName("foo"));
        Assert.assertEquals("A", D_vtable.getImplementingClassName("foo2"));
        Assert.assertEquals("D", D_vtable.getImplementingClassName("foo4"));

        //E
        Assert.assertNull(E_vtable.getIndex("foo"));
        Assert.assertNull(E_vtable.getIndex("foo1"));
        Assert.assertNull(E_vtable.getIndex("foo2"));
        Assert.assertNull(E_vtable.getIndex("foo3"));
        Assert.assertNull(E_vtable.getIndex("foo4"));

        //F
        Assert.assertEquals((Integer)0, F_vtable.getIndex("foo"));
        Assert.assertEquals("F", F_vtable.getImplementingClassName("foo"));












    }
}
