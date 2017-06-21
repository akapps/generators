package org.akapps.tools.generators.sql.structure

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Representation of a database column.
 *
 * @author Antoine Kapps
 */
@EqualsAndHashCode
@ToString
class Column {

    final String name
    final String type
    final boolean nullable
    final Serializable defaultValue
    final String comment

    Column(String name, String type, boolean nullable, Serializable defaultValue, String comment) {
        this.name = name
        this.type = type
        this.nullable = nullable
        this.defaultValue = defaultValue
        this.comment = comment
    }
}
