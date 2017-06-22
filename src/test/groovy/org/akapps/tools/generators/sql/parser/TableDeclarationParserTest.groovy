package org.akapps.tools.generators.sql.parser

import org.junit.Test

class TableDeclarationParserTest {

    def example = getClass().classLoader.getResource("sql/examples/create-table.sql")

    def readExample = { example.readLines('UTF-8').join('') }

    @Test
    void testReadTableName() {
        def parser = new TableDeclarationParser(readExample())

        assert parser.readTableName() == "BOOK"
    }

    @Test
    void testReadColumnDeclarations() {
        def parser = new TableDeclarationParser(readExample())

        assert parser.readColumnDeclarations().toList() == [
                "ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1,INCREMENT BY 1,NO MAXVALUE,NO CYCLE,ORDER)",
                "NAME VARCHAR(25) NOT NULL",
                "PUBLICATION DATE NOT NULL WITH DEFAULT CURRENT_DATE",
                "PRICE DECIMAL(8,2)",
                "CONSTRAINT PK_BOOK PRIMARY KEY (ID)"
        ]
    }
}
