package com.tstu.backend.syntax;

import com.tstu.backend.INameTable;
import com.tstu.backend.ISyntaxAnalyzer;
import com.tstu.backend.generator.PL0CodeGenerator;
import com.tstu.backend.model.Identifier;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.enums.Command;
import com.tstu.backend.model.enums.Function;
import com.tstu.backend.model.enums.IdentifierCategory;
import com.tstu.backend.model.enums.Lexem;
import com.tstu.backend.structures.ExpressionParser;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecursiveDescentParser implements ISyntaxAnalyzer {

    private static final Logger logger = Logger.getLogger(RecursiveDescentParser.class.getName());

    private List<Keyword> lexems;
    private List<Keyword> currentExpression;
    private final INameTable identifierTable;

    private Keyword currentKeyword;
    private Iterator<Keyword> iterator;
    private boolean hasErrors;

    private int currentDataAddress = 3; // because exists system vars RA,DL,SL
    private int currentLevel = 0;

    public RecursiveDescentParser(List<Keyword> lexems, INameTable nameTable) {
        this.lexems = lexems;
        this.identifierTable = nameTable;
        iterator = lexems.iterator();
        currentExpression = new ArrayList<>();
    }

    void getNextKeyword() {
        if (iterator.hasNext()) {
            currentKeyword = iterator.next();
        }
    }

    // accept not command
    boolean isAccept(Lexem lex) {
        if (currentKeyword.lex == lex) {
            if (currentKeyword.lex == Lexem.NAME) {
                Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
                if (identifier.getCategory().equals(IdentifierCategory.COMMAND)) {
                    return false;
                }
            }
            getNextKeyword();
            return true;
        }
        return false;
    }

    // accept command only
    boolean isAccept(Command command) {
        if (currentKeyword.lex == Lexem.NAME) {
            Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
            if (identifier.getCategory().equals(IdentifierCategory.COMMAND)) {
                if (identifier.getName().equals(command.getName())) {
                    getNextKeyword();
                    return true;
                }
            }
        }
        return false;
    }

    // accept ident with category
    boolean isAccept(IdentifierCategory category) {
        if (currentKeyword.lex == Lexem.NAME) {
            Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
            if (identifier.getCategory().equals(category)) {
                getNextKeyword();
                return true;
            }
        }
        return false;
    }

    boolean isExpect(Lexem lex, int errorCode) {
        if (isAccept(lex)) {
            return true;
        }
        error(errorCode);
        return false;
    }

    boolean isExpect(Command command, int errorCode) {
        if (isAccept(command)) {
            return true;
        }
        error(errorCode);
        return false;
    }

    void factor() {
        if (currentKeyword.lex == Lexem.NAME) { // var or const
            currentExpression.add(currentKeyword);
            isAccept(Lexem.NAME);
        } else if (currentKeyword.lex == Lexem.NUMBER) {
            currentExpression.add(currentKeyword);
            isAccept(Lexem.NUMBER);
        } else if (isAccept(Lexem.LEFT_BRACKET)) {
            expression();
            currentExpression.add(currentKeyword);
            isExpect(Lexem.RIGHT_BRACKET, 22);
        } else {
            error(12);
            getNextKeyword();
        }
    }

    void term() {
        factor();
        while (currentKeyword.lex == Lexem.MULTIPLICATION || currentKeyword.lex == Lexem.DIVISION) {
            currentExpression.add(currentKeyword);
            getNextKeyword();
            factor();
        }
    }

    void expression() {
        if (currentKeyword.lex == Lexem.ADDITION || currentKeyword.lex == Lexem.SUBTRACTION) {
            currentExpression.add(currentKeyword);
            getNextKeyword();
        }
        term();
        while (currentKeyword.lex == Lexem.ADDITION || currentKeyword.lex == Lexem.SUBTRACTION) {
            currentExpression.add(currentKeyword);
            getNextKeyword();
            term();
        }
    }

    void condition() {
        if (isAccept(Command.ODD)) {
            expression();
        } else {
            expression();
            if (currentKeyword.lex == Lexem.EQUAL ||
                    currentKeyword.lex == Lexem.NOT_EQUAL ||
                    currentKeyword.lex == Lexem.LESS_THAN ||
                    currentKeyword.lex == Lexem.LESS_OR_EQUAL_THAN ||
                    currentKeyword.lex == Lexem.MORE_THAN ||
                    currentKeyword.lex == Lexem.MORE_OR_EQUAL_THAN) {
                getNextKeyword();
                expression();
            } else {
                error(20);
                getNextKeyword();
            }
        }
    }

    void statement() {
        Identifier identifier;
        if (currentKeyword.lex == Lexem.NAME) {
            identifier = identifierTable.getIdentifier(currentKeyword.word);
        } else {
            error(11);
            getNextKeyword();
            return;
        }
        if (isAccept(IdentifierCategory.VAR)) {
            isExpect(Lexem.ASSIGN, 19);
            currentExpression.clear();
            expression();
            currentExpression.forEach(expr -> System.out.print(expr.word));
            ExpressionParser expressionParser = new ExpressionParser(currentExpression, identifierTable);
            expressionParser.parseExpression();
            PL0CodeGenerator.addInstruction(Function.STO, identifier.getLevel(), identifier.getAddress());
        } else if (isAccept(Command.CALL)) {
            isExpect(Lexem.NAME, 14);
        } else if (isAccept(Command.BEGIN)) {
            do {
                statement();
            } while (isAccept(Lexem.SEMICOLON));
            isExpect(Command.END, 17);
        } else if (isAccept(Command.IF)) {
            condition();
            isExpect(Command.THEN, 16);
            statement();
        } else if (isAccept(Command.WHILE)) {
            condition();
            isExpect(Command.DO, 18);
            statement();
        } else {
            error(11);
            getNextKeyword();
        }
    }

    void block() {
        currentLevel++;
        if (isAccept(Command.CONST)) {
            do {
                Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
                if (currentKeyword.lex == Lexem.NAME) {
                    updateIdentifierInfo(IdentifierCategory.CONST, currentLevel, "0");
                }
                isExpect(Lexem.NAME, 4);
                isExpect(Lexem.EQUAL, 3);
                if (currentKeyword.lex == Lexem.NUMBER) {
                    identifier.setValue(Integer.parseInt(currentKeyword.word));
                }
                isExpect(Lexem.NUMBER, 2);
            } while (isAccept(Lexem.SEMI));
            isExpect(Lexem.SEMICOLON, 5);
        }
        if (isAccept(Command.VAR)) {
            do {
                if (currentKeyword.lex == Lexem.NAME) {
                    updateIdentifierInfo(IdentifierCategory.VAR, currentLevel, String.valueOf(currentDataAddress++));
                }
                isExpect(Lexem.NAME, 4);
            } while (isAccept(Lexem.SEMI));
            PL0CodeGenerator.addInstruction(Function.INT, currentLevel, String.valueOf(currentDataAddress));
            isExpect(Lexem.SEMICOLON, 5);
        }
        while (isAccept(Command.PROCEDURE)) {
            if (currentKeyword.lex == Lexem.NAME) {
                updateIdentifierInfo(IdentifierCategory.PROCEDURE_NAME, currentLevel, "0");
            }
            isExpect(Lexem.NAME, 4);
            isExpect(Lexem.SEMICOLON, 5);
            block();
            isExpect(Lexem.SEMICOLON, 5);
        }
        statement();
        currentLevel--;
    }

    private void updateIdentifierInfo(IdentifierCategory category, int level, String address) {
        identifierTable.getIdentifier(currentKeyword.word).setCategory(category);
        identifierTable.getIdentifier(currentKeyword.word).setLevel(level);
        identifierTable.getIdentifier(currentKeyword.word).setAddress(address);
    }

    void error(int errorCode) {
        hasErrors = true;
        switch (errorCode) {
            case 1:
                logger.error("Use = instead of := ");
                break;
            case 2:
                logger.error("= must be followed by a number");
                break;
            case 3:
                logger.error("Identifier must be followed by = ");
                break;
            case 4:
                logger.error("const, var, procedure must be followed by an identifier");
                break;
            case 5:
                logger.error("Semicolon or comma missing");
                break;
            case 6:
                logger.error("Incorrect symbol after procedure declaration");
                break;
            case 7:
                logger.error("Statement expected");
                break;
            case 8:
                logger.error("Incorrect symbol after statement part in block");
                break;
            case 9:
                logger.error("Period expected");
                break;
            case 10:
                logger.error("Semicolon between statements is missing");
                break;
            case 11:
                logger.error("Statement: syntax error");
                break;
            case 12:
                logger.error("Factor: syntax error");
                break;
            case 13:
                logger.error("Assignment operator := expected");
                break;
            case 14:
                logger.error("call must be followed by an identifier");
                break;
            case 15:
                logger.error("Call of a constant or a variable is meaningless");
                break;
            case 16:
                logger.error("then expected");
                break;
            case 17:
                logger.error("Semicolon or end expected");
                break;
            case 18:
                logger.error("do expected");
                break;
            case 19:
                logger.error("Incorrect symbol following statement");
                break;
            case 20:
                logger.error("Condition: invalid operator");
                break;
            case 21:
                logger.error("Expression must not contain a procedure identifier");
                break;
            case 22:
                logger.error("Right parenthesis missing.");
                break;
            case 23:
                logger.error("The preceding factor cannot be followed by this symbol");
                break;
            case 24:
                logger.error("An expression cannot begin with this symbol");
                break;
            case 25:
                logger.error("This number is too large");
                break;
        }
    }

    void program() {
        getNextKeyword();
        block();
        isExpect(Lexem.DOT, 9);
    }


    @Override
    public boolean checkSyntax() {
        program();
        return hasErrors;
    }
}
