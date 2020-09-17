package com.tstu;

import com.tstu.backend.ILexicalAnalyzer;
import com.tstu.backend.INameTable;
import com.tstu.backend.ISyntaxAnalyzer;
import com.tstu.backend.exceptions.ExpressionAnalyzeException;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.exceptions.SyntaxAnalyzeException;
import com.tstu.backend.lexems.IdentifierTable;
import com.tstu.backend.lexems.LexicalAnalyzer;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.syntax.SyntaxAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ExpressionTest {

    private ISyntaxAnalyzer syntaxAnalyzer;

    private void init(String data) throws LexicalAnalyzeException {
        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(
                "Var a,b,c,res : Logical\n" +
                        "Begin\n" +
                        "a:=0\n" +
                        "b:=0\n" +
                        "c:=0\n" +
                        data +
                        "End\n"
        );
        INameTable nameTable = new IdentifierTable();
        nameTable.recognizeAllIdentifiers(lexems);
        syntaxAnalyzer = new SyntaxAnalyzer(lexems, nameTable);
    }

    @Test
    public void correctOrOperation() throws LexicalAnalyzeException, ExpressionAnalyzeException, SyntaxAnalyzeException {
        init("res:=a|b\n");
        Assert.assertTrue(syntaxAnalyzer.checkSyntax());
    }

    @Test
    public void correctAssign() throws LexicalAnalyzeException, ExpressionAnalyzeException, SyntaxAnalyzeException {
        init("res:=a\n");
        Assert.assertTrue(syntaxAnalyzer.checkSyntax());
    }

    @Test(expected = ExpressionAnalyzeException.class)
    public void notCorrectTwoOperationInARow() throws LexicalAnalyzeException, ExpressionAnalyzeException, SyntaxAnalyzeException {
        init("res:=a||\n");
        Assert.assertTrue(syntaxAnalyzer.checkSyntax());
    }
}
