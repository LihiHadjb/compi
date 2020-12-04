package Ex2;

import ast.MethodDecl;

import java.util.HashMap;

public class Vtable {
    HashMap<String, String> methodName2ImplementingClassName;
    HashMap<String, Integer> methodName2Index;
    HashMap<String, MethodDecl> methodName2MethodDecl;
    int last_index;

    public Vtable(){
        this.methodName2ImplementingClassName = new HashMap<>();
        this.methodName2Index = new HashMap<>();
        this.methodName2MethodDecl = new HashMap<>();
        last_index = -1;
    }

    public String getImplementingClassName(String methodName){
        return this.methodName2ImplementingClassName.get(methodName);
    }

    public void setMethodDecl(String methodName, MethodDecl methodDecl){
        this.methodName2MethodDecl.put(methodName, methodDecl);
    }

    public MethodDecl getMethodDecl(String methodName){
        return this.methodName2MethodDecl.get(methodName);
    }

    public void setImplementingClassName(String methodName, String implementingClassName){
        this.methodName2ImplementingClassName.put(methodName, implementingClassName);
    }

    public Integer getIndex(String methodName){
        return this.methodName2Index.get(methodName);
    }

    public void setIndex(String methodName, Integer index){
        this.methodName2Index.put(methodName, index);
    }

    public Vtable clone(){
        Vtable result = new Vtable();
        result.methodName2ImplementingClassName.putAll(this.methodName2ImplementingClassName);
        result.methodName2Index.putAll(this.methodName2Index);
        result.methodName2MethodDecl.putAll(this.methodName2MethodDecl);
        result.last_index = this.last_index;
        return result;
    }

    public HashMap<String, String> getMethodName2ImplementingClassName() {
        return methodName2ImplementingClassName;
    }

    public void setMethodName2ImplementingClassName(HashMap<String, String> methodName2ImplementingClassName) {
        this.methodName2ImplementingClassName = methodName2ImplementingClassName;
    }

    public HashMap<String, MethodDecl> getMethodName2MethodDecl() {
        return this.methodName2MethodDecl;
    }



    public int getLast_index() {
        return last_index;
    }

    public void setLast_index(int last_index) {
        this.last_index = last_index;
    }


}
