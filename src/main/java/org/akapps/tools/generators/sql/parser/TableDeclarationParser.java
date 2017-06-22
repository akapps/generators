package org.akapps.tools.generators.sql.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper parser for a single "CREATE TABLE" statement
 *
 * @author Antoine Kapps
 */
public class TableDeclarationParser {

    private final String statement;

    public TableDeclarationParser(String statement) {
        this.statement = statement;
    }

    /**
     * Reads the name of the table. If the schema is specified too, it is read as well :
     * <pre>
     *     CREATE TABLE BOOK (...)        ->  "BOOK"
     *     CREATE TABLE STORE.BOOK (...)  ->  "STORE.BOOK"
     * </pre>
     *
     * @return the table's name in the create statement
     */
    public String readTableName() {
        int end = statement.indexOf('(');
        String tableDeclaration = statement.substring(0, end).trim();

        int start = tableDeclaration.lastIndexOf(' ');
        return tableDeclaration.substring(start + 1);
    }

    /**
     * Split the column declarations. Column definitions and constraints are returned without distinction.
     *
     * @return all declarations inside the create statement
     */
    public String[] readColumnDeclarations() {
        String declarations = statement.substring(statement.indexOf('(') + 1, statement.lastIndexOf(')')).trim() + ',';

        List<String> result = new ArrayList<>();
        int current = 0;

        while (current < declarations.length()) {
            int start = current;

            int inner = 0;
            char currentChar = declarations.charAt(current);

            while ((currentChar != ',' || inner > 0) && current < declarations.length()) {

                if (currentChar == '(')
                    inner++;
                else if (currentChar == ')')
                    inner--;

                currentChar = declarations.charAt(++current);
            }
            final String substring = declarations.substring(start, current);
            result.add(substring.trim());
            current++;
        }

        return result.toArray(new String[result.size()]);
    }

}
