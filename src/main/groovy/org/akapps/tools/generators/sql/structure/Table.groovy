package org.akapps.tools.generators.sql.structure

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * Representation of a database table.
 *
 * @author Antoine Kapps
 */
@EqualsAndHashCode
@ToString
class Table {

    @TupleConstructor
    @EqualsAndHashCode
    @ToString
    static class Index {
        String name
        List<Column> columns
    }

    final String name
    final Map<String, Column> columns
    final List<Constraint> constraints
    final List<Index> indexes
    final String comment

    Table(String name, Map<String, Column> columns, List<Constraint> constraints, List<Index> indexes, String comment) {
        this.name = name
        this.columns = columns
        this.constraints = constraints
        this.indexes = indexes
        this.comment = comment
    }

    PrimaryKey getPrimaryKey() {
        return constraints.find {it.class == PrimaryKey}
    }

    List<ForeignKey> getForeignKeys() {
        return constraints.findAll {it.class == ForeignKey}
    }
}
