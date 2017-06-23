package org.akapps.tools.generators.sql.parser

import org.junit.Test

class ColumnDeclarationParserTest {

    def readExample = {
        def example = getClass().classLoader.getResource("sql/examples/create-table.sql")
        example.readLines('UTF-8').subList(1, 5).collect {
            def line = it.trim()
            return line.substring(0, line.length()-1)
    } }

    @Test
    void readColumnName() {
        def examples = readExample()

        assert new ColumnDeclarationParser(examples[0]).readColumnName() == "ID"
        assert new ColumnDeclarationParser(examples[1]).readColumnName() == "NAME"
        assert new ColumnDeclarationParser(examples[2]).readColumnName() == "PUBLICATION"
        assert new ColumnDeclarationParser(examples[3]).readColumnName() == "PRICE"
    }

    @Test
    void readColumnType() {
        def examples = readExample()

        assert new ColumnDeclarationParser(examples[0]).readColumnType() == "INTEGER"
        assert new ColumnDeclarationParser(examples[1]).readColumnType() == "VARCHAR(25)"
        assert new ColumnDeclarationParser(examples[2]).readColumnType() == "DATE"
        assert new ColumnDeclarationParser(examples[3]).readColumnType() == "DECIMAL(8,2)"
    }

    @Test
    void readColumnType_Spaced() {
        def parser = new ColumnDeclarationParser("NAME VARCHAR (90)")
        assert parser.readColumnType() == "VARCHAR(90)"
    }

    @Test
    void readColumnType_InnerSpaced() {
        def parser = new ColumnDeclarationParser("PRICE DECIMAL(10, 2)")
        assert parser.readColumnType() == "DECIMAL(10,2)"
    }

    @Test
    void readColumnType_QuiteHarder() {
        def parser = new ColumnDeclarationParser("PRICE   DECIMAL  ( 10, 2 )")
        assert parser.readColumnType() == "DECIMAL(10,2)"
    }

    @Test
    void isDeclaredNonNullable() {
        def examples = readExample()

        assert new ColumnDeclarationParser(examples[0]).isDeclaredNonNullable()
        assert new ColumnDeclarationParser(examples[1]).isDeclaredNonNullable()
        assert new ColumnDeclarationParser(examples[2]).isDeclaredNonNullable()
        assert ! new ColumnDeclarationParser(examples[3]).isDeclaredNonNullable()
    }

    @Test
    void readDefaultValue() {
        def examples = readExample()

        assert new ColumnDeclarationParser(examples[0]).readDefaultValue() == null
        assert new ColumnDeclarationParser(examples[1]).readDefaultValue() == null
        assert new ColumnDeclarationParser(examples[2]).readDefaultValue() == "CURRENT_DATE"
        assert new ColumnDeclarationParser(examples[3]).readDefaultValue() == null
    }

    @Test
    void readDefaultValue_Unspecified() {
        def parser = new ColumnDeclarationParser("SOMETHING DATE WITH DEFAULT")
        assert parser.readDefaultValue() == ""
    }
}
