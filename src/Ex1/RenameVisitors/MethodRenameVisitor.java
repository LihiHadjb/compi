package Ex1.RenameVisitors;

import ast.*;

import java.util.Set;

public class MethodRenameVisitor implements Visitor {
    private Program prog;
    private Set<String> classesToCheck;
    private String oldName;
    private String newName;
    private MethodDecl lastMethodSeen;
    private boolean isLastOwnerInClassesToCheck;



    public MethodRenameVisitor(Program prog, Set<String> classesToCheck, String oldName, String newName){
        this.prog = prog;
        this.classesToCheck = classesToCheck;
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void visit(Program prog) {
        prog.mainClass().accept(this); // visit(prog.mainClass());
        if (prog.classDecls() != null){
            for (ClassDecl classDecl : prog.classDecls()){
                classDecl.accept(this); // visit(classDecl);
            }
        }
    }

    @Override
    public void visit(ClassDecl classDecl) {
        if (classesToCheck.contains(classDecl.name())){
            if (classDecl.methoddecls() != null){
                for (MethodDecl methodDecl : classDecl.methoddecls()){
                    if (methodDecl.name().equals(oldName)){
                        methodDecl.setName(newName);
                    }
                }
            }
        }

        if (classDecl.methoddecls() != null){
            for (MethodDecl methodDecl : classDecl.methoddecls()){
                methodDecl.accept(this); // visit(methodDecl);
            }
        }
    }

    @Override
    public void visit(MainClass mainClass) {
        mainClass.mainStatement().accept(this); // visit(mainClass.mainStatement());
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        this.lastMethodSeen = methodDecl;
        if (methodDecl.body() != null){
            for (Statement statement : methodDecl.body()){
                statement.accept(this); // visit(statement);
            }
        }

        methodDecl.ret().accept(this); // visit(methodDecl.ret());
    }

    @Override
    public void visit(FormalArg formalArg) {

    }

    @Override
    public void visit(VarDecl varDecl) {

    }

    @Override
    public void visit(BlockStatement blockStatement) {
        if (blockStatement.statements() != null){
            for (Statement statement : blockStatement.statements()){
                statement.accept(this); // visit(statement);
            }
        }
    }

    @Override
    public void visit(IfStatement ifStatement) {
        ifStatement.cond().accept(this);
        ifStatement.thencase().accept(this);
        ifStatement.elsecase().accept(this);
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
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
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
        if (isLastOwnerInClassesToCheck && e.methodId().equals(oldName)){
            e.setMethodId(newName);
        }

        if (e.actuals() != null){
            for (Expr actual : e.actuals()){
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
        //TODO
        String type = getVariableType(lastMethodSeen, e.id());
        isLastOwnerInClassesToCheck = classesToCheck.contains(type);
    }

    @Override
    public void visit(ThisExpr e) {
        //TODO
        String type = getContextClass(lastMethodSeen, e);
        isLastOwnerInClassesToCheck = classesToCheck.contains(type);
    }

    @Override
    public void visit(NewIntArrayExpr e) {
        e.lengthExpr().accept(this);
    }

    @Override
    public void visit(NewObjectExpr e) {
        String type = e.classId();
        isLastOwnerInClassesToCheck = classesToCheck.contains(type);

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


