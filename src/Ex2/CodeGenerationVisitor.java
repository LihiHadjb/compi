package Ex2;

import ast.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerationVisitor implements Visitor {
    String GENERAL_METHODS_PATH = "generalMethods";
    String INT_SIZE = "i32";
    String PTR_SIZE = "i8*";
    String INT_ARRAY_SIZE = "i32*";
    String BOOLEAN_SIZE = "i1";


    Integer currRegister;
    FileWriter fileWriter;
    ClassDecl lastClassSeen;
    HashMap<String, Vtable> class2vtable;
    HashMap<String, FieldOffsets> class2FieldOffsets;


    public CodeGenerationVisitor(FileWriter fileWriter, HashMap<String, Vtable> class2vtable, HashMap<String, FieldOffsets> class2FieldOffsets){
        this.fileWriter = fileWriter;
        this.currRegister = 0;
        this.class2vtable = class2vtable;
        this.class2FieldOffsets = class2FieldOffsets;
    }

    public void writeToFile(String text) throws IOException {
        this.fileWriter.write(text);
//        this.fileWriter.close();

    }


    public String getNextRegister(){
        String result = "%_" + this.currRegister.toString();
        this.currRegister++;
        return result;
    }

    public void writeGeneralMethods() throws IOException {
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(GENERAL_METHODS_PATH)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        writeToFile(content);
    }


    public HashMap<Integer, String> reverseMap(HashMap<String, Integer> map){
        HashMap<Integer, String> result = new HashMap<>();
        for(Map.Entry<String, Integer> entry : map.entrySet()){
            result.put(entry.getValue(), entry.getKey());
        }
        return result;
    }

    public String getFormalString(FormalArg formalArg){
        AstType astType = formalArg.type();
        return getSizeString(astType);
    }

    public String getSizeString(AstType astType){
        String size;
        if(astType instanceof IntAstType){
            size = INT_SIZE;
        }
        else if(astType instanceof BoolAstType){
            size = BOOLEAN_SIZE;
        }
        else if(astType instanceof IntArrayAstType){
            size = INT_ARRAY_SIZE;
        }
        else{
            size = PTR_SIZE;
        }
        return size;
    }

    public HashMap<String, String> createMethodName2ReturnString(Vtable classVtable){
        HashMap<String, String> result = new HashMap<>();

        for(Map.Entry<String, MethodDecl> entry : classVtable.getMethodName2MethodDecl().entrySet()){
            MethodDecl methodDecl = entry.getValue();
            result.put(methodDecl.name(), getSizeString(methodDecl.returnType()));
        }
        return result;
    }


    public HashMap<String, String> createMethodName2FormalsString(Vtable classVtable){
        HashMap<String, String> result = new HashMap<>();
        for(Map.Entry<String, MethodDecl> entry : classVtable.getMethodName2MethodDecl().entrySet()){
            StringBuilder methodFormalsString = new StringBuilder();
            methodFormalsString.append("(" + PTR_SIZE);
            MethodDecl methodDecl = entry.getValue();
            if(methodDecl.formals() != null){
                for(FormalArg formalArg : methodDecl.formals()){
                    methodFormalsString.append(", " + getFormalString(formalArg));
                }
            }
            methodFormalsString.append(")*");
            result.put(methodDecl.name(), methodFormalsString.toString());
        }
        return result;
    }

    public void writeVtable(ClassDecl classDecl) throws IOException {
        Vtable classVtable = this.class2vtable.get(classDecl.name());

        String intro = "@." + classDecl.name() + "_vtable = global [" + classVtable.methodName2Index.size() + " x i8*] [\n";
        writeToFile(intro);

        HashMap<String, String> methodName2FormalsString = createMethodName2FormalsString(classVtable);
        HashMap<String, String> methodName2ReturnString = createMethodName2ReturnString(classVtable);

        HashMap<Integer, String> index2methodName = reverseMap(classVtable.methodName2Index);
        for(int i=0; i<index2methodName.size(); i++){
            String methodName = index2methodName.get(i);
            String formalString = methodName2FormalsString.get(methodName);
            String returnTypeString = methodName2ReturnString.get(methodName);
            String nameString = "@" + classVtable.getImplementingClassName(methodName) + "." + methodName;
            String methodLine = "\t" + PTR_SIZE + " bitcast" + " (" + returnTypeString + " " + formalString + " " + nameString  + " to " + PTR_SIZE +")";
            if(i<=index2methodName.size()-2){
                methodLine = methodLine + ",";
            }
            methodLine = methodLine + "\n";
            writeToFile(methodLine);
        }
        String ending = "]\n";
        writeToFile(ending);
    }

    @Override
    public void visit(Program prog){
        try{
            writeGeneralMethods();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (prog.mainClass() != null){
            prog.mainClass().accept(this); // visit(prog.mainClass());
        }
        if (prog.classDecls() != null){
            for (ClassDecl classDecl : prog.classDecls()){
                try{
                    writeVtable(classDecl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (ClassDecl classDecl : prog.classDecls()){
                classDecl.accept(this); // visit(classDecl);
            }
        }
    }

    @Override
    public void visit(ClassDecl classDecl) {
        this.lastClassSeen = classDecl;
        if (classDecl.methoddecls() != null){
            for (MethodDecl methodDecl : classDecl.methoddecls()){
                methodDecl.accept(this);
            }
        }
    }

    @Override
    public void visit(MainClass mainClass) {

    }

    @Override
    public void visit(MethodDecl methodDecl) {

    }

    @Override
    public void visit(FormalArg formalArg) {

    }

    @Override
    public void visit(VarDecl varDecl) {

    }

    @Override
    public void visit(BlockStatement blockStatement) {

    }

    @Override
    public void visit(IfStatement ifStatement) {

    }

    @Override
    public void visit(WhileStatement whileStatement) {

    }

    @Override
    public void visit(SysoutStatement sysoutStatement) {

    }

    @Override
    public void visit(AssignStatement assignStatement) {

    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {

    }

    @Override
    public void visit(AndExpr e) {

    }

    @Override
    public void visit(LtExpr e) {

    }

    @Override
    public void visit(AddExpr e) {

    }

    @Override
    public void visit(SubtractExpr e) {

    }

    @Override
    public void visit(MultExpr e) {

    }

    @Override
    public void visit(ArrayAccessExpr e) {

    }

    @Override
    public void visit(ArrayLengthExpr e) {

    }

    @Override
    public void visit(MethodCallExpr e) {

    }

    @Override
    public void visit(IntegerLiteralExpr e) {

    }

    @Override
    public void visit(TrueExpr e) {

    }

    @Override
    public void visit(FalseExpr e) {

    }

    @Override
    public void visit(IdentifierExpr e) {

    }

    @Override
    public void visit(ThisExpr e) {

    }

    @Override
    public void visit(NewIntArrayExpr e) {

    }

    @Override
    public void visit(NewObjectExpr e) {

    }

    @Override
    public void visit(NotExpr e) {

    }

    @Override
    public void visit(IntAstType t) {

    }

    @Override
    public void visit(BoolAstType t) {

    }

    @Override
    public void visit(IntArrayAstType t) {

    }

    @Override
    public void visit(RefType t) {

    }
}
