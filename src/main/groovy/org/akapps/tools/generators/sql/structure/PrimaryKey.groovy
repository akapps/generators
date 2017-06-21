package org.akapps.tools.generators.sql.structure

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * @author Antoine Kapps
 */
@EqualsAndHashCode
@ToString
class PrimaryKey extends Constraint {

    List<Column> columns

    PrimaryKey(Map map) {
        this.name = map.name
        this.columns = map.columns
    }
}
