package Ex2;

import java.util.HashMap;

public class FieldOffsets {

    HashMap<String, Integer> fieldName2Index;
    int last_index;

    int PTR_SIZE = 8;
    int INT_SIZE= 4;
    int BOOLEAN_SIZE = 1;


    public FieldOffsets(){
        this.fieldName2Index = new HashMap<>();
        this.last_index = 8;
    }

    public HashMap<String, Integer> getFieldName2Index() {
        return fieldName2Index;
    }

    public void setFieldName2Index(HashMap<String, Integer> fieldName2Index) {
        this.fieldName2Index = fieldName2Index;
    }

    public int getLast_index() {
        return last_index;
    }

    public void setLast_index(int last_index) {
        this.last_index = last_index;
    }

    public FieldOffsets clone(){
        FieldOffsets result = new FieldOffsets();
        result.fieldName2Index.putAll(this.fieldName2Index);
        result.last_index = this.last_index;
        return result;
    }

    public Integer getIndex(String fieldName){
        return this.fieldName2Index.get(fieldName);
    }

    public void setIndex(String fieldName, Integer index){
        this.fieldName2Index.put(fieldName, index);
    }


}
