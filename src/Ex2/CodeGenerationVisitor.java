package Ex2;

import Ex1.SearchInContext;
import Ex1.SymbolTables.SymbolTable;
import ast.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerationVisitor implements Visitor {
    String GENERAL_METHODS_PATH = "generalMethods";
    String INT_SIZE = "i32";
    String PTR_SIZE = "i8*";
    String INT_ARRAY_SIZE = "i32*";
    String BOOLEAN_SIZE = "i1";

    Integer currRegister;
    Integer currIfIndex;
    Integer currLoopIndex;
    Integer currArrIndex;
    Integer currAndIndex;
    FileWriter fileWriter;
    ClassDecl lastClassSeen;
    MethodDecl lastMethodSeen;
    IdentifierExpr lastIdentifierExprSeen;
    Expr lastOwnerSeen;
    HashMap<String, Vtable> class2vtable;
    HashMap<String, FieldOffsets> class2FieldOffsets;
    SearchInContext searchInContext;
    String lastOwnerRegister;



    public CodeGenerationVisitor(FileWriter fileWriter, HashMap<String, Vtable> class2vtable, HashMap<String, FieldOffsets> class2FieldOffsets, SearchInContext searchInContext){
        this.fileWriter = fileWriter;
        this.currRegister = 0;
        this.currIfIndex = 0;
        this.currArrIndex = 0;
        this.currAndIndex = 0;
        this.currLoopIndex = 0;

        this.class2vtable = class2vtable;
        this.class2FieldOffsets = class2FieldOffsets;
        this.searchInContext = searchInContext;
    }



    //_________GENERAL__________

    public void writeToFile(String text) {
        try{
            this.fileWriter.write("\t" + text);

        }
        catch (IOException e){
            System.out.println("error writing!");
        }

    }

    public void writeToFileWithoutTab(String text) {
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

    public String getNextArrIndex(){
        String result = this.currArrIndex.toString();
        this.currArrIndex++;
        return result;
    }

    public String getNextAndIndex(){
        String result = this.currAndIndex.toString();
        this.currAndIndex++;
        return result;
    }

    public String getNextLoopIndex(){
        String result = this.currLoopIndex.toString();
        this.currLoopIndex++;
        return result;

    }

    public void writeGeneralMethods() {
        String content = "";
        content = "declare i8* @calloc(i32, i32)\n" +
                "declare i32 @printf(i8*, ...)\n" +
                "declare void @exit(i32)\n" +
                "\n" +
                "@_cint = constant [4 x i8] c\"%d\\0a\\00\"\n" +
                "@_cOOB = constant [15 x i8] c\"Out of bounds\\0a\\00\"\n" +
                "define void @print_int(i32 %i) {\n" +
                "\t%_str = bitcast [4 x i8]* @_cint to i8*\n" +
                "\tcall i32 (i8*, ...) @printf(i8* %_str, i32 %i)\n" +
                "\tret void\n" +
                "}\n" +
                "\n" +
                "define void @throw_oob() {\n" +
                "\t%_str = bitcast [15 x i8]* @_cOOB to i8*\n" +
                "\tcall i32 (i8*, ...) @printf(i8* %_str)\n" +
                "\tcall void @exit(i32 1)\n" +
                "\tret void\n" +
                "}\n\n\n";
//        try
//        {
//            content = new String ( Files.readAllBytes( Paths.get(GENERAL_METHODS_PATH)));
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
        writeToFileWithoutTab(content);
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


    public String getNextIfLabelString(){
        return "if" + getNextIfIndex();
    }

    public String getNextArrLabelString(){
        return "arr_alloc" + getNextArrIndex();
    }

    public String getNextAndLabelString(){
        return "andcond" + getNextAndIndex();
    }

    public String getNextLoopLabelString(){
        return "loop" + getNextLoopIndex();
    }

    public String getDottedName(FormalArg formalArg){
        return "%." + formalArg.name();
    }

    public String getUnDottedName(VariableIntroduction variableIntroduction){
        return "%" + variableIntroduction.name();
    }

    public String getUndottedName(IdentifierExpr identifierExpr){
        return "%" + identifierExpr.id();
    }


    public String getFieldOffsetString(String fieldId){
        FieldOffsets currFieldOffsets = this.class2FieldOffsets.get(this.lastClassSeen.name());
        return currFieldOffsets.getIndex(fieldId).toString();
    }

    public String getVarRegisterString(String varName, String varSizeString){
        SymbolTable currentSymbolTable = this.searchInContext.astNodeToSymbolTable().get(this.lastMethodSeen);
        String lvString;

        if(currentSymbolTable.hasVariableWithName(varName)){//formal or varDecl in method
            lvString = "%" + varName;
        }

        else{//field
            String elementPtrReg = getNextRegister();
            String fieldOffsetString = getFieldOffsetString(varName);
            writeToFile(elementPtrReg + " = getelementptr i8" + ", i8* %this, i32 " + fieldOffsetString + "\n");

            String castedElementPtrReg = getNextRegister();
            writeToFile(castedElementPtrReg + " = bitcast i8* " + elementPtrReg + " to " + varSizeString + "*\n");
            lvString = castedElementPtrReg;
        }
        return lvString;
    }

    public String getValueOfExpr(Expr expr){
        String result = null;
        if(expr instanceof IntegerLiteralExpr){
            int num = ((IntegerLiteralExpr) expr).num();
            result = Integer.toString(num);
        }

        else if(expr instanceof TrueExpr){
            result = "1";
        }

        else if(expr instanceof FalseExpr){
            result = "0";
        }

        else if(expr instanceof ThisExpr){ //debug!!
            result = "%this";
        }

        else if(expr instanceof NewObjectExpr){
            expr.accept(this);
            result = lastOwnerRegister;
        }

        else{
            expr.accept(this);
            result = getLastUsedRegister();
        }
        return result;
    }


    //_________VTABLES__________
    public void writeVtable(ClassDecl classDecl) {
        Vtable classVtable = this.class2vtable.get(classDecl.name());

        String intro = "@." + classDecl.name() + "_vtable = global [" + classVtable.methodName2Index.size() + " x i8*] [\n";
        writeToFileWithoutTab(intro);

//        HashMap<String, String> methodName2FormalsString = createMethodName2FormalsString(classVtable);
//        HashMap<String, String> methodName2ReturnString = createMethodName2ReturnString(classVtable);

        HashMap<Integer, String> index2methodName = reverseMap(classVtable.methodName2Index);
        for(int i=0; i<index2methodName.size(); i++){
            String methodName = index2methodName.get(i);
//            String formalString = methodName2FormalsString.get(methodName);
//            String returnTypeString = methodName2ReturnString.get(methodName);
            String nameString = "@" + classVtable.getImplementingClassName(methodName) + "." + methodName;
            String methodLine = PTR_SIZE + " bitcast" + " (" + createMethodSignatureString(classDecl.name(), methodName) + " " + nameString  + " to " + PTR_SIZE +")";
            if(i<=index2methodName.size()-2){
                methodLine = methodLine + ",";
            }
            methodLine = methodLine + "\n";
            writeToFile(methodLine);
        }
        String ending = "]\n\n";
        writeToFileWithoutTab(ending);
    }

    public String createMethodFormalsString(String className, String methodName){
        Vtable classVtable = this.class2vtable.get(className);

        //TODO: we dont really need the whole map
        HashMap<String, String> methodName2FormalsString = createMethodName2FormalsString(classVtable);
        String formalString = methodName2FormalsString.get(methodName);
        return formalString;
    }

    public String createMethodReturnTypeString(String className, String methodName){
        Vtable classVtable = this.class2vtable.get(className);

        //TODO: we dont really need the whole map
        HashMap<String, String> methodName2ReturnString = createMethodName2ReturnString(classVtable);
        String returnTypeString = methodName2ReturnString.get(methodName);
        return returnTypeString;
    }

    public String createMethodSignatureString(String className, String methodName){
        Vtable classVtable = this.class2vtable.get(className);
        //HashMap<String, String> methodName2FormalsString = createMethodName2FormalsString(classVtable);
        //HashMap<String, String> methodName2ReturnString = createMethodName2ReturnString(classVtable);
        String formalString = createMethodFormalsString(className, methodName);
        String returnTypeString = createMethodReturnTypeString(className, methodName);
        return returnTypeString + " " + formalString;
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



    public HashMap<String, String> createMethodName2ReturnString(Vtable classVtable){
        HashMap<String, String> result = new HashMap<>();

        for(Map.Entry<String, MethodDecl> entry : classVtable.getMethodName2MethodDecl().entrySet()){
            MethodDecl methodDecl = entry.getValue();
            result.put(methodDecl.name(), getSizeString(methodDecl.returnType()));
        }
        return result;
    }



    //_________METHOD_DECLS__________
    public String getMethodDeclFormals(MethodDecl methodDecl){
        StringBuilder result = new StringBuilder();
        result.append("(i8* %this");
        if(methodDecl.formals() != null){
            for(FormalArg formalArg : methodDecl.formals()){
                result.append(", " + getVarSizeString(formalArg) + " " + getDottedName(formalArg));
            }
        }
        result.append(")");
        return result.toString();
    }


    //define i32 @Classes.run(i8* %this) {


    public String getMethodDeclName(MethodDecl methodDecl){
        return "@" + this.lastClassSeen.name() + "." + methodDecl.name();
    }





    //_________METHOD_CALLS__________
    public String getLastOwnerSeenClassName(){
        String result;
        //TODO: verify lastOwnerSeen cant be a NewArrayExpr
        if(this.lastOwnerSeen instanceof ThisExpr){
            result = this.lastClassSeen.name();
        }
        else if(this.lastOwnerSeen instanceof NewObjectExpr){
            result = (((NewObjectExpr) this.lastOwnerSeen).classId());
            //"New B().foo()"
        }

        else{//field or local var
            result = searchInContext.lookupVariableType(this.lastMethodSeen, ((IdentifierExpr)this.lastOwnerSeen).id());
        }
        return result;
    }

    //(i8* %_6, i32 1)
    public String createActualsString(String ownerReg, String ownerClassName, String methodName, List<Expr> actuals){
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append("i8* ");
        result.append(ownerReg);

        Vtable ownerVtable = this.class2vtable.get(ownerClassName);
        MethodDecl methodDecl = ownerVtable.methodName2MethodDecl.get(methodName);

//        if(methodDecl.formals() != null && methodDecl.formals().size() > 0){
//            result.append(", ");
//        }

        String actualVal;
        int i = 0;
        if(methodDecl.formals() != null){
            for(FormalArg formalArg : methodDecl.formals()){
                actualVal = getValueOfExpr(actuals.get(i));
                result.append(", ");
                result.append(getSizeString(formalArg.type()));
                result.append(" ");
                result.append(actualVal);
                i++;
            }
        }
        result.append(")");
        return result.toString();

    }


    //_________ARRAYS__________
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

    public String verifyArrayAccess(String arrayReg, Expr index){
        //Check that the index is greater than zero
        String indexReg = getValueOfExpr(index);
        String indexGTZeroReg = getNextRegister();
        writeToFile(indexGTZeroReg + " = icmp slt i32 " + indexReg + ", 0\n");
        String gtzTrueLabel = getNextArrLabelString();
        String gtzFalseLabel = getNextArrLabelString();
        writeToFile("br i1 " + indexGTZeroReg + ", label %" + gtzTrueLabel + ", label %" + gtzFalseLabel + "\n");
        writeToFileWithoutTab(gtzTrueLabel + ":\n");
        writeToFile("call void @throw_oob()\n");
        writeToFile("br label %" + gtzFalseLabel +"\n");
        writeToFileWithoutTab(gtzFalseLabel + ":\n");


        //Check that the index is less than the size of the array
        String arrSizePtrReg = getNextRegister();
        writeToFile(arrSizePtrReg + " = getelementptr i32, i32* " + arrayReg + ", i32 0\n");
        String arrSizeReg = getNextRegister();
        writeToFile(arrSizeReg + " = load i32, i32* " + arrSizePtrReg + "\n");

        String indexLTSizeReg = getNextRegister();
        writeToFile(indexLTSizeReg + " = icmp sle i32 " + arrSizeReg + ", " + indexReg + "\n");

        String ltSizeTrueLabel = getNextArrLabelString();
        String ltSizeFalseLabel = getNextArrLabelString();
        writeToFile("br i1 " + indexLTSizeReg + ", label %" + ltSizeTrueLabel + ", label %" + ltSizeFalseLabel + "\n");
        writeToFileWithoutTab(ltSizeTrueLabel + ":\n");
        writeToFile("call void @throw_oob()\n");
        writeToFile("br label %" + ltSizeFalseLabel +"\n");
        writeToFileWithoutTab(ltSizeFalseLabel + ":\n");

        return indexReg;

    }

    public String getRealElemPtrReg(String indexReg, String arrayReg){
        String realIndexReg = getNextRegister();
        writeToFile(realIndexReg + " = add i32 " + indexReg + ", 1\n");
        String realElemPtrReg = getNextRegister();
        writeToFile(realElemPtrReg + " = getelementptr i32, i32* " + arrayReg + ", i32 " + realIndexReg + "\n");
        return realElemPtrReg;
    }

    public String getRealElemPtrReg_NoAdding1(String indexReg, String arrayReg){
        //String realIndexReg = getNextRegister();
        //writeToFile(realIndexReg + " = add i32 " + indexReg + " 1\n");
        String realElemPtrReg = getNextRegister();
        writeToFile(realElemPtrReg + " = getelementptr i32, i32* " + arrayReg + ", i32 0\n");
        return realElemPtrReg;
    }





    @Override
    public void visit(Program prog){

        if (prog.classDecls() != null){
            for (ClassDecl classDecl : prog.classDecls()){
                writeVtable(classDecl);

            }
        }

        writeGeneralMethods();

        if (prog.mainClass() != null){
            prog.mainClass().accept(this); // visit(prog.mainClass());
        }
        if (prog.classDecls() != null){
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
        writeToFileWithoutTab("define i32 @main() {\n");
        mainClass.mainStatement().accept(this);
        writeToFile("ret i32 0\n");
        writeToFileWithoutTab("}\n\n");
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        this.currRegister = 0;
//        this.currIfIndex = 0;
//        this.currArrIndex = 0;
//        this.currAndIndex = 0;
//        this.currLoopIndex = 0;
        this.lastMethodSeen = methodDecl;
        String intro = "define " + getSizeString(methodDecl.returnType()) + " " + getMethodDeclName(methodDecl) + getMethodDeclFormals(methodDecl) + "{\n";
        writeToFileWithoutTab(intro);
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
            String returnValue = getValueOfExpr(methodDecl.ret());
            String returnSizeString = getSizeString(methodDecl.returnType());
            writeToFile("ret " + returnSizeString + " " + returnValue + "\n");
        }
        writeToFileWithoutTab("}\n\n");
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

        writeToFileWithoutTab(trueLabel + ":\n");
        ifStatement.thencase().accept(this);
        writeToFile("br label %" + finalLabel + "\n");

        writeToFileWithoutTab(falseLabel + ":\n");
        ifStatement.elsecase().accept(this);
        writeToFile("br label %" + finalLabel + "\n");


        writeToFileWithoutTab(finalLabel + ":\n");

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
        String condLabel = getNextLoopLabelString();
        String trueLabel = getNextLoopLabelString();
        String falseLabel = getNextLoopLabelString();
        writeToFile("br label %"+ condLabel + "\n");
        writeToFileWithoutTab(condLabel + ":\n");
        whileStatement.cond().accept(this);
        writeToFile("br "+ BOOLEAN_SIZE + " " + getLastUsedRegister() + ", label %" + trueLabel + ", label %" + falseLabel + "\n");


        writeToFileWithoutTab(trueLabel + ":\n");
        if(whileStatement.body() != null){
            whileStatement.body().accept(this);
        }
        writeToFile("br label %" + condLabel + "\n");

        writeToFileWithoutTab(falseLabel + ":\n");
    }



    //	call void (i32) @print_int(i32 0)
    @Override
    public void visit(SysoutStatement sysoutStatement) {
        String argString = getValueOfExpr(sysoutStatement.arg());
        writeToFile("call void (i32) @print_int(i32 " + argString + ")\n");
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
        String rvString = getValueOfExpr(assignStatement.rv());
        String lvString = getVarRegisterString(assignStatement.lv(), varSizeString);
        writeToFile("store " + varSizeString + " " + rvString + ", " + varSizeString + "* " + lvString + "\n");
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {

//        //verify the index
        String varSizeString = INT_ARRAY_SIZE;
        String lvString = getVarRegisterString(assignArrayStatement.lv(), varSizeString);
        String arrayReg = getNextRegister();
        writeToFile(arrayReg + " = load i32*, i32** " + lvString + "\n");
        String indexReg = verifyArrayAccess(arrayReg, assignArrayStatement.index()); //debug!

        String realElemPtrReg = getRealElemPtrReg(indexReg, arrayReg);
        String rvString = getValueOfExpr(assignArrayStatement.rv());
        writeToFile("store i32 " + rvString + ", i32* " + realElemPtrReg + "\n");

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




//    %_0 = load i1, i1* %b
//    br label %andcond0
//    andcond0:
//    ; Check result, short circuit if false
//    br i1 %_0, label %andcond1, label %andcond3
//    andcond1:
//            %_1 = load i1, i1* %c
//    br label %andcond2
//    andcond2:
//    ; this label seems redundant here and we could have used %andcond1 instead -
//    ; but this becomes useful when compiling expressions a && b && c (hint!)
//    br label %andcond3
//    andcond3:
//    ; Get appropriate value, depending on the predecessor block
//	%_2 = phi i1 [0, %andcond0], [%_1, %andcond2]
    @Override
    public void visit(AndExpr e) {
        String e1String = getValueOfExpr(e.e1());
        String and0Label = getNextAndLabelString();
        String and1Label = getNextAndLabelString();
        String and2Label = getNextAndLabelString();
        String and3Label = getNextAndLabelString();

        writeToFile("br label %" + and0Label + "\n");
        writeToFileWithoutTab(and0Label + ":\n");
        writeToFile("br i1 " + e1String + ", label %" + and1Label + ", label %" + and3Label + "\n");

        writeToFileWithoutTab(and1Label + ":\n");
        String e2String = getValueOfExpr(e.e2());
        writeToFile("br label %" + and2Label + "\n");

        writeToFileWithoutTab(and2Label + ":\n");
        writeToFile("br label %" + and3Label + "\n");

        writeToFileWithoutTab(and3Label + ":\n");
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = phi i1 [0, %" + and0Label + "], [" + e2String + ", %" + and2Label + "]\n");
    }

    //	%_4 = icmp slt i32 %_2, %_3
    @Override
    public void visit(LtExpr e) {
        String e1Val = getValueOfExpr(e.e1());
        String e2Val = getValueOfExpr(e.e2());
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = icmp slt i32 " + e1Val + ", " + e2Val + "\n");

    }

    //%_13 = add i32 %_8, 1
    @Override
    public void visit(AddExpr e) {
        String e1Val = getValueOfExpr(e.e1());
        String e2Val = getValueOfExpr(e.e2());
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = add i32 " + e1Val + ", " + e2Val + "\n");

    }

    @Override
    public void visit(SubtractExpr e) {
        String e1Val = getValueOfExpr(e.e1());
        String e2Val = getValueOfExpr(e.e2());
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = sub i32 " + e1Val + ", " + e2Val + "\n");

    }

    @Override
    public void visit(MultExpr e) {
        String e1Val = getValueOfExpr(e.e1());
        String e2Val = getValueOfExpr(e.e2());
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = mul i32 " + e1Val + ", " + e2Val + "\n");

    }

//
//    ; Similar code, but this time we just load x[0] instead of storing to it
//    ; In either case, we need to perform an OOB check
//	%_18 = load i32*, i32** %x
//	%_19 = icmp slt i32 0, 0
//    br i1 %_19, label %arr_alloc10, label %arr_alloc11
//    arr_alloc10:
//    call void @throw_oob()
//    br label %arr_alloc11
//    arr_alloc11:
//        %_20 = getelementptr i32, i32* %_18, i32 0
//        %_21 = load i32, i32* %_20
//	%_22 = icmp sle i32 %_21, 0
//    br i1 %_22, label %arr_alloc12, label %arr_alloc13
//    arr_alloc12:
//    call void @throw_oob()
//    br label %arr_alloc13
//    arr_alloc13:
//        %_23 = add i32 0, 1
//        %_24 = getelementptr i32, i32* %_18, i32 %_23
//	%_25 = load i32, i32* %_24
    @Override
    public void visit(ArrayAccessExpr e) {
        //TODO: verify Lihi

        e.arrayExpr().accept(this);
        //verify the index
//        String varSizeString = PTR_SIZE;
//        String lvString = getVarRegisterString(this.lastIdentifierExprSeen.id(), varSizeString);
//        String arrayReg = getNextRegister();
        String arrayReg = getLastUsedRegister();
        String indexReg = verifyArrayAccess(arrayReg, e.indexExpr());

        //assign
        String realElemPtrReg = getRealElemPtrReg(indexReg, arrayReg);
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = load i32, i32* " + realElemPtrReg + "\n");

//        %_5 = getelementptr i8, i8* %this, i32 8
//                %_6 = bitcast i8* %_5 to i32**
//	%_7 = load i32*, i32** %_6
//                %_8 = getelementptr i8, i8* %this, i32 8 ----NO!
//                %_9 = bitcast i8* %_8 to i8**       ---- NO!
    }

    @Override
    public void visit(ArrayLengthExpr e) {
        e.arrayExpr().accept(this);
        //verify the index
        String varSizeString = PTR_SIZE;
//        String lvString = getVarRegisterString(this.lastIdentifierExprSeen.id(), varSizeString);
        String arrayReg = getLastUsedRegister();

        //get the length
//        String lenPtrReg = getNextRegister();
//        writeToFile(lenPtrReg + " = getelementptr i32, i32* " + arrayReg + "i32 0");
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = load i32, i32* " + arrayReg + "\n");
    }








//    ; This segment implements the b.set(1) call
//
//        ; First load the object pointer from the stack variable for b
//	%_6 = load i8*, i8** %b
//
//    ; Do the required bitcasts, so that we can access the vtable pointer - we're holding a pointer to i8**
//        %_7 = bitcast i8* %_6 to i8***
//
//    ; Load vtable_ptr
//	%_8 = load i8**, i8*** %_7
//
//    ; Get a pointer to the 0-th entry in the vtable.
//    ; The index here is exactly the offset corresponding to Base::set.
//	%_9 = getelementptr i8*, i8** %_8, i32 0
//
//    ; Read into the array to get the actual function pointer
//	%_10 = load i8*, i8** %_9
//
//    ; Cast the function pointer from i8* to a function ptr type that matches the function's signature,
//    ; so that we can call it.
//        %_11 = bitcast i8* %_10 to i32 (i8*, i32)*
//
//    ; Perform the call on the function pointer. Note that the first argument is the receiver object ("this").
//        %_12 = call i32 %_11(i8* %_6, i32 1)


    @Override
    public void visit(MethodCallExpr e) {
        //TODO:verify Lihi
        e.ownerExpr().accept(this);
        String ownerReg = this.lastOwnerRegister;
        String ownerRegCasted = getNextRegister();
        writeToFile(ownerRegCasted + " = bitcast i8* " + ownerReg + " to i8***" + "\n");

        String vtablePtrReg = getNextRegister();
        writeToFile(vtablePtrReg + " = load i8**, i8*** " + ownerRegCasted + "\n");

        //The next line is in order to know which "className.methodName" should be called.
        //We called ownerExpr.accept, so now this.lastOwnerSeen is set to this call's owner.
        String ownerClassName = getLastOwnerSeenClassName();
        Vtable ownerVtable = this.class2vtable.get(ownerClassName);
        Integer methodIndex = ownerVtable.getIndex(e.methodId());

        String vtableMethodEntryPtr = getNextRegister();
        writeToFile(vtableMethodEntryPtr + " = getelementptr i8*, i8** " + vtablePtrReg + ", i32 " + methodIndex + "\n");

        String methodPtr = getNextRegister();
        writeToFile(methodPtr + " = load i8*, i8** " + vtableMethodEntryPtr + "\n");

        String methodPtrCasted = getNextRegister();

        String methodName = e.methodId();
        writeToFile(methodPtrCasted + " = bitcast i8* " + methodPtr + " to " + createMethodSignatureString(ownerClassName, methodName) + "\n");

        //Stopped here. should fix last line!
        String returnSize = createMethodReturnTypeString(ownerClassName, methodName);
        String actualsString = createActualsString(ownerReg, ownerClassName, methodName, e.actuals());
        String returnReg = getNextRegister();
        writeToFile(returnReg + " = call " + returnSize + " " + methodPtrCasted + actualsString + "\n");
    }

    @Override
    public void visit(IntegerLiteralExpr e) {
        //do nothing

    }

    @Override
    public void visit(TrueExpr e) {
        //do nothing
    }

    @Override
    public void visit(FalseExpr e) {
        //do nothing
    }

    //%_27 = load i32, i32* %aux01
    @Override
    public void visit(IdentifierExpr e) {
        this.lastIdentifierExprSeen = e;
        this.lastOwnerSeen = e;
        //TODO: verify Lihi!!
        String varSizeString = getSizeString(searchInContext.lookupVarAstType(this.lastMethodSeen, e.id()));
        //TODO: maybe getting the size should be inside getVarRegisterString()
        String identifierPtrReg = getVarRegisterString(e.id(), varSizeString);
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = load " + varSizeString + ", " + varSizeString + "* " + identifierPtrReg + "\n");
        this.lastOwnerRegister = resultReg;
    }

    @Override
    public void visit(ThisExpr e) {
        //TODO: verify that its not possible to do "this.field" (only "this.foo(...)")
        this.lastOwnerSeen = e;
        this.lastOwnerRegister = "%this";
    }




//    ; Check that the size of the array is not negative
//	%_0 = icmp slt i32 2, 0
//    br i1 %_0, label %arr_alloc0, label %arr_alloc1
//
//    arr_alloc0:
//    ; Size was negative, throw negative size exception
//    call void @throw_oob()
//
//    br label %arr_alloc1
//
//    arr_alloc1:
//    ; All ok, we can proceed with the allocation
//
//    ; Calculate size bytes to be allocated for the array (new arr[sz] -> add i32 1, sz)
//    ; -> We need an additional int worth of space, to store the size of the array.
//        %_1 = add i32 2, 1
//
//    ; Allocate sz + 1 integers (4 bytes each)
//	%_2 = call i8* @calloc(i32 4, i32 %_1)
//
//    ; Cast the returned pointer
//	%_3 = bitcast i8* %_2 to i32*
//
//    ; Store the size of the array in the first position of the array
//    store i32 2, i32* %_3
//
//    ; This concludes the array allocation (new int[2])
//
//        ; Assign the array pointer to x
//    store i32* %_3, i32** %x
    @Override
    public void visit(NewIntArrayExpr e) {
        //Check that the size of the array is not negative
        String lengthString = getValueOfExpr(e.lengthExpr());
        String sizeCheckReg = getNextRegister();
        writeToFile(sizeCheckReg + " = icmp slt i32 " + lengthString + ", 0\n");

        String checkTrueLabel = getNextArrLabelString();
        String checkFalseLabel = getNextArrLabelString();
        writeToFile("br i1 "+ sizeCheckReg + ", label %" + checkTrueLabel + ", label %" + checkFalseLabel + "\n");

        writeToFileWithoutTab(checkTrueLabel + ":\n");
        writeToFile("call void @throw_oob()\n");

        writeToFile("br label %" + checkFalseLabel + "\n");

        writeToFileWithoutTab(checkFalseLabel + ":\n");

        //calculate the length
//        String lengthReg = getValueOfExpr(e.lengthExpr());
        String lengthWithExtra = getNextRegister();
        writeToFile(lengthWithExtra + " = add i32 " + lengthString + ", 1\n");

        //allocate
        String arrReg = getNextRegister();
        writeToFile(arrReg + " = call i8* @calloc(i32 4, i32 " + lengthWithExtra + ")\n");
        String arrRegCasted = getNextRegister();
        writeToFile(arrRegCasted + " = bitcast i8* " + arrReg + " to i32*\n");

        //Store the size of the array in the first position of the array
        writeToFile("store i32 " + lengthString + ", i32* " + arrRegCasted + "\n");
    }



//    ; The following sequence of instructions creates a new Base object
//
//        ; First, we allocate the required memory on heap for our object.
//    ; We call calloc to achieve this:
//    ;   * The first argument is the amount of objects we want to allocate
//        ;     (always 1 for object allocation, but handy in arrays)
//    ;   * The second argument is the size of the object. This is calculated as the sum of the
//        ;     size of the fields of the class and all the super classes PLUS 8 bytes, to account for
//    ;     the vtable pointer.
//    ; In our case, we have a single int field so it's 4 + 8 = 12 bytes
//        %_0 = call i8* @calloc(i32 1, i32 12)
//
//    ; Next we need to set the vtable pointer to point to the correct vtable (Base_vtable)
//        ; First we bitcast the object pointer from i8* to i8***
//    ; This is done because:
//    ;   -> The vtable stores values of type i8*.
//    ;   -> Thus, a pointer that points to the start of the vtable (equivalently at the first entry
//        ;      of the vtable) must have type i8**.
//    ;   -> Thus, to set the vtable pointer at the start of the object, we need to have its address
//        ;      (first byte of the object) in a register of type i8***
//    ;		- it's a pointer to a location where we will be storing i8**.
//        %_1 = bitcast i8* %_0 to i8***
//
//    ; Get the address of the first element of the Base_vtable
//        ; The getelementptr arguments are as follows:
//    ;   * The first argument is the type of elements our Base_vtable ptr points to.
//    ;   * The second argument is the Base_vtable ptr.
//    ;   * The third and fourth arguments are indexes
//    ;; (alternative to getelementpr: %_2 = bitcast [2 x i8*]* @.Base_vtable to i8**)
//        %_2 = getelementptr [2 x i8*], [2 x i8*]* @.Base_vtable, i32 0, i32 0

//
//    ; Set the vtable to the correct address.
//    store i8** %_2, i8*** %_1
    @Override
    public void visit(NewObjectExpr e) {
        this.lastOwnerSeen = e;
        //%_0 = call i8* @calloc(i32 1, i32 12)
        String locReg = getNextRegister();
        this.lastOwnerRegister = locReg;
        FieldOffsets fieldOffsets = this.class2FieldOffsets.get(e.classId());
        int objectSize = fieldOffsets.getLast_index();
        writeToFile(locReg + " = call i8* @calloc(i32 1, i32 "+ objectSize + ")\n");

        //%_1 = bitcast i8* %_0 to i8***
        String locRegCasted = getNextRegister();
        writeToFile(locRegCasted + " = bitcast i8* " + locReg + " to i8***\n");

        //%_2 = getelementptr [2 x i8*], [2 x i8*]* @.Base_vtable, i32 0, i32 0
        Vtable vtable = this.class2vtable.get(e.classId());
        String vtableReg = getNextRegister();
        String sizes = "[" + vtable.methodName2Index.size() + " x i8*]";
        String vtableName = "@." + e.classId() + "_vtable";
        writeToFile(vtableReg + " = getelementptr " + sizes + ", " + sizes + "* " + vtableName + ", i32 0, i32 0\n");

        // Set the vtable to the correct address.
        // store i8** %_2, i8*** %_1
        writeToFile("store i8** " + vtableReg + ", i8*** " + locRegCasted + "\n");
    }

    @Override
    public void visit(NotExpr e) {
        e.e().accept(this);
        String exprReg = getLastUsedRegister();
        String resultReg = getNextRegister();
        writeToFile(resultReg + " = sub i1 1" + ", " + exprReg + "\n");
    }

    @Override
    public void visit(IntAstType t) {
        //do nothing
    }

    @Override
    public void visit(BoolAstType t) {
        //do nothing
    }

    @Override
    public void visit(IntArrayAstType t) {
        //do nothing
    }

    @Override
    public void visit(RefType t) {
        //do nothing
    }
}
