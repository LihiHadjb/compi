#!/bin/bash

mkdir -p temp_out
mkdir -p temp_expected
mkdir -p temp_actual

echo "Running renaming logic..."

java -jar mjavac.jar unmarshal rename method theMethod 25 renamedMethod  examples/ex1/test_cases/method_renaming/method_related_sibling_class.xml temp_out/method_related_sibling_class_renamed.xml
java -jar mjavac.jar unmarshal rename method theMethod 19 renamedMethod  examples/ex1/test_cases/method_renaming/method_unrelated_sibling_class.xml temp_out/method_unrelated_sibling_class_renamed.xml
java -jar mjavac.jar unmarshal rename method theMethod 13 renamedMethod  examples/ex1/test_cases/method_renaming/method_with_if_and_assign_array_statements.xml temp_out/method_with_if_and_assign_array_statements_renamed1.xml
java -jar mjavac.jar unmarshal rename method theMethod 19 renamedMethod  examples/ex1/test_cases/method_renaming/method_with_if_and_assign_array_statements.xml temp_out/method_with_if_and_assign_array_statements_renamed2.xml
java -jar mjavac.jar unmarshal rename method theMethod 19 renamedMethod  examples/ex1/test_cases/method_renaming/method_with_new_object.xml temp_out/method_with_new_object_renamed.xml
java -jar mjavac.jar unmarshal rename method theMethod 19 renamedMethod  examples/ex1/test_cases/method_renaming/method_with_ref_id.xml temp_out/method_with_ref_id_renamed1.xml
java -jar mjavac.jar unmarshal rename method theMethod 13 renamedMethod  examples/ex1/test_cases/method_renaming/method_with_ref_id.xml temp_out/method_with_ref_id_renamed2.xml
java -jar mjavac.jar unmarshal rename method theMethod 19 renamedMethod  examples/ex1/test_cases/method_renaming/method_with_sysout_statement.xml temp_out/method_with_sysout_statement_renamed.xml
java -jar mjavac.jar unmarshal rename method theMethod 23 renamedMethod  examples/ex1/test_cases/method_renaming/method_with_this.xml temp_out/method_with_this_renamed.xml
java -jar mjavac.jar unmarshal rename method theMethod 19 renamedMethod  examples/ex1/test_cases/method_renaming/method_with_while_and_assign_statements.xml temp_out/method_with_while_and_assign_statements_renamed.xml
java -jar mjavac.jar unmarshal rename method theThing 27 renamedThing examples/ex1/test_cases/method_renaming/method_and_variable_with_the_same_name.xml  temp_out/method_and_variable_with_the_same_name_renamed1.xml
java -jar mjavac.jar unmarshal rename var theVar 10 renamedVar examples/ex1/test_cases/variable_renaming/field_related_sibling_class.xml  temp_out/field_related_sibling_class_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 20 renamedVar examples/ex1/test_cases/variable_renaming/field_unrelated_sibling_class.xml  temp_out/field_unrelated_sibling_class_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 20 renamedVar examples/ex1/test_cases/variable_renaming/field_with_formal_param_shadowing.xml  temp_out/field_with_formal_param_shadowing_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 20 renamedVar examples/ex1/test_cases/variable_renaming/field_with_if_and_assign_array_statements.xml  temp_out/field_with_if_and_assign_array_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 20 renamedVar examples/ex1/test_cases/variable_renaming/field_with_local_var_shadowing.xml  temp_out/field_with_local_var_shadowing_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 20 renamedVar examples/ex1/test_cases/variable_renaming/field_with_sysout_and_array_length_statements.xml  temp_out/field_with_sysout_and_array_length_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 20 renamedVar examples/ex1/test_cases/variable_renaming/field_with_while_and_assign_statements.xml  temp_out/field_with_while_and_assign_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 26 renamedVar examples/ex1/test_cases/variable_renaming/formal_param_with_if_and_assign_array_statements.xml  temp_out/formal_param_with_if_and_assign_array_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 26 renamedVar examples/ex1/test_cases/variable_renaming/formal_param_with_sysout_and_array_length_statements.xml  temp_out/formal_param_with_sysout_and_array_length_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 26 renamedVar examples/ex1/test_cases/variable_renaming/formal_param_with_while_and_assign_statements.xml  temp_out/formal_param_with_while_and_assign_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 30 renamedVar examples/ex1/test_cases/variable_renaming/local_var_with_if_and_assign_array_statements.xml  temp_out/local_var_with_if_and_assign_array_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 29 renamedVar examples/ex1/test_cases/variable_renaming/local_var_with_sysout_and_array_length_statements.xml  temp_out/local_var_with_sysout_and_array_length_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theVar 28 renamedVar examples/ex1/test_cases/variable_renaming/local_var_with_while_and_assign_statements.xml  temp_out/local_var_with_while_and_assign_statements_renamed.xml
java -jar mjavac.jar unmarshal rename var theThing 10 renamedThing examples/ex1/test_cases/variable_renaming/variable_and_method_with_the_same_name.xml  temp_out/variable_and_method_with_the_same_name_renamed1.xml
java -jar mjavac.jar unmarshal rename var theThing 33 renamedThing examples/ex1/test_cases/variable_renaming/variable_and_method_with_the_same_name.xml  temp_out/variable_and_method_with_the_same_name_renamed2.xml
java -jar mjavac.jar unmarshal rename var theThing 38 renamedThing examples/ex1/test_cases/variable_renaming/variable_and_method_with_the_same_name.xml  temp_out/variable_and_method_with_the_same_name_renamed3.xml


printf "\n\nChecking method renaming results against expected:\n"
echo "---------------------------------"
for path in $(ls examples/ex1/test_cases/method_renaming/*_renamed*.xml); do
	echo "Checking $(basename $path)..."
	cat $path | sed -E -e 's/[[:blank:]]+//g' > temp_expected/$(basename $path)
	cat temp_out/$(basename $path) | sed -E -e 's/[[:blank:]]+//g' > temp_actual/$(basename $path)
	diff temp_expected/$(basename $path) temp_actual/$(basename $path)
	echo "================================="
done

printf "\n\nChecking variable renaming results against expected:\n"
echo "---------------------------------"
for path in $(ls examples/ex1/test_cases/variable_renaming/*_renamed*.xml); do
	echo "Checking $(basename $path)..."
	cat $path | sed -E -e 's/[[:blank:]]+//g' > temp_expected/$(basename $path)
	cat temp_out/$(basename $path) | sed -E -e 's/[[:blank:]]+//g' > temp_actual/$(basename $path)
	diff temp_expected/$(basename $path) temp_actual/$(basename $path)
	echo "================================="
done

printf "\n\nFinishing up...  "
rm -rf temp_out
rm -rf temp_expected
rm -rf temp_actual
echo "Done!"

