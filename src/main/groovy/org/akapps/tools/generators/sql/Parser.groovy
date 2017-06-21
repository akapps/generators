package org.akapps.tools.generators.sql

import org.akapps.tools.generators.sql.structure.TableBuilder

/**
 * @author Antoine Kapps
 */
class Parser {

    final File source
    final String charset

    Parser(File source, String charset) {
        this.source = source
        this.charset = charset
    }

    Parser(File source) {
        this(source, 'UTF-8')
    }

    private static class Schema {
        Map<String, TableBuilder> tables
    }

    private final Map<String, Schema> schemas = [:]

    private List<String> normalizeSource() {
        final List<String> result = []
        StringBuilder sb = new StringBuilder()

        List<String> lines = source.readLines(charset).collect {it.trim()}.findAll { !it.isEmpty() && !it.startsWith('--') }
        lines.each {line ->
            // Remove inlined comments
            if (line.contains('--'))    line = line.substring(0, line.indexOf('--')).trim()

            // Normalize words
            line = line.split().join(' ')
            sb.append(' ').append(line)

            if (line.endsWith(';')) {
                result << sb.toString().trim()
                sb = new StringBuilder()
            }
        }

        return result
    }


    void parse() {
        normalizeSource().each { String statement ->
            if (statement.startsWith("CREATE TABLE ")) {
                createTable(statement)
            }

            else if (statement.startsWith("COMMENT ON COLUMN ")) {
                commentColumn(statement)
            }

            else if (statement.startsWith("COMMENT ON TABLE ")) {
                commentTable(statement)
            }

            else if (statement.startsWith("CREATE INDEX ")) {
                createIndex(statement)
            }

            else if (statement.startsWith("ALTER TABLE ") && statement.contains("FOREIGN KEY")) {
                defineForeignKey(statement)
            }

            else
                println "Statement type not recognized :\n  >> $statement"
        }
    }

    private void createTable(String statement) {

    }

    private void commentColumn(String statement) {

    }

    private void commentTable(String statement) {

    }

    private void createIndex(String statement) {

    }

    private void defineForeignKey(String statement) {

    }

}
