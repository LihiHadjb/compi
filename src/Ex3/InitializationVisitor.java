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

    public InitializationVisitor(SearchInContext searchInContext) {
        this.searchInContext = searchInContext;
        isErrorFound = false;
        lastIsInitialized = new HashMap<>();
    }

    public HashMap<String, Boolean> joinIsInitialized(HashMap<String, Boolean> map1, HashMap<String, Boolean> map2) {
//        System.out.println("map1 is: "+map1.toString());
//        System.out.println("map2 is: "+map2.toString());
        HashMap<String, Boolean> result = new HashMap<>();
        for (String var : map1.keySet()) {
            result.put(var, (map1.get(var) && map2.get(var)));
        }
        return result;
    }

    public boolean isErrorFound() {
        return isErrorFound;
    }

    public boolean isInitialized(String varName) {

        //local variable
        if (lastIsInitialized.containsKey(varName)) {
            return lastIsInitialized.get(varName);
        }

        //TODO: we assumed here that each identifier is defined!! make sure its true
        //field
        return true;
    }

    @Override
    public void visit(Program program) {
        //program.mainClass().accept(this);
        if (program.classDecls() != null) {
            for (ClassDecl classDecl : program.classDecls()) {
                classDecl.accept(this);
            }
        }

    }

    @Override
    public void visit(ClassDecl classDecl) {
        this.lastClassSeen = classDecl;
        if (classDecl.methoddecls() != null) {
            for (MethodDecl methodDecl : classDecl.methoddecls()) {
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
        if (methodDecl.formals() != null) {
            for (FormalArg formalArg : methodDecl.formals()) {
                isInitialized.put(formalArg.name(), true);
            }
        }

        if (methodDecl.vardecls() != null) {
            for (VariableIntroduction variableIntroduction : methodDecl.vardecls()) {
                isInitialized.put(variableIntroduction.name(), false);
            }
        }
        this.lastIsInitialized = isInitialized;

        if (methodDecl.body() != null) {
            for (Statement statement : methodDecl.body()) {
                if (statement instanceof IfStatement) {
                    System.out.println("if");
                }
                statement.accept(this);
            }
        }

        if (methodDecl.ret() != null) {
            methodDecl.ret().accept(this);
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
        if (blockStatement.statements() != null) {
            for (Statement statement : blockStatement.statements()) {
                statement.accept(this);
            }
        }
    }

    @Override
    public void visit(IfStatement ifStatement) {
        ifStatement.cond().accept(this);

        HashMap<String, Boolean> originalIsInitialized = new HashMap<>();
        HashMap<String, Boolean> thenIsInitialized = new HashMap<>();
        HashMap<String, Boolean> elseIsInitialized = new HashMap<>();
        originalIsInitialized.putAll(this.lastIsInitialized);

        ifStatement.thencase().accept(this);
        thenIsInitialized.putAll(this.lastIsInitialized);

        this.lastIsInitialized = originalIsInitialized;

        ifStatement.elsecase().accept(this);
        elseIsInitialized.putAll(this.lastIsInitialized);

        this.lastIsInitialized = joinIsInitialized(thenIsInitialized, elseIsInitialized);
    }


    @Override
    public void visit(WhileStatement whileStatement) {
        whileStatement.cond().accept(this);

        HashMap<String, Boolean> originalIsInitialized = new HashMap<>();
        originalIsInitialized.putAll(lastIsInitialized);
        whileStatement.body().accept(this);
        this.lastIsInitialized = originalIsInitialized;
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
        if(!isInitialized(assignArrayStatement.lv())){
            isErrorFound = true;
            return;
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
        e.arrayExpr().accept(this);
        e.indexExpr().accept(this);
    }

    @Override
    public void visit(ArrayLengthExpr e) {
        e.arrayExpr().accept(this);
    }

    @Override
    public void visit(MethodCallExpr e) {
        e.ownerExpr().accept(this);
        if(e.actuals() != null){
            for(Expr actual : e.actuals()){
                actual.accept(this);
            }
        }
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

    @Override
    public void visit(IdentifierExpr e) {
        if(!isInitialized(e.id())){
            isErrorFound = true;
        }
    }

    @Override
    public void visit(ThisExpr e) {
        //do nothing
    }

    @Override
    public void visit(NewIntArrayExpr e) {
        e.lengthExpr().accept(this);
    }

    @Override
    public void visit(NewObjectExpr e) {
        //do nothing
    }

    @Override
    public void visit(NotExpr e) {
        e.e().accept(this);
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
