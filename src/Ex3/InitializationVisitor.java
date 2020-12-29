package Ex3;

import Ex1.SearchInContext;
import ast.*;

import java.util.HashMap;

public class InitializationVisitor implements Visitor {
    SearchInContext searchInContext;
    boolean isErrorFound;
    ClassDecl lastClassSeen;
    MethodDecl lastMethodSeen;
    HashMap<String, Boolean> lastIsInitialized;

    public InitializationVisitor(SearchInContext searchInContext){
        this.searchInContext = searchInContext;
        isErrorFound = false;
        lastIsInitialized = new HashMap<>();
    }

    public HashMap<String, Boolean> joinIsInitialized(HashMap<String, Boolean> map1, HashMap<String, Boolean> map2){
        HashMap<String, Boolean> result = new HashMap<>();
        for(String var : map1.keySet()){
            result.put(var, (map1.get(var) && map2.get(var)));
        }
        return result;
    }

    public boolean isErrorFound() {
        return isErrorFound;
    }

    @Override
    public void visit(Program program) {
        //program.mainClass().accept(this);
        if(program.classDecls() != null){
            for(ClassDecl classDecl : program.classDecls()){
                classDecl.accept(this);
            }
        }

    }

    @Override
    public void visit(ClassDecl classDecl) {
        this.lastClassSeen = classDecl;
        if(classDecl.methoddecls() != null){
            for(MethodDecl methodDecl : classDecl.methoddecls()){
                methodDecl.accept(this);
            }
        }
    }

    @Override
    public void visit(MainClass mainClass) {
        //do nothing
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        this.lastMethodSeen = methodDecl;
        HashMap<String, Boolean> isInitialized = new HashMap<>();
        if(methodDecl.formals() != null){
            for(FormalArg formalArg : methodDecl.formals()){
                isInitialized.put(formalArg.name(), true);
            }
        }

        if(methodDecl.vardecls() != null){
            for(VariableIntroduction variableIntroduction : methodDecl.vardecls()){
                isInitialized.put(variableIntroduction.name(), false);
            }
        }
        this.lastIsInitialized = isInitialized;

        if(methodDecl.body() != null){
            for(Statement statement : methodDecl.body()){
                statement.accept(this);
            }
        }

        if(methodDecl.ret() != null){
            methodDecl.accept(this);
        }
    }

    @Override
    public void visit(FormalArg formalArg) {
        //do nothing
    }

    @Override
    public void visit(VarDecl varDecl) {
        //do nothing
    }



    @Override
    public void visit(BlockStatement blockStatement) {
        HashMap<String, Boolean> originalIsInitialized = this.lastIsInitialized;
        if(blockStatement.statements() != null){
            for(Statement statement : blockStatement.statements()){
                statement.accept(this);
            }
        }
        this.lastIsInitialized = originalIsInitialized;
    }

    @Override
    public void visit(IfStatement ifStatement) {
        ifStatement.cond().accept(this);

        HashMap<String, Boolean> originalIsInitialized = this.lastIsInitialized;
        ifStatement.thencase().accept(this);
        HashMap<String, Boolean> thenIsInitialized = this.lastIsInitialized;
        this.lastIsInitialized = originalIsInitialized;
        ifStatement.elsecase().accept(this);
        HashMap<String, Boolean> elseIsInitialized = this.lastIsInitialized;

        this.lastIsInitialized = joinIsInitialized(thenIsInitialized, elseIsInitialized);


    }

    @Override
    public void visit(WhileStatement whileStatement) {
        whileStatement.cond().accept(this);
        whileStatement.body().accept(this);

    }

    @Override
    public void visit(SysoutStatement sysoutStatement) {
        sysoutStatement.arg().accept(this);
    }

    @Override
    public void visit(AssignStatement assignStatement) {
        assignStatement.rv().accept(this);
        lastIsInitialized.put(assignStatement.lv(), true);
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
        if(!lastIsInitialized.get(assignArrayStatement.lv())){
        //TODO: Stopped here!! check if its a field that is not hidden
            isErrorFound = true;
        }
        assignArrayStatement.index().accept(this);
        assignArrayStatement.rv().accept(this);
    }

    @Override
    public void visit(AndExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(LtExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(AddExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(SubtractExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(MultExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(ArrayAccessExpr e) {
        foo()[4]
        if(lastIsInitialized.get(e.))
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
