package Ex2;

import Ex1.Inheritance.InheritanceNode;
import Ex1.SearchInContext;
import Ex1.SymbolTables.SymbolTable;

import java.util.HashMap;
import java.util.Map;

public class FieldOffsetsBuilder {
    SearchInContext searchInContext;
    public FieldOffsetsBuilder(SearchInContext searchInContext){
        this.searchInContext = searchInContext;
    }


    public HashMap<String, FieldOffsets> build(){
        HashMap<String, FieldOffsets> result = new HashMap();
        for(Map.Entry<String, InheritanceNode> entry: searchInContext.inheritanceTrees().roots().entrySet()){
            HashMap<String, FieldOffsets> rootResult = new HashMap();
            createFieldOffsetsRec(entry.getKey(), entry.getValue(), rootResult, null);
            result.putAll(rootResult);
        }
        return result;
    }

    public void createFieldOffsetsRec(String className, InheritanceNode currInheritanceNode, HashMap<String, FieldOffsets> result, FieldOffsets parentFieldOffsets){
        FieldOffsets currFieldOffsets = createFieldOffsetsForClass(currInheritanceNode, parentFieldOffsets);
        result.put(className, currFieldOffsets);
        for(InheritanceNode child : currInheritanceNode.children()){
            createFieldOffsetsRec(child.name(), child, result, currFieldOffsets);
        }
    }

    public FieldOffsets createFieldOffsetsForClass(InheritanceNode inheritanceNode, FieldOffsets parentFieldOffsets){
        FieldOffsets result;
        if(parentFieldOffsets == null){
            result = new FieldOffsets();
        }

        else{
            result = parentFieldOffsets.clone();
        }
        updateIndex(searchInContext.inheritanceNode2ClassSymbolTable(inheritanceNode), result);
        return result;
    }

    public void updateIndex(SymbolTable symbolTable, FieldOffsets fieldOffsets){
        for(String fieldName : symbolTable.variables().keySet()){
            fieldOffsets.setLast_index(fieldOffsets.getLast_index() + 1);
            fieldOffsets.setIndex(fieldName, fieldOffsets.getLast_index());
        }
    }
}
