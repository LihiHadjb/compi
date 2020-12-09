package Ex2;

import Ex1.SearchInContext;
import ast.Program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CodeGenerator {
    Program prog;
    String outFile;
    FileWriter fileWriter;

    //1. Take ast and build: Vtables, Class2Vtables, FieldOffsets, MethodOffsets
    //2. Call visitor to generate the code

    public CodeGenerator(Program prog, String outFileName){
        this.prog = prog;
        this.outFile = outFileName;
        this.fileWriter = createOutFile(outFileName);

    }

    public FileWriter createOutFile(String outFileName){
        try {
            File outFile = new File(outFileName);
            FileWriter fileWriter = new FileWriter(outFileName);
            return fileWriter;
//            if (outFile.createNewFile()) {
//                System.out.println("File created: " + myObj.getName());
//            } else {
//                System.out.println("File already exists.");
//            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;

    }

    public void closeWriter(){
        try {
            this.fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void generate(){
        SearchInContext searchInContext = new SearchInContext(this.prog);
        VtablesMapBuilder vtablesMapBuilder = new VtablesMapBuilder(searchInContext);
        HashMap<String, Vtable> class2vtable = vtablesMapBuilder.build();
        FieldOffsetsMapBuilder fieldOffsetsMapBuilder = new FieldOffsetsMapBuilder(searchInContext);
        HashMap<String, FieldOffsets> class2FieldOffsets = fieldOffsetsMapBuilder.build();
        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor(this.fileWriter, class2vtable, class2FieldOffsets, searchInContext);
        prog.accept(codeGenerationVisitor);
        closeWriter();


    }





}
