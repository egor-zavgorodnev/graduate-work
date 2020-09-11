package com.tstu;

import com.tstu.backend.ILexicalAnalyzer;
import com.tstu.backend.INameTable;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.lexems.IdentifierTable;
import com.tstu.backend.lexems.LexicalAnalyzer;
import com.tstu.backend.model.Identifier;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.enums.tCat;
import com.tstu.backend.model.enums.tType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class IdentifierTableTest
{

    INameTable nameTable;
    ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

    @Test
    public void TestNameTable() throws LexicalAnalyzeException {
//        List<Keyword> expectedList = List.of(new Keyword("Var", NAME),
//                new Keyword("num", NAME),
//                new Keyword(ASSIGN.getStringValue(), ASSIGN),
//                new Keyword("31231", NUMBER),
//                new Keyword("\n", SPLITTER)
//        );

        List<Keyword> keywords = lexicalAnalyzer.recognizeAllLexem("Var a,b,c :Logical \n");
        Set<Identifier> identifiers = Set.of(
                new Identifier("a",tCat.VAR,null),
                new Identifier("b",tCat.VAR,null),
                new Identifier("c",tCat.VAR,null),
                new Identifier("Var", tCat.COMMAND,null),
                new Identifier("Logical",tCat.TYPE, tType.BOOL)
        );
        nameTable = new IdentifierTable();
        nameTable.recognizeAllIdentifiers(keywords);
        Assert.assertTrue(nameTable.getIdentifiers().containsAll(identifiers));

    }

}
