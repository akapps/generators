package org.akapps.tools.generators.sql.parser;

/**
 * Helper parser for a single column statement
 *
 * @author Antoine Kapps
 */
public class ColumnDeclarationParser {

    private final String statement;

    public ColumnDeclarationParser(String statement) {
        this.statement = statement;
    }

    public String readColumnName() {
        int end = statement.indexOf(' ');
        return statement.substring(0, end);
    }

    public String readColumnType() {
        int start = beginningOfSecondWord();
        int end = gotoNextEnglobeParenthesis(start, ' ');

        while (end < statement.length()-1 && (statement.charAt(end+1) == '(' || statement.charAt(end+1) == ' ')) {
            end = gotoNextEnglobeParenthesis(end, ' ');
        }

        return statement.substring(start, end).replaceAll("\\ ", "");
    }

    public boolean isDeclaredNonNullable() {
        return (statement + " ").toLowerCase().contains(" not null ");
    }

    public String readDefaultValue() {
        int wd = statement.toLowerCase().indexOf(" with default");
        if (wd == -1)
            return null;

        int start = wd + 14;
        if (start >= statement.length())
            return "";
        else {
            int end = gotoNextEnglobeParenthesis(start, ' ');
            return statement.substring(start, end);
        }
    }

    private int beginningOfSecondWord() {
        int i = statement.indexOf(' ') + 1;
        while (i < statement.length() && statement.charAt(i) == ' ') {
            i++;
        }
        return i;
    }

    private int gotoNextEnglobeParenthesis(int current, char target) {
        final int length = statement.length();
        int i = current + 1;

        while(i < length) {
            final char currentChar = statement.charAt(i);
            if (currentChar == target)
                return i;

            if (currentChar == '(')
                i = gotoNextEnglobeParenthesis(i, ')');
            else
                i++;
        }

        return i;
    }
}
