package Ex2;

import Ex1.Inheritance.InheritanceNode;
import Ex1.SearchInContext;
import Ex1.SymbolTables.SymbolTable;
import ast.AstType;
import ast.BoolAstType;
import ast.IntArrayAstType;
import ast.IntAstType;

import java.util.HashMap;
import java.util.Map;

public class FieldOffsetsMapBuilder {
    SearchInContext searchInContext;
    public FieldOffsetsMapBuilder(SearchInContext searchInContext){
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
        int size;
        for(String fieldName : symbolTable.variables().keySet()){
            AstType astType = symbolTable.getVariableAstTypeOfName(fieldName);
            if(astType instanceof IntAstType){
                size = fieldOffsets.INT_SIZE;
            }
            else if(astType instanceof BoolAstType){
                size = fieldOffsets.BOOLEAN_SIZE;
            }
            else if(astType instanceof IntArrayAstType){
                size = fieldOffsets.PTR_SIZE;
            }
            else{
                size = fieldOffsets.PTR_SIZE;
            }

            fieldOffsets.setIndex(fieldName, fieldOffsets.getLast_index());
            fieldOffsets.setLast_index(fieldOffsets.getLast_index() + size);
        }
    }
}
