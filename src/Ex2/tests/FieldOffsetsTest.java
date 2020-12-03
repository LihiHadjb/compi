package Ex2.tests;

import Ex1.SearchInContext;
import Ex2.FieldOffsets;
import Ex2.FieldOffsetsMapBuilder;
import ast.AstXMLSerializer;
import ast.Program;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

public class FieldOffsetsTest {
    AstXMLSerializer xmlSerializer;
    Program prog;

    @Before
    public void setUp() {
        this.xmlSerializer = new AstXMLSerializer();
    }

    @Test
    public void forClassesToCheck_fields(){
        String filePath = "/home/pc/IdeaProjects/compi/examples/ex1/testExamples/forClassesToCheck_fields.java.xml";
        prog = xmlSerializer.deserialize(new File(filePath));
        SearchInContext searchInContext = new SearchInContext(this.prog);
        FieldOffsetsMapBuilder fieldOffsetsMapBuilder = new FieldOffsetsMapBuilder(searchInContext);
        HashMap<String, FieldOffsets> class2fieldOffsets = fieldOffsetsMapBuilder.build();

        FieldOffsets A_fieldOffsets = class2fieldOffsets.get("A");
        FieldOffsets B_fieldOffsets = class2fieldOffsets.get("B");
        FieldOffsets C_fieldOffsets = class2fieldOffsets.get("C");
        FieldOffsets D_fieldOffsets = class2fieldOffsets.get("D");
        FieldOffsets E_fieldOffsets = class2fieldOffsets.get("E");
        FieldOffsets F_fieldOffsets = class2fieldOffsets.get("F");

        //A
        Assert.assertEquals((Integer)8, A_fieldOffsets.getIndex("field1"));
        Assert.assertEquals((Integer)12, A_fieldOffsets.getIndex("field3"));
        Assert.assertEquals((Integer)13, A_fieldOffsets.getIndex("field2"));
        Assert.assertEquals(21, A_fieldOffsets.getLast_index());

        //B
        Assert.assertEquals((Integer)8, B_fieldOffsets.getIndex("field1"));
        Assert.assertEquals((Integer)12, B_fieldOffsets.getIndex("field3"));
        Assert.assertEquals((Integer)13, B_fieldOffsets.getIndex("field2"));
        Assert.assertEquals(21, B_fieldOffsets.getLast_index());
        Assert.assertTrue(A_fieldOffsets != B_fieldOffsets);

        //C
        Assert.assertEquals((Integer)8, C_fieldOffsets.getIndex("field1"));
        Assert.assertEquals((Integer)12, C_fieldOffsets.getIndex("field3"));
        Assert.assertEquals((Integer)13, C_fieldOffsets.getIndex("field2"));
        Assert.assertEquals((Integer)21, C_fieldOffsets.getIndex("field4"));
        Assert.assertEquals(25, C_fieldOffsets.getLast_index());

        //D
        Assert.assertEquals((Integer)8, D_fieldOffsets.getIndex("field1"));
        Assert.assertEquals((Integer)12, D_fieldOffsets.getIndex("field3"));
        Assert.assertEquals((Integer)13, D_fieldOffsets.getIndex("field2"));
        Assert.assertEquals((Integer)21, D_fieldOffsets.getIndex("field4"));
        Assert.assertEquals(25, D_fieldOffsets.getLast_index());

        //E
        Assert.assertEquals((Integer)8, E_fieldOffsets.getIndex("field1"));
        Assert.assertEquals((Integer)12, E_fieldOffsets.getIndex("field3"));
        Assert.assertNull(E_fieldOffsets.getIndex("field2"));
        Assert.assertEquals(13, E_fieldOffsets.getLast_index());

        //F
        Assert.assertEquals((Integer)8, F_fieldOffsets.getIndex("field1"));
        Assert.assertEquals((Integer)12, F_fieldOffsets.getIndex("field3"));
        Assert.assertNull(F_fieldOffsets.getIndex("field2"));
        Assert.assertEquals(13, F_fieldOffsets.getLast_index());














    }
}
