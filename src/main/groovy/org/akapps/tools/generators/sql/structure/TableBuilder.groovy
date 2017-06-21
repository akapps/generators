package org.akapps.tools.generators.sql.structure

import groovy.transform.CompileStatic

/**
 * A builder that helps creating a Table
 *
 * @author Antoine Kapps
 */
@CompileStatic
class TableBuilder {

    private static class ColumnDef {
        String name
        String type
        boolean nullable
        Serializable defaultValue
        String comment
    }

    final String name
    private String comment
    private List<ColumnDef> columns = []
    private List<Closure<Constraint>> constraints = []
    private List<Closure<Table.Index>> indexes = []

    TableBuilder(String name) {
        this.name = name
    }

    TableBuilder addColumn(String name, String type, boolean nullable) {
        columns << new ColumnDef(name: name, type: type, nullable: nullable)
        return this
    }

    TableBuilder addColumn(String name, String type, boolean nullable, Serializable defaultValue) {
        columns << new ColumnDef(name: name, type: type, nullable: nullable, defaultValue: defaultValue)
        return this
    }

    TableBuilder commentOnTable(String comment) {
        this.comment = comment
        return this
    }

    TableBuilder commentOnColumn(String name, String comment) {
        columns.find {it.name == name}.comment = comment
        return this
    }

    TableBuilder primaryKey(String name, String... columns) {
        constraints.add { Map<String, Column> cols ->
            return new PrimaryKey(name: name, columns: columns.collect {cols.get(it)})
        }
        return this
    }

    TableBuilder foreignKey(String name, List<String> columns, String targetTable, List<String> targetColumns) {
        constraints.add { Map<String, Column> cols ->
            List refs = columns.withIndex().collect { String col, int i -> [cols.get(col), targetColumns[i]]}
            return new ForeignKey(name: name, targetTable: targetTable, references: refs)
        }
        return this
    }

    TableBuilder foreignKey(String name, String column, String targetTable, String targetColumn) {
        return foreignKey(name, [column], targetTable, [targetColumn])
    }

    TableBuilder index(String name, List<String> columns) {
        indexes.add { Map<String, Column> cols ->
            return new Table.Index(name, columns.collect {cols.get(it)})
        }
        return this
    }

    TableBuilder index(String name, String column) {
        return index(name, [column])
    }

    Table build() {
        Map<String, Column> tableColumns = columns.collect {colDef ->
            return new Column(colDef.name, colDef.type, colDef.nullable, colDef.defaultValue, colDef.comment)
        }.collectEntries { [it.name, it] }

        List<Constraint> tableConstraints = constraints.collect { Closure<Constraint> it -> it.call(tableColumns) }
        List<Table.Index> tableIndexes = indexes.collect { Closure<Table.Index> it -> it.call(tableColumns) }

        return new Table(name, tableColumns, tableConstraints, tableIndexes, comment)
    }
}
