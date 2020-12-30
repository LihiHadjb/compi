package Ex3;

import Ex1.Inheritance.InheritanceTrees;
import Ex1.SearchInContext;
import Ex1.SymbolTables.MethodEntry;
import Ex1.SymbolTables.SymbolTable;
import Ex1.SymbolTables.VarEntry;
import ast.*;
import org.w3c.dom.css.ViewCSS;

import java.util.HashSet;
import java.util.Set;

public class SemanticCheckerVisitor implements Visitor {
    SearchInContext searchInContext;
    boolean isErrorFound;
    Set<String> classesSeen;
    ClassDecl lastClassSeen;
    MethodDecl lastMethodSeen;
    String mainClassName;
    AstType actualType;

    public boolean isErrorFound() {
        return isErrorFound;
    }

    public SemanticCheckerVisitor(SearchInContext searchInContext){
        this.searchInContext = searchInContext;
        this.isErrorFound = false;
        this.classesSeen = new HashSet<>();
    }


    @Override
    public void visit(Program program) {
        if (program.mainClass() != null){
            program.mainClass().accept(this);
        }
        if(program.classDecls() != null){
            for(ClassDecl classDecl : program.classDecls()){
                classDecl.accept(this);
            }
        }

    }

    @Override
    public void visit(ClassDecl classDecl) {
        this.lastClassSeen = classDecl;
        //check duplicates
        if(this.classesSeen.contains(classDecl.name())){
            this.isErrorFound = true;
            return;
        }

        //check that super class was defined before the current class
        if (classDecl.superName() != null){
            if(!this.classesSeen.contains(classDecl.superName())){
                this.isErrorFound = true;
                return;
            }
            if(this.mainClassName != null && classDecl.superName().equals(this.mainClassName)) {
                this.isErrorFound = true;
                return;
            }
        }

        classesSeen.add(classDecl.name());

        if(classDecl.fields() != null){
            Set<String> currClassFields = new HashSet<>();
            for(VarDecl field : classDecl.fields()){
                if(currClassFields.contains(field.name())){
                    this.isErrorFound = true;
                    return;
                }
                if(searchInContext.isOverridingField(field.name(), lastClassSeen)){
                    this.isErrorFound = true;
                    return;
                }
                if(!searchInContext.typeExists(field.type())){
                    isErrorFound = true;
                    return;
                }
                currClassFields.add(field.name());
                field.accept(this);
            }
        }

        if(classDecl.methoddecls() != null){
            Set<String> currClassMethods = new HashSet<>();
            for(MethodDecl methodDecl : classDecl.methoddecls()){
                if(currClassMethods.contains(methodDecl.name())){
                    this.isErrorFound = true;
                    return;
                }
                currClassMethods.add(methodDecl.name());
                methodDecl.accept(this);
            }
        }

    }

    @Override
    public void visit(MainClass mainClass) {
        this.mainClassName = mainClass.name();
        this.classesSeen.add(mainClass.name());
        mainClass.mainStatement().accept(this);

    }

    @Override
    public void visit(MethodDecl methodDecl) {
        this.lastMethodSeen = methodDecl;
        //check that overriding is legal
        if (!(searchInContext.verifyOverridingMethod(methodDecl, this.lastClassSeen))){
            this.isErrorFound = true;
        }

        //check the return expression is of correct type
        if(methodDecl.ret() == null){
            this.isErrorFound = true;
        }
        methodDecl.ret().accept(this);
        if(!searchInContext.isSubType(this.actualType, methodDecl.returnType())){
            this.isErrorFound = true;
        }

        //check duplicates in formals and varDecls
        Set<String> currMethodFormals = new HashSet<>();
        Set<String> currMethodVarDecls = new HashSet<>();

        if(methodDecl.formals() != null){
            for(FormalArg formalArg : methodDecl.formals()){
                if(currMethodFormals.contains(formalArg.name())){
                    this.isErrorFound = true;
                }
                if(!searchInContext.typeExists(formalArg.type())){
                    isErrorFound = true;
                    return;
                }
                currMethodFormals.add(formalArg.name());
            }
        }

        if(methodDecl.vardecls() != null){
            for(VarDecl varDecl : methodDecl.vardecls()){
                if(currMethodVarDecls.contains(varDecl.name())){
                    this.isErrorFound = true;
                }
                if(!searchInContext.typeExists(varDecl.type())){
                    isErrorFound = true;
                    return;
                }
                currMethodVarDecls.add(varDecl.name());
            }
        }

        if(methodDecl.vardecls() != null){
            for(VarDecl varDecl : methodDecl.vardecls()){
                if(currMethodFormals.contains(varDecl.name())){
                    this.isErrorFound = true;
                }
            }
        }

        if (methodDecl.body() != null){
            for (Statement statement : methodDecl.body()){
                statement.accept(this);
            }
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
        if (blockStatement.statements() != null){
            for (Statement statement : blockStatement.statements()){
                statement.accept(this); // visit(statement);
            }
        }
    }

    @Override
    public void visit(IfStatement ifStatement) {
        //check that condition is boolean
        ifStatement.cond().accept(this);
        if(!(actualType instanceof BoolAstType)){
            isErrorFound = true;
        }

        if (ifStatement.thencase() != null){
            ifStatement.thencase().accept(this);
        }

        if (ifStatement.elsecase() != null){
            ifStatement.elsecase().accept(this);
        }
    }

    @Override
    public void visit(WhileStatement whileStatement) {
        whileStatement.cond().accept(this);
        if(!(actualType instanceof BoolAstType)){
            isErrorFound = true;
        }

        if(whileStatement.body() != null){
            whileStatement.body().accept(this);
        }
    }

    @Override
    public void visit(SysoutStatement sysoutStatement) {
        sysoutStatement.arg().accept(this);

        if(!(actualType instanceof IntAstType)){
            isErrorFound = true;
        }
    }

    @Override
    public void visit(AssignStatement assignStatement) {
        assignStatement.rv().accept(this);

        AstType expectedAstType = searchInContext.lookupVarAstType(lastMethodSeen, assignStatement.lv());
        if(!(searchInContext.isSubType(actualType, expectedAstType))){
            isErrorFound = true;
        }
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
        AstType actualAstType = searchInContext.lookupVarAstType(lastMethodSeen, assignArrayStatement.lv());
        if(!searchInContext.isSubType(actualAstType, new IntArrayAstType())){
            isErrorFound = true;
        }

        assignArrayStatement.rv().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        assignArrayStatement.index().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }
    }

    @Override
    public void visit(AndExpr e) {
        e.e1().accept(this);
        if(!searchInContext.isSubType(actualType, new BoolAstType())){
            isErrorFound = true;
        }

        e.e2().accept(this);
        if(!searchInContext.isSubType(actualType, new BoolAstType())){
            isErrorFound = true;
        }

        actualType = new BoolAstType();
    }

    @Override
    public void visit(LtExpr e) {
        e.e1().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        e.e2().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        actualType = new BoolAstType();
    }

    @Override
    public void visit(AddExpr e) {
        e.e1().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        e.e2().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        actualType = new IntAstType();
    }

    @Override
    public void visit(SubtractExpr e) {
        e.e1().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        e.e2().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        actualType = new IntAstType();
    }

    @Override
    public void visit(MultExpr e) {
        e.e1().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        e.e2().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        actualType = new IntAstType();
    }

    @Override
    public void visit(ArrayAccessExpr e) {
        e.arrayExpr().accept(this);
        if(!searchInContext.isSubType(actualType, new IntArrayAstType())){
            isErrorFound = true;
        }

        e.indexExpr().accept(this);
        if(!searchInContext.isSubType(actualType, new IntAstType())){
            isErrorFound = true;
        }

        actualType = new IntAstType();
    }

    @Override
    public void visit(ArrayLengthExpr e) {
        e.arrayExpr().accept(this);
        if(!searchInContext.isSubType(actualType, new IntArrayAstType())){
            isErrorFound = true;
        }
        actualType = new IntAstType();

    }

    @Override
    public void visit(MethodCallExpr e) {
        // Check that ownerExpr is one of the following: this, new Expr, ref to a local var, formal or field
        if (!(e.ownerExpr() instanceof ThisExpr || e.ownerExpr() instanceof NewObjectExpr ||
                e.ownerExpr() instanceof IdentifierExpr)){
            isErrorFound = true;
            return;
        }

        e.ownerExpr().accept(this);
        if(!(actualType instanceof RefType)){
            isErrorFound = true;
            return;
        }

        SymbolTable classSymbolTable = searchInContext.className2ClassSymbolTable(((RefType) actualType).id());
        if(!searchInContext.hasMethodOrInheritedWithName(e.methodId(), classSymbolTable)){
            isErrorFound = true;
        }

        MethodEntry methodEntry = searchInContext.getMethodEntryOfClosestAncestorThatHasMethod(e.methodId(), classSymbolTable);
        if (methodEntry == null){
            isErrorFound = true;
            return;
        }
        MethodDecl methodDecl = methodEntry.getMethodDecl();
        if(e.actuals().size() != methodEntry.getMethodDecl().formals().size()){
            isErrorFound = true;
            return;
        }

        if(e.actuals() != null){
            int i =0;
            for(Expr actual : e.actuals()){
                actual.accept(this);
                FormalArg formalArg = methodDecl.formals().get(i);
                if(!searchInContext.isSubType(actualType, formalArg.type())){
                    isErrorFound = true;
                }
                i++;
            }
        }

        actualType = methodDecl.returnType();
    }

    @Override
    public void visit(IntegerLiteralExpr e) {
        actualType = new IntAstType();
    }

    @Override
    public void visit(TrueExpr e) {
        actualType = new BoolAstType();
    }

    @Override
    public void visit(FalseExpr e) {
        actualType = new BoolAstType();
    }

    @Override
    public void visit(IdentifierExpr e) {
      AstType astType = searchInContext.lookupVarAstType(lastMethodSeen, e.id());
      if(astType == null){
          isErrorFound = true;
      }
      actualType = astType;
    }

    @Override
    public void visit(ThisExpr e) {
        actualType = new RefType(lastClassSeen.name());
    }

    @Override
    public void visit(NewIntArrayExpr e) {
        e.lengthExpr().accept(this);
        if(!(actualType instanceof IntAstType)){
            isErrorFound = true;
        }

        actualType = new IntArrayAstType();

    }

    @Override
    public void visit(NewObjectExpr e) {
        if(!searchInContext.inheritanceTrees().getFlatClasses().keySet().contains(e.classId())){
            isErrorFound = true;
            return;
        }
        actualType = new RefType(e.classId());
    }

    @Override
    public void visit(NotExpr e) {
        e.e().accept(this);
        if(!(actualType instanceof BoolAstType)){
            isErrorFound = true;
        }

        actualType = new BoolAstType();
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
