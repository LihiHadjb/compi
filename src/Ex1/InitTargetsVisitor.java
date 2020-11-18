package Ex1;

import ast.*;

public class InitTargetsVisitor implements Visitor {
    private String oldName;
    private Integer lineNumber;
    private MethodDecl lastMethodSeen;
    private ClassDecl lastClassSeen;
    private AstNode targetAstNode;

    public InitTargetsVisitor(String oldName, String lineNumber){
        this.oldName = oldName;
        this.lineNumber = Integer.parseInt(lineNumber);

    }

    public MethodDecl lastMethodSeen(){
        return this.lastMethodSeen;
    }

    public ClassDecl lastClasSeen(){
        return this.lastClasSeen();
    }

    public AstNode targetAstNode(){
        return this.targetAstNode;
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
        this.lastClassSeen = classDecl;
        if (classDecl.fields() != null){
            for(VarDecl field : classDecl.fields()){
                field.accept(this);
            }
        }

        if(classDecl.methoddecls() != null){
            for(MethodDecl methodDecl : classDecl.methoddecls()){
                methodDecl.accept(this);
            }
        }
    }

    @Override
    public void visit(MainClass mainClass) {
        mainClass.mainStatement().accept(this);
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        this.lastMethodSeen = methodDecl;
        if(methodDecl.lineNumber == this.lineNumber){
            this.targetAstNode = methodDecl;
        }

        else {
            if (methodDecl.vardecls() != null){
                for (VarDecl varDecl : methodDecl.vardecls()){
                    varDecl.accept(this);
                }
            }

            if (methodDecl.formals() != null){
                for(FormalArg formalArg : methodDecl.formals()){
                    formalArg.accept(this);
                }
            }
        }
    }

    @Override
    public void visit(FormalArg formalArg) {
        if(formalArg.lineNumber == this.lineNumber){
            this.targetAstNode = formalArg;
        }
    }

    @Override
    public void visit(VarDecl varDecl) {
        if(varDecl.lineNumber == this.lineNumber){
            this.targetAstNode = varDecl;
        }
    }

    @Override
    public void visit(BlockStatement blockStatement) {
        //do nothing

    }

    @Override
    public void visit(IfStatement ifStatement) {
        //do nothing
    }

    @Override
    public void visit(WhileStatement whileStatement) {
        //do nothing
    }

    @Override
    public void visit(SysoutStatement sysoutStatement) {
        //do nothing
    }

    @Override
    public void visit(AssignStatement assignStatement) {
        //do nothing
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
        //do nothing
    }

    @Override
    public void visit(AndExpr e) {
        //do nothing
    }

    @Override
    public void visit(LtExpr e) {
        //do nothing
    }

    @Override
    public void visit(AddExpr e) {
        //do nothing
    }

    @Override
    public void visit(SubtractExpr e) {
        //do nothing
    }

    @Override
    public void visit(MultExpr e) {
        //do nothing
    }

    @Override
    public void visit(ArrayAccessExpr e) {
        //do nothing
    }

    @Override
    public void visit(ArrayLengthExpr e) {
        //do nothing
    }

    @Override
    public void visit(MethodCallExpr e) {
        //do nothing
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
        //do nothing
    }

    @Override
    public void visit(ThisExpr e) {
        //do nothing
    }

    @Override
    public void visit(NewIntArrayExpr e) {
        //do nothing
    }

    @Override
    public void visit(NewObjectExpr e) {
        //do nothing
    }

    @Override
    public void visit(NotExpr e) {
        //do nothing
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
