package Ex2;

import Ex1.SearchInContext;
import ast.Program;

import java.util.HashMap;

public class CodeGenerator {
    Program prog;
    String outFile;

    //1. Take ast and build: Vtables, Class2Vtables, FieldOffsets, MethodOffsets
    //2. Call visitor to generate the code

    public CodeGenerator(Program prog, String outFile){
        this.prog = prog;
        this.outFile = outFile;
        //TODO: create the file of outFile

    }

    public void generate(){
        SearchInContext searchInContext = new SearchInContext(this.prog);
        VtablesMapBuilder vtablesMapBuilder = new VtablesMapBuilder(searchInContext);
        HashMap<String, Vtable> class2vtable = vtablesMapBuilder.build();
        HashMap<String, FieldOffsets> class2FieldOffsets = createFieldOffets;
        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        prog.accept(codeGenerationVisitor);

    }





}
