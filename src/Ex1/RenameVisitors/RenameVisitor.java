package Ex1.RenameVisitors;

import Ex1.SearchInContext;
import ast.Visitor;

public abstract class RenameVisitor implements Visitor {
    String oldName;
    String newName;
    SearchInContext searchInContext;

    public RenameVisitor(String oldName, String newName, SearchInContext searchInContext){
        this.oldName = oldName;
        this.newName = newName;
        this.searchInContext = searchInContext;
    }
}
