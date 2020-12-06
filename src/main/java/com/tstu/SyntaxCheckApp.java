//package com.tstu;
//
//import com.tstu.backend.ILexicalAnalyzer;
//import com.tstu.backend.INameTable;
//import com.tstu.backend.ISyntaxAnalyzer;
//import com.tstu.backend.exceptions.*;
//import com.tstu.backend.generator.CodeGenerator;
//import com.tstu.backend.lexems.IdentifierTable;
//import com.tstu.backend.lexems.LexicalAnalyzer;
//import com.tstu.backend.model.Keyword;
//import com.tstu.backend.syntax.SyntaxAnalyzer;
//import com.tstu.util.CustomLogger;
//import com.tstu.util.Logger;
//
//import java.util.List;
//
//public class SyntaxCheckApp {
//
//    public static void main(String[] args) {
//
//        Logger logger = new CustomLogger("Compilier");
//
//        ILexicalAnalyzer lexicalAnalyzer;
//        INameTable nameTable;
//        ISyntaxAnalyzer syntaxAnalyzer;
//
//        lexicalAnalyzer = new LexicalAnalyzer();
//        nameTable = new IdentifierTable();
//
//        List<Keyword> lexems;
//        try {
//            lexems = lexicalAnalyzer.recognizeAllLexem(data);
//            nameTable.recognizeAllIdentifiers(lexems);
//        } catch (LexicalAnalyzeException e) {
//            logger.error(e.getMessage());
//            return false;
//        }
//
//        syntaxAnalyzer = new SyntaxAnalyzer(lexems, nameTable);
//
//        CodeGenerator.declareDataSegment();
//
//        try {
//            syntaxAnalyzer.checkSyntax();
//        } catch (ExpressionAnalyzeException | SyntaxAnalyzeException | ConditionAnalyzeException | LexicalAnalyzeException | WhileAnalyzeException e) {
//            logger.error(e.getMessage());
//            return false;
//        }
//    }
//}
