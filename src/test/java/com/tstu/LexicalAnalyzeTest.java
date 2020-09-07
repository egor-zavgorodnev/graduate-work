package com.tstu;

import com.tstu.backend.ILexicalAnalyzer;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.model.Keyword;

import com.tstu.backend.lexical.LexicalAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import static com.tstu.backend.model.enums.Lexems.*;

import java.util.List;

public class LexicalAnalyzeTest {

    ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

    @Test
    public void NoNameToThisTest() throws LexicalAnalyzeException {
        List<Keyword> expectedList = List.of(new Keyword("Var", NAME),
                new Keyword("num", NAME),
                new Keyword(ASSIGN.getStringValue(), ASSIGN),
                new Keyword("\n", SPLITTER)
        );
        List<Keyword> realList = lexicalAnalyzer.recognizeAllLexem("Var num := 31231 \n");
        //assertSize
        Assert.assertEquals(expectedList.size(), realList.size());
        //assert is lex in collection
        Assert.assertTrue(realList.containsAll(expectedList));
        //assert lex order in collection
        for (int i = 0; i < realList.size(); i++) {
            Assert.assertEquals(realList.get(i), expectedList.get(i));
        }

    }


    @Test(expected = LexicalAnalyzeException.class)
    public void TestNumberSizeMaxExpectException() throws LexicalAnalyzeException {
        lexicalAnalyzer.recognizeAllLexem(String.valueOf((long) Integer.MAX_VALUE + 1));
    }

    @Test(expected = LexicalAnalyzeException.class)
    public void TestNumberSizeMinExpectException() throws LexicalAnalyzeException {
        lexicalAnalyzer.recognizeAllLexem(String.valueOf((long) Integer.MIN_VALUE - 1));
    }

    @Test
    public void SevenCommasInRowTest() throws LexicalAnalyzeException {
        List<Keyword> expectedList = List.of(new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI)
        );
        List<Keyword> realList = lexicalAnalyzer.recognizeAllLexem(",,,,,,,");
        //assertSize
        Assert.assertEquals(expectedList.size(), realList.size());
        //assert is lex in collection
        Assert.assertTrue(realList.containsAll(expectedList));
        //assert lex order in collection
        for (int i = 0; i < realList.size(); i++) {
            Assert.assertEquals(realList.get(i), expectedList.get(i));
        }

    }
    @Test
    public void SevenCommasInRowWithSpacesTest() throws LexicalAnalyzeException {
        List<Keyword> expectedList = List.of(new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI)
        );
        List<Keyword> realList = lexicalAnalyzer.recognizeAllLexem(" ,  ,,    ,      , ,         ");
        //assertSize
        Assert.assertEquals(expectedList.size(), realList.size());
        //assert is lex in collection
        Assert.assertTrue(realList.containsAll(expectedList));
        //assert lex order in collection
        for (int i = 0; i < realList.size(); i++) {
            Assert.assertEquals(realList.get(i), expectedList.get(i));
        }

    }
    @Test
    public void SevenCommasInRowWithSpacesAndOthersLexemsTest() throws LexicalAnalyzeException {
        List<Keyword> expectedList = List.of(new Keyword(",", SEMI),
                new Keyword("Egor",NAME),
                new Keyword(",", SEMI),
                new Keyword("!",NOT),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI),
                new Keyword(":=",ASSIGN),
                new Keyword(",", SEMI),
                new Keyword(",", SEMI)
        );
        List<Keyword> realList = lexicalAnalyzer.recognizeAllLexem(" , Egor ,!,    ,  :=    , ,         ");
        //assertSize
        Assert.assertEquals(expectedList.size(), realList.size());
        //assert is lex in collection
        Assert.assertTrue(realList.containsAll(expectedList));
        //assert lex order in collection
        for (int i = 0; i < realList.size(); i++) {
            Assert.assertEquals(realList.get(i), expectedList.get(i));
        }

    }
    //TODO я хз наверное так и должно быть или хз
    //@Test
    public void AssignAndColonTest() throws LexicalAnalyzeException {
        List<Keyword> expectedList = List.of(new Keyword(":=",ASSIGN),
                new Keyword(":",COLON),
                new Keyword(":=",ASSIGN),
                new Keyword("=",NAME),   //TODO хз почему у тебя так
                new Keyword(":=",ASSIGN),
                new Keyword("=",NAME),
                new Keyword("=",NAME),
                new Keyword(":",COLON),
                new Keyword(":",COLON),
                new Keyword(":",COLON),
                new Keyword(":=",ASSIGN),
                new Keyword("=",NAME)
                );
        List<Keyword> realList = lexicalAnalyzer.recognizeAllLexem(":=::==:===::::==");
        //assertSize
        Assert.assertEquals(expectedList.size(), realList.size());
        //assert is lex in collection
        Assert.assertTrue(realList.containsAll(expectedList));
        //assert lex order in collection
        for (int i = 0; i < realList.size(); i++) {
            Assert.assertEquals(realList.get(i), expectedList.get(i));
        }

    }


}
