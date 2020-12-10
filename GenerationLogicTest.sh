#!/bin/bash

echo "Running generation logic test..."

echo "1"
/home/pc/IdeaProjects/compi/examples/1_vars/Simple.java.xml
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/1_vars/Simple.java.xml /home/pc/IdeaProjects/compi/examples/1_vars/Simple_out.ll
lli /home/pc/IdeaProjects/compi/examples/1_vars/Simple.ll > /home/pc/IdeaProjects/compi/examples/1_vars/Simple_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/1_vars/Simple_out.ll > /home/pc/IdeaProjects/compi/examples/1_vars/Simple_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/1_vars/Simple_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/1_vars/Simple_actual_outputs.txt
echo "2"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/2_vars_type/VarType.java.xml /home/pc/IdeaProjects/compi/examples/2_vars_type/VarType_out.ll
lli /home/pc/IdeaProjects/compi/examples/2_vars_type/VarType.ll > /home/pc/IdeaProjects/compi/examples/2_vars_type/VarType_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/2_vars_type/VarType_out.ll > /home/pc/IdeaProjects/compi/examples/2_vars_type/VarType_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/2_vars_type/VarType_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/2_vars_type/VarType_actual_outputs.txt
echo "3"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/3_simple_expr/SimpleExpr.java.xml /home/pc/IdeaProjects/compi/examples/3_simple_expr/SimpleExpr_out.ll
lli /home/pc/IdeaProjects/compi/examples/3_simple_expr/SimpleExpr.ll > /home/pc/IdeaProjects/compi/examples/3_simple_expr/SimpleExpr_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/3_simple_expr/SimpleExpr_out.ll > /home/pc/IdeaProjects/compi/examples/3_simple_expr/SimpleExpr_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/3_simple_expr/SimpleExpr_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/3_simple_expr/SimpleExpr_actual_outputs.txt
echo "4"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/4_compound_expr/CompoundExpr.java.xml /home/pc/IdeaProjects/compi/examples/4_compound_expr/CompoundExpr_out.ll
lli /home/pc/IdeaProjects/compi/examples/4_compound_expr/CompoundExpr.ll > /home/pc/IdeaProjects/compi/examples/4_compound_expr/CompoundExpr_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/4_compound_expr/CompoundExpr_out.ll > /home/pc/IdeaProjects/compi/examples/4_compound_expr/CompoundExprr_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/4_compound_expr/CompoundExpr_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/4_compound_expr/CompoundExpr_actual_outputs.txt
echo "5"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/5_if/If.java.xml /home/pc/IdeaProjects/compi/examples/5_if/If_out.ll
lli /home/pc/IdeaProjects/compi/examples/5_if/If.ll > /home/pc/IdeaProjects/compi/examples/5_if/If_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/5_if/If_out.ll > /home/pc/IdeaProjects/compi/examples/5_if/If_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/5_if/If_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/5_if/If_actual_outputs.txt
echo "6_and"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/6_and/And.java.xml /home/pc/IdeaProjects/compi/examples/6_and/And_out.ll
lli /home/pc/IdeaProjects/compi/examples/6_and/And.ll > /home/pc/IdeaProjects/compi/examples/6_and/And_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/6_and/And_out.ll > /home/pc/IdeaProjects/compi/examples/6_and/And_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/6_and/And_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/6_and/And_actual_outputs.txt
echo "6_lowering_oo/Arrays"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Arrays.java.xml /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Arrays_out.ll
lli /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Arrays.ll > /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Arrays_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Arrays_out.ll > /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Arrays_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Arrays_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Arrays_actual_outputs.txt
echo "6_lowering_oo/Classes"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Classes.java.xml /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Classes_out.ll
lli /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Classes.ll > /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Classes_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Classes_out.ll > /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Classes_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Classes_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/6_lowering_oo/Classes_actual_outputs.txt
echo "7_arrays/Arrays"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/7_arrays/Arrays.java.xml /home/pc/IdeaProjects/compi/examples/7_arrays/Arrays_out.ll
lli /home/pc/IdeaProjects/compi/examples/7_arrays/Arrays.ll > /home/pc/IdeaProjects/compi/examples/7_arrays/Arrays_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/7_arrays/Arrays_out.ll > /home/pc/IdeaProjects/compi/examples/7_arrays/Arrays_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/7_arrays/Arrays_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/7_arrays/Arrays_actual_outputs.txt
echo "BinarySearch"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/ex2/BinarySearch.java.xml /home/pc/IdeaProjects/compi/examples/ex2/BinarySearch_out.ll
lli /home/pc/IdeaProjects/compi/examples/ex2/BinarySearch.ll > /home/pc/IdeaProjects/compi/examples/ex2/BinarySearch_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/ex2/BinarySearch_out.ll > /home/pc/IdeaProjects/compi/examples/ex2/BinarySearch_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/ex2/BinarySearch_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/ex2/BinarySearch_actual_outputs.txt
echo "BinaryTree"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/ex2/BinaryTree.java.xml /home/pc/IdeaProjects/compi/examples/ex2/BinaryTree_out.ll
lli /home/pc/IdeaProjects/compi/examples/ex2/BinaryTree.ll > /home/pc/IdeaProjects/compi/examples/ex2/BinaryTree_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/ex2/BinaryTree_out.ll > /home/pc/IdeaProjects/compi/examples/ex2/BinaryTree_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/ex2/BinaryTree_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/ex2/BinaryTree_actual_outputs.txt
echo "BubbleSort"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/ex2/BubbleSort.java.xml /home/pc/IdeaProjects/compi/examples/ex2/BubbleSort_out.ll
lli /home/pc/IdeaProjects/compi/examples/ex2/BubbleSort.ll > /home/pc/IdeaProjects/compi/examples/ex2/BubbleSort_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/ex2/BubbleSort_out.ll > /home/pc/IdeaProjects/compi/examples/ex2/BubbleSort_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/ex2/BubbleSort_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/ex2/BubbleSort_actual_outputs.txt
echo "Factorial"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/ex2/Factorial.java.xml /home/pc/IdeaProjects/compi/examples/ex2/Factorial_out.ll
lli /home/pc/IdeaProjects/compi/examples/ex2/Factorial.ll > /home/pc/IdeaProjects/compi/examples/ex2/Factorial_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/ex2/Factorial_out.ll > /home/pc/IdeaProjects/compi/examples/ex2/Factorial_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/ex2/Factorial_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/ex2/Factorial_actual_outputs.txt
echo "LinearSearch"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/ex2/LinearSearch.java.xml /home/pc/IdeaProjects/compi/examples/ex2/LinearSearch_out.ll
lli /home/pc/IdeaProjects/compi/examples/ex2/LinearSearch.ll > /home/pc/IdeaProjects/compi/examples/ex2/LinearSearch_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/ex2/LinearSearch_out.ll > /home/pc/IdeaProjects/compi/examples/ex2/LinearSearch_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/ex2/LinearSearch_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/ex2/LinearSearch_actual_outputs.txt
echo "LinkedList"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/ex2/LinkedList.java.xml /home/pc/IdeaProjects/compi/examples/ex2/LinkedList_out.ll
lli /home/pc/IdeaProjects/compi/examples/ex2/LinkedList.ll > /home/pc/IdeaProjects/compi/examples/ex2/LinkedList_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/ex2/LinkedList_out.ll > /home/pc/IdeaProjects/compi/examples/ex2/LinkedList_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/ex2/LinkedList_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/ex2/LinkedList_actual_outputs.txt
echo "QuickSort"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/ex2/QuickSort.java.xml /home/pc/IdeaProjects/compi/examples/ex2/QuickSort_out.ll
lli /home/pc/IdeaProjects/compi/examples/ex2/QuickSort.ll > /home/pc/IdeaProjects/compi/examples/ex2/QuickSort_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/ex2/QuickSort_out.ll > /home/pc/IdeaProjects/compi/examples/ex2/QuickSort_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/ex2/QuickSort_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/ex2/QuickSort_actual_outputs.txt
echo "TreeVisitor"
java -jar mjavac.jar unmarshal compile /home/pc/IdeaProjects/compi/examples/ex2/TreeVisitor.java.xml /home/pc/IdeaProjects/compi/examples/ex2/TreeVisitor_out.ll
lli /home/pc/IdeaProjects/compi/examples/ex2/TreeVisitor.ll > /home/pc/IdeaProjects/compi/examples/ex2/TreeVisitor_expected_outputs.txt
lli /home/pc/IdeaProjects/compi/examples/ex2/TreeVisitor_out.ll > /home/pc/IdeaProjects/compi/examples/ex2/TreeVisitor_actual_outputs.txt
diff /home/pc/IdeaProjects/compi/examples/ex2/TreeVisitor_expected_outputs.txt /home/pc/IdeaProjects/compi/examples/ex2/TreeVisitor_actual_outputs.txt
echo "Done!"