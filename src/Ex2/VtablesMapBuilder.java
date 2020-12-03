package Ex2;

import Ex1.Inheritance.InheritanceNode;
import Ex1.SearchInContext;
import Ex1.SymbolTables.SymbolTable;

import java.util.HashMap;
import java.util.Map;

public class VtablesMapBuilder {
    SearchInContext searchInContext;
    public VtablesMapBuilder(SearchInContext searchInContext){
        this.searchInContext = searchInContext;
    }


    public HashMap<String, Vtable> build(){
        HashMap<String, Vtable> result = new HashMap();
        for(Map.Entry<String, InheritanceNode> entry: searchInContext.inheritanceTrees().roots().entrySet()){
            HashMap<String, Vtable> rootResult = new HashMap();
            createVtablesRec(entry.getKey(), entry.getValue(), rootResult, null);
            result.putAll(rootResult);
        }
        return result;
    }

    public void createVtablesRec(String className, InheritanceNode currInheritanceNode, HashMap<String, Vtable> result, Vtable parentVtable){
        Vtable currVtable = createVtableForClass(currInheritanceNode, parentVtable);
        result.put(className, currVtable);
        for(InheritanceNode child : currInheritanceNode.children()){
            createVtablesRec(child.name(), child, result, currVtable);
        }
    }

    public Vtable createVtableForClass(InheritanceNode inheritanceNode, Vtable parentVtable){
        Vtable result;
        if(parentVtable == null){
            result = new Vtable();

        }
        else{
            result = parentVtable.clone();
        }
        updateImplementingClasses(searchInContext.inheritanceNode2ClassSymbolTable(inheritanceNode), result, inheritanceNode.name());
        updateIndex(searchInContext.inheritanceNode2ClassSymbolTable(inheritanceNode), result, parentVtable);
        return result;
    }

    public void updateImplementingClasses(SymbolTable symbolTable, Vtable vtable, String className){
        for(String methodName : symbolTable.methods().keySet()){
            vtable.setImplementingClassName(methodName, className);
        }
    }

    public void updateIndex(SymbolTable symbolTable, Vtable vtable, Vtable parentVtable){
        for(String methodName : symbolTable.methods().keySet()){
            if(parentVtable.getImplementingClassName(methodName) == null){
                vtable.setLast_index(vtable.getLast_index() + 1);
                vtable.setIndex(methodName, vtable.getLast_index());
            }
        }
    }

}
