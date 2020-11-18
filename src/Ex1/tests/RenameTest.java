package Ex1.tests;

import Ex1.Rename;
import ast.AstXMLSerializer;
import ast.Program;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class RenameTest {

    public void run_rename(String path, String oldName, String lineNumber, String newName, boolean isMethod){
        AstXMLSerializer xmlSerializer = new AstXMLSerializer();
        Program prog;
        prog = xmlSerializer.deserialize(new File(path));
        Rename rename = new Rename(prog, isMethod, oldName, lineNumber, newName);
    }

    @Test
    public void field_related_sibling_class(){
        String path = "/home/pc/IdeaProjects/compi/examples/ex1/test_cases/variable_renaming/field_related_sibling_class.xml";
        boolean isMethod = false;
        String oldName = "theVar";
        String lineNumber = "10";
        String newName = "renamedVar";
        run_rename(path, oldName, lineNumber, newName, isMethod);

    }


    @Test
    public void method_and_variable_with_the_same_name(){
        String path = "/home/pc/IdeaProjects/compi/examples/ex1/test_cases/variable_renaming/method_and_variable_with_the_same_name.xml";
        boolean isMethod = true;
        String oldName = "theThing";
        String lineNumber = "27";
        String newName = "renamedThing";
        run_rename(path, oldName, lineNumber, newName, isMethod);
    }

    @Test
    public void method_related_sibling_class(){
        String path = "/home/pc/IdeaProjects/compi/examples/ex1/test_cases/variable_renaming/method_related_sibling_class.xml";
        boolean isMethod = true;
        String oldName = "theMethod";
        String lineNumber = "25";
        String newName = "renamedMethod";
        run_rename(path, oldName, lineNumber, newName, isMethod);

    }

    @Test
    public void variable_and_method_with_the_same_name1(){
        String path = "/home/pc/IdeaProjects/compi/examples/ex1/test_cases/variable_renaming/variable_and_method_with_the_same_name.xml";
        boolean isMethod = false;
        String oldName = "theThing";
        String lineNumber = "10";
        String newName = "renamedThing";
        run_rename(path, oldName, lineNumber, newName, isMethod);
    }

    @Test
    public void variable_and_method_with_the_same_name2(){
        String path = "/home/pc/IdeaProjects/compi/examples/ex1/test_cases/variable_renaming/variable_and_method_with_the_same_name.xml";
        boolean isMethod = false;
        String oldName = "theThing";
        String lineNumber = "33";
        String newName = "renamedThing";
        run_rename(path, oldName, lineNumber, newName, isMethod);
    }

    @Test
    public void variable_and_method_with_the_same_name3(){
        String path = "/home/pc/IdeaProjects/compi/examples/ex1/test_cases/variable_renaming/variable_and_method_with_the_same_name.xml";
        boolean isMethod = false;
        String oldName = "theThing";
        String lineNumber = "38";
        String newName = "renamedThing";
        run_rename(path, oldName, lineNumber, newName, isMethod);
    }

}
