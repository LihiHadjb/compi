package Ex2;

import java.util.HashMap;

public class Vtable {
    HashMap<String, String> methodName2ImplementingClassName;

    public Vtable(){
        this.methodName2ImplementingClassName = new HashMap<>();
    }

    public String getImplementingClassName(String methodName){
        return this.methodName2ImplementingClassName.get(methodName);
    }

    public void setImplementingClassName(String methodName, String implementingClassName){
        this.methodName2ImplementingClassName.put(methodName, implementingClassName);
    }

    public Vtable clone(){
        Vtable result = new Vtable();
        HashMap<String, String> map = new HashMap();
        result.methodName2ImplementingClassName.putAll(this.methodName2ImplementingClassName);
        return result;
    }


}
