package org.akapps.tools.generators.sql

import groovy.transform.TupleConstructor
import org.akapps.tools.generators.sql.parser.TableDeclarationParser
import org.akapps.tools.generators.sql.structure.Table
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
        final Map<String, TableBuilder> tables = [:]
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


    Map<String, Table[]> parse() {
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

        return schemas.collectEntries {String schemaName, Schema content ->
            def tables = content.tables.values().collect {it.build()}
            return [schemaName, tables.toArray(new Table[tables.size()])]
        }
    }

    private void createTable(String statement) {
        def tp = new TableDeclarationParser(statement)

        def tableName = TableName.parse(tp.readTableName())
        Schema schema = schemas.get(tableName.schema)
        if (!schema) {
            schema = new Schema()
            schemas.put(tableName.schema, schema)
        }

        def builder = new TableBuilder(tableName.tableName)
        schema.tables.put(tableName.tableName, builder)

        tp.readColumnDeclarations().each {dec ->
            def words = dec.split()
            if (dec.startsWith("CONSTRAINT ")) {
                // FIXME Not necessarily a PK, PK decomposition NOK
                builder.primaryKey(words[1], words[4])
            }
            else {
                def nullable = dec.toLowerCase().contains("not null")
                // TODO Manque le reste...
                builder.addColumn(words[0], words[1], nullable)
            }
        }
    }

    @TupleConstructor
    private static class ColumnName {
        String schema, tableName, colName

        static ColumnName parse(String s) {
            def split = s.tokenize('.')
            if (split.size() == 1)  return new ColumnName(null, null, split[0])
            if (split.size() == 2)  return new ColumnName(null, split[0], split[1])
            if (split.size() == 3)  return new ColumnName(split[0], split[1], split[2])
            throw new IllegalArgumentException("ColumnName not recognized : $s")
        }
    }

    @TupleConstructor
    private static class TableName {
        String schema, tableName

        static TableName parse(String s) {
            def split = s.tokenize('.')
            if (split.size() == 1)  return new TableName(null, split[0])
            if (split.size() == 2)  return new TableName(split[0], split[1])
            throw new IllegalArgumentException("TableName not recognized : $s splitted into $split")
        }
    }

    private void commentColumn(String statement) {
        def cn = ColumnName.parse(statement.split()[3])
        def sub = statement.substring(statement.indexOf(' IS ') + 4)
        int d = sub.indexOf("'") + 1
        int f = sub.lastIndexOf("'")

        schemas.get(cn.schema).tables.get(cn.tableName).commentOnColumn(cn.colName, sub.substring(d, f))
    }

    private void commentTable(String statement) {
        def tn = TableName.parse(statement.split()[3])
        def sub = statement.substring(statement.indexOf(' IS ') + 4)
        int d = sub.indexOf("'") + 1
        int f = sub.lastIndexOf("'")

        schemas.get(tn.schema).tables.get(tn.tableName).commentOnTable(sub.substring(d, f))
    }

    private void createIndex(String statement) {
        def split = statement.split()
        def name = split[2]
        def tn = TableName.parse(split[4])

        int d = statement.indexOf('(') + 1
        int f = statement.lastIndexOf(')')
        def cols = statement.substring(d, f).split(',').collect {it.trim().split()[0]}
        schemas.get(tn.schema).tables.get(tn.tableName).index(name, cols)
    }

    int fkCounter = 0
    private void defineForeignKey(String statement) {
        fkCounter++
    }
}
