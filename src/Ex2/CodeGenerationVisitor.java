package Ex2;

import Ex1.SearchInContext;
import Ex1.SymbolTables.SymbolTable;
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
    Integer currIfIndex;
    FileWriter fileWriter;
    ClassDecl lastClassSeen;
    MethodDecl lastMethodSeen;
    HashMap<String, Vtable> class2vtable;
    HashMap<String, FieldOffsets> class2FieldOffsets;
    SearchInContext searchInContext;



    public CodeGenerationVisitor(FileWriter fileWriter, HashMap<String, Vtable> class2vtable, HashMap<String, FieldOffsets> class2FieldOffsets, SearchInContext searchInContext){
        this.fileWriter = fileWriter;
        this.currRegister = 0;
        this.class2vtable = class2vtable;
        this.class2FieldOffsets = class2FieldOffsets;
        this.searchInContext = searchInContext;
    }

    public void writeToFile(String text) {
        try{
            this.fileWriter.write(text);

        }
        catch (IOException e){
            System.out.println("error writing!");
        }

    }


    public String getNextRegister(){
        String result = "%_" + this.currRegister.toString();
        this.currRegister++;
        return result;
    }

    public String getLastUsedRegister(){
        Integer lastUsedRegister = this.currRegister - 1;
        String result = "%_" + lastUsedRegister.toString();
        return result;
    }

    public String getNextIfIndex(){
        String result = this.currIfIndex.toString();
        this.currIfIndex++;
        return result;

    }

    public void writeGeneralMethods() {
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

    public String getVarSizeString(VariableIntroduction variableIntroduction){
        AstType astType = variableIntroduction.type();
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
                    methodFormalsString.append(", " + getVarSizeString(formalArg));
                }
            }
            methodFormalsString.append(")*");
            result.put(methodDecl.name(), methodFormalsString.toString());
        }
        return result;
    }

    public void writeVtable(ClassDecl classDecl) {
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

    public void writeMethod(String methodName){

    }

    @Override
    public void visit(Program prog){
        writeGeneralMethods();
        if (prog.mainClass() != null){
            prog.mainClass().accept(this); // visit(prog.mainClass());
        }
        if (prog.classDecls() != null){
            for (ClassDecl classDecl : prog.classDecls()){
                writeVtable(classDecl);

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
        //TODO: verify this should be i32 even though main returns void
        writeToFile("define i32 @main() {\n");
        mainClass.mainStatement().accept(this);
        writeToFile("ret i32 0\n");
        writeToFile("}");
    }

    //define i32 @Classes.run(i8* %this) {


    public String getMethodDeclName(MethodDecl methodDecl){
        return "@" + this.lastClassSeen.name() + "." + methodDecl.name();
    }

    public String getDottedName(FormalArg formalArg){
        return "%." + formalArg.name();
    }

    public String getUnDottedName(VariableIntroduction variableIntroduction){
        return "%" + variableIntroduction.name();
    }

    public String getMethodDeclFormals(MethodDecl methodDecl){
        StringBuilder result = new StringBuilder();
        result.append("(i8* %this");
        if(methodDecl.formals() != null){
            for(FormalArg formalArg : methodDecl.formals()){
                result.append(", " + getVarSizeString(formalArg) + getDottedName(formalArg));
            }
        }
        result.append(")");
        return result.toString();



    }

    @Override
    public void visit(MethodDecl methodDecl) {
        this.currRegister = 0;
        this.lastMethodSeen = methodDecl;
        String intro = "define " + getSizeString(methodDecl.returnType()) + " " + getMethodDeclName(methodDecl) + getMethodDeclFormals(methodDecl) + "{\n";
        writeToFile(intro);
        if(methodDecl.formals() != null){
            for(FormalArg formalArg : methodDecl.formals()){
                formalArg.accept(this);
            }
        }
        if(methodDecl.vardecls() != null){
            for(VarDecl varDecl : methodDecl.vardecls()){
                varDecl.accept(this);
            }
        }

        if(methodDecl.body() != null){
            for (Statement statement : methodDecl.body()){
                statement.accept(this);
            }
        }

        if(methodDecl.ret() != null){
            methodDecl.ret().accept(this);
        }
        writeToFile("}\n");
    }


    @Override
    public void visit(FormalArg formalArg) {
        String size = getSizeString(formalArg.type());
        String undotted = getUnDottedName(formalArg);
        String dotted = getDottedName(formalArg);
        writeToFile(undotted + " = alloca " +size + "\n");
        writeToFile("store " + size + " " + dotted +", " + size + "* " + undotted + "\n");
    }

    //	%x = alloca i32
    @Override
    public void visit(VarDecl varDecl) {
        writeToFile(getUnDottedName(varDecl) + " = alloca " + getVarSizeString(varDecl) + "\n");

    }

    @Override
    public void visit(BlockStatement blockStatement) {
        if(blockStatement.statements() != null){
            for(Statement statement  : blockStatement.statements()){
                statement.accept(this);
            }
        }

    }

//    	%x = alloca i32
//    store i32 10, i32* %x

//	%_0 = load i32, i32* %x
//	%_1 = icmp slt i32 %_0, 2
//    br i1 %_1, label %if0, label %if1
//    if0:
//    call void (i32) @print_int(i32 0)
//    br label %if2
//    if1:
//    call void (i32) @print_int(i32 1)
//    br label %if2
//    if2:
//    ret i32 0

    public String getNextIfLabelString(){
        return "if" + getNextIfIndex();
    }

    @Override
    public void visit(IfStatement ifStatement) {
        ifStatement.cond().accept(this);

        //branch line
        String trueLabel = getNextIfLabelString();
        String falseLabel = getNextIfLabelString();
        String finalLabel = getNextIfLabelString();
        StringBuilder branchString = new StringBuilder();
        branchString.append("br i1 ");
        branchString.append(getLastUsedRegister());
        branchString.append(", ");
        branchString.append("label %" + trueLabel);
        branchString.append(", ");
        branchString.append("label %" + falseLabel);
        branchString.append("\n");
        writeToFile(branchString.toString());

        writeToFile(trueLabel + ":\n");
        ifStatement.thencase().accept(this);

        writeToFile(falseLabel + ":\n");
        ifStatement.elsecase().accept(this);

        writeToFile(finalLabel + ":\n");

    }

//    br label %loop0
//    loop0:
//            %_2 = load i32, i32* %count02
//	%_3 = load i32, i32* %aux03
//	%_4 = icmp slt i32 %_2, %_3
//    br i1 %_4, label %loop1, label %loop2
//    loop1:
//            %_5 = load i32, i32* %count01
//	%_6 = add i32 %_5, 1
//    store i32 %_6, i32* %count01
//
//	%_7 = load i32, i32* %count02
//	%_8 = add i32 %_7, 2
//    store i32 %_8, i32* %count02
//
//    br label %loop0
//    loop2:


    @Override
    public void visit(WhileStatement whileStatement) {
        String condLabel = getNextLoopLabel();
        String trueLabel = getNextLoopLabel();
        String falseLabel = getNextLoopLabel();
        writeToFile("br label "+ condLabel + "\n");
        writeToFile(condLabel + ":\n");
        whileStatement.cond().accept(this);
        writeToFile("br "+ BOOLEAN_SIZE + " " + getLastUsedRegister() + ", label %" + trueLabel + ", label %" + falseLabel + "\n");


        writeToFile(trueLabel + ":\n");
        if(whileStatement.body() != null){
            whileStatement.body().accept(this);
        }
        writeToFile("br label %" + condLabel + "\n");

        writeToFile(falseLabel + ":\n");


    }

    public String getValueOfExpr(Expr expr){
        String result;
        if(expr instanceof IntegerLiteralExpr){
            result = String.valueOf(((IntegerLiteralExpr) expr).num());
        }

        else if(expr instanceof TrueExpr){
            result = "1";
        }

        else if(expr instanceof FalseExpr){
            result = "0";
        }

        else{
            expr.accept(this);
            result = getLastUsedRegister();
        }
        return result;
    }

    //	call void (i32) @print_int(i32 0)
    @Override
    public void visit(SysoutStatement sysoutStatement) {
        String argString = getValueOfExpr(sysoutStatement.arg());
        writeToFile("call void (i32) @print_int(i32 " + argString + ")\n");
    }

    public String getFieldOffsetString(String fieldId){
        FieldOffsets currFieldOffsets = this.class2FieldOffsets.get(this.lastClassSeen);
        return currFieldOffsets.getIndex(fieldId).toString();
    }

    public String getVarRegisterString(String varName, String varSizeString){
        SymbolTable currentSymbolTable = this.searchInContext.astNodeToSymbolTable().get(this.lastMethodSeen);
        String lvString;

        if(currentSymbolTable.hasVariableWithName(varName)){
            lvString = "%" +varName;
        }

        else{
            String elementPtrReg = getNextRegister();
            String fieldOffsetString = getFieldOffsetString(varName);
            writeToFile(elementPtrReg + " = getelementptr " + PTR_SIZE + ", i8* %this, " + varSizeString + " " + fieldOffsetString + "\n");

            String castedElementPtrReg = getNextRegister();
            writeToFile(castedElementPtrReg + " = bitcast i8* " + elementPtrReg + " to " + varSizeString + "*\n");
            lvString = castedElementPtrReg;
        }
        return lvString;
    }




    //        ; Get pointer to the byte where the field starts
//        ; Here the index is 8 because we're accessing the first field,
//        ; which resides immediately after the vtable pointer
//                %_1 = getelementptr i8, i8* %this, i32 8
//
//        ; Cast to a pointer to the field with the correct type
//                %_2 = bitcast i8* %_1 to i32*
//
//        ; Write to the field
//        store i32 %_0, i32* %_2
//
//        ; Now the same process for reading the field (load instead of store in the end)
//                %_3 = getelementptr i8, i8* %this, i32 8
//                %_4 = bitcast i8* %_3 to i32*
//	%_5 = load i32, i32* %_4


    //store i32 10, i32* %x
    @Override
    public void visit(AssignStatement assignStatement) {
        String varSizeString = getSizeString(searchInContext.lookupVarAstType(this.lastMethodSeen, assignStatement.lv()));
        String lvString = getVarRegisterString(assignStatement.lv(), varSizeString);
        String rvString = getValueOfExpr(assignStatement.rv());
        writeToFile("store " + varSizeString + " " + rvString + ", " + varSizeString + "* " + lvString + "\n");
    }



//    ; The following segment implements the array store x[0] = 1
//
//    ; Load the address of the x array
//	%_4 = load i32*, i32** %x
//
//    ; Check that the index is greater than zero
//	%_5 = icmp slt i32 0, 0
//    br i1 %_5, label %arr_alloc2, label %arr_alloc3
//    arr_alloc2:
//    ; Else throw out of bounds exception
//    call void @throw_oob()
//    br label %arr_alloc3
//
//    arr_alloc3:
//    ; Load the size of the array (first integer of the array)
//	%_6 = getelementptr i32, i32* %_4, i32 0
//            %_7 = load i32, i32* %_6
//
//    ; Chech that the index is less than the size of the array
//	%_8 = icmp sle i32 %_7, 0
//    br i1 %_8, label %arr_alloc4, label %arr_alloc5
//
//    arr_alloc4:
//    ; Else throw out of bounds exception
//    call void @throw_oob()
//    br label %arr_alloc5
//
//    arr_alloc5:
//    ; All ok, we can safely index the array now
//
//            ; We'll be accessing our array at index + 1, since the first element holds the size
//            %_9 = add i32 0, 1
//
//    ; Get pointer to the i + 1 element of the array
//	%_10 = getelementptr i32, i32* %_4, i32 %_9
//
//    ; And store 1 to that address *%_10 = 1
//    store i32 1, i32* %_10

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
        //verify the index
        String varSizeString = getSizeString(searchInContext.lookupVarAstType(this.lastMethodSeen, assignArrayStatement.lv()));
        String lvString = getVarRegisterString(assignArrayStatement.lv(), varSizeString);
        String arrayReg = getNextRegister();
        writeToFile(arrayReg + " = load i32*, i32** " + lvString + "\n");

        //Check that the index is greater than zero
        String indexGTZeroReg = getNextRegister();
        String indexReg = getValueOfExpr(assignArrayStatement.index());
        writeToFile(indexGTZeroReg + " = icmp slt i32 " + indexReg + ", 0\n");
        String gtzTrueLabel = getNextArrLabel();
        String gtzFalseLabel = getNextArrLabel();
        writeToFile("br i1 " + indexGTZeroReg + ", label %" + gtzTrueLabel + ", label %" + gtzFalseLabel + "\n");
        writeToFile(gtzTrueLabel + ":\n");
        writeToFile("call void @throw_oob()\n");
        writeToFile("br label %" + gtzFalseLabel +"\n");
        writeToFile(gtzFalseLabel + ":\n");
        String arrSizePtrReg = getNextRegister();
        writeToFile(arrSizePtrReg + " = getelementptr i32, i32* " + arrayReg + ", i32 0\n");
        String arrSizeReg = getNextRegister();
        writeToFile(arrSizeReg + " = load i32, i32* " + arrSizePtrReg + "\n");

        //Check that the index is less than the size of the array
        String indexLTSizeReg = getNextRegister();
        writeToFile(indexLTSizeReg + " = icmp sle i32 " + arrSizePtrReg + ", " + indexReg + "\n");

        String ltSizeTrueLabel = getNextArrLabel();
        String ltSizeFalseLabel = getNextArrLabel();
        writeToFile("br i1 " + indexLTSizeReg + ", label %" + ltSizeTrueLabel + ", label %" + ltSizeFalseLabel + "\n");
        writeToFile(ltSizeTrueLabel + ":\n");
        writeToFile("call void @throw_oob()\n");
        writeToFile("br label %" + ltSizeFalseLabel +"\n");
        writeToFile(ltSizeFalseLabel + ":\n");
        // Assign
        String realIndexReg = getNextRegister();
        writeToFile(realIndexReg + " = add i32 " + indexReg + " 1\n");
        String realElemPtrReg = getNextRegister();
        writeToFile(realElemPtrReg + " = getelementptr i32, i32* " + arrayReg + "i32 " + realElemPtrReg);

        String rvString = getValueOfExpr(assignArrayStatement.rv());
        writeToFile("store i32 " + rvString + ", i32* " + realElemPtrReg + "\n");
        //TODO: stopped here. need to implement getNextArrLabel(), getNextLoopLabel





//	%_8 = icmp sle i32 %_7, 0
//    br i1 %_8, label %arr_alloc4, label %arr_alloc5
//
//    arr_alloc4:
//    ; Else throw out of bounds exception
//    call void @throw_oob()
//    br label %arr_alloc5
//
//    arr_alloc5:
//    ; All ok, we can safely index the array now
//
//            ; We'll be accessing our array at index + 1, since the first element holds the size
//            %_9 = add i32 0, 1
//
//    ; Get pointer to the i + 1 element of the array
//	%_10 = getelementptr i32, i32* %_4, i32 %_9
//
//    ; And store 1 to that address *%_10 = 1
//    store i32 1, i32* %_10





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
