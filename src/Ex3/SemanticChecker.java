package Ex3;

import Ex1.SearchInContext;
import ast.Program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SemanticChecker {

    public FileWriter createOutFile(String outFileName){
//        System.out.println("_____Entered createOutFile method_____");
//        System.out.println("_____At createOutFile method - outFileName is: "+outFileName);
        try {
            File outFile = new File(outFileName);
            FileWriter fileWriter = new FileWriter(outFileName);
            return fileWriter;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public void writeToFile(String text, FileWriter fileWriter) {
        try{
            fileWriter.write(text);
        }
        catch (IOException e){
            System.out.println("error writing!");
            e.printStackTrace();
        }
    }




    public SemanticChecker(Program prog, String outFileName){
        SearchInContext searchInContext = new SearchInContext(prog);
        SemanticCheckerVisitor semanticCheckerVisitor = new SemanticCheckerVisitor(searchInContext);
        InitializationVisitor initializationVisitor = new InitializationVisitor(searchInContext);
        prog.accept(semanticCheckerVisitor);

        FileWriter fileWriter = createOutFile(outFileName);
        if(semanticCheckerVisitor.isErrorFound()){
            writeToFile("ERROR\n", fileWriter);
        }
        else{
            prog.accept(initializationVisitor);
            if(initializationVisitor.isErrorFound()){
                writeToFile("ERROR\n", fileWriter);
            }
            else{
                writeToFile("OK\n", fileWriter);
            }
        }

        try{
            fileWriter.close();
        }
        catch (IOException e){
            System.out.println("error closing");
            e.printStackTrace();
        }

    }
}
