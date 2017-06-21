package org.akapps.tools.generators.sql.structure

import org.junit.Test

class TableBuilderTest {

    @Test
    void testBuild_Simple() {
        def table = new TableBuilder('MyNewTable')
            .addColumn('id', 'INTEGER', false)
            .addColumn('name', 'VARCHAR(25)', true)
            .addColumn('creation', 'DATE', false, 'CURRENT')
            .primaryKey('PK', 'id')
            .commentOnColumn('name', 'Name of the row')
            .commentOnTable('Table defined only for tests')
            .build()

        // then
        assert table.name == 'MyNewTable'
        assert table.comment == 'Table defined only for tests'
        def expectedColumns = [
                new Column('id', 'INTEGER', false, null, null),
                new Column('name', 'VARCHAR(25)', true, null, 'Name of the row'),
                new Column('creation', 'DATE', false, 'CURRENT', null)
        ]
        assert table.columns.values().toList() == expectedColumns
        assert table.primaryKey == new PrimaryKey(name: 'PK', columns: [expectedColumns[0]])
    }

    @Test
    void testBuild_ForeignKey() {
        def table = new TableBuilder('OtherTable')
                .addColumn('id', 'VARCHAR(25)', false)
                .addColumn('referencedId', 'INTEGER', false)
                .foreignKey("REF_ToMainTable", "referencedId", "MainTable", "id")
                .build()

        // then
        assert table.foreignKeys.size() == 1

        def fk = table.foreignKeys.first()
        assert fk.name == 'REF_ToMainTable'
        assert fk.targetTable == 'MainTable'
        assert fk.participators == [new ForeignKey.Reference(table.columns['referencedId'], 'id')]
    }

    @Test
    void testBuild_Indexes() {
        def table = new TableBuilder('IndexedTable')
                .addColumn('id', 'VARCHAR(25)', false)
                .addColumn('referencedId', 'INTEGER', false)
                .addColumn('name', 'VARCHAR(25)', true)
                .addColumn('creation', 'DATE', false, 'CURRENT')
                .index("index1", 'referencedId')
                .index("indexDouble", ['name', 'creation'])
                .build()

        // then
        def columns = table.columns

        def expectedIndexes = [
                new Table.Index("index1", [columns.get('referencedId')]),
                new Table.Index("indexDouble", [columns.get('name'), columns.get('creation')])
        ]
        assert table.indexes == expectedIndexes
    }
}
