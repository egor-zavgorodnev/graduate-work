package com.tstu.backend.syntax;

import com.tstu.backend.INameTable;
import com.tstu.backend.ISyntaxAnalyzer;
import com.tstu.backend.model.Identifier;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.enums.Command;
import com.tstu.backend.model.enums.IdentifierCategory;
import com.tstu.backend.model.enums.Lexems;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class RecursiveDescentParser implements ISyntaxAnalyzer {

    private static final Logger logger = Logger.getLogger(RecursiveDescentParser.class.getName());

    private List<Keyword> lexems;
    private final INameTable identifierTable;

    private Keyword currentKeyword;
    private Iterator<Keyword> iterator;
    private boolean hasErrors;

    public RecursiveDescentParser(List<Keyword> lexems, INameTable nameTable) {
        this.lexems = lexems;
        this.identifierTable = nameTable;
        iterator = lexems.iterator();
    }

    void getNextKeyword() {
        if (iterator.hasNext()) {
            currentKeyword = iterator.next();
        }
    }

    // accept not command
    boolean isAccept(Lexems lex) {
        if (currentKeyword.lex == lex) {
            if (currentKeyword.lex == Lexems.NAME) {
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
        if (currentKeyword.lex == Lexems.NAME) {
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
        if (currentKeyword.lex == Lexems.NAME) {
            Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
            if (identifier.getCategory().equals(category)) {
                getNextKeyword();
                return true;
            }
        }
        return false;
    }

    boolean isExpect(Lexems lex, int errorCode) {
        if (isAccept(lex)) {
            return true;
        }
        logger.error("Expect: unexpected symbol");
        error(errorCode);
        return false;
    }
    boolean isExpect(Command command, int errorCode) {
        if (isAccept(command)) {
            return true;
        }
        logger.error("Expect: unexpected symbol");
        error(errorCode);
        return false;
    }

    void factor() {
        if (isAccept(Lexems.NAME)) { // var or const

        } else if (isAccept(Lexems.NUMBER)) {

        } else if (isAccept(Lexems.LEFT_BRACKET)) {
            expression();
            isExpect(Lexems.RIGHT_BRACKET,22);
        } else {
            error(12);
            getNextKeyword();
        }
    }

    void term() {
        factor();
        while (currentKeyword.lex == Lexems.MULTIPLICATION || currentKeyword.lex == Lexems.DIVISION) {
            getNextKeyword();
            factor();
        }
    }

    void expression() {
        if (currentKeyword.lex == Lexems.ADDITION || currentKeyword.lex == Lexems.SUBTRACTION)
            getNextKeyword();
        term();
        while (currentKeyword.lex == Lexems.ADDITION || currentKeyword.lex == Lexems.SUBTRACTION) {
            getNextKeyword();
            term();
        }
    }

    void condition() {
        if (isAccept(Command.ODD)) {
            expression();
        } else {
            expression();
            if (currentKeyword.lex == Lexems.EQUAL ||
                    currentKeyword.lex == Lexems.NOT_EQUAL ||
                    currentKeyword.lex == Lexems.LESS_THAN ||
                    currentKeyword.lex == Lexems.LESS_OR_EQUAL_THAN ||
                    currentKeyword.lex == Lexems.MORE_THAN ||
                    currentKeyword.lex == Lexems.MORE_OR_EQUAL_THAN) {
                getNextKeyword();
                expression();
            } else {
                error(20);
                getNextKeyword();
            }
        }
    }

    void statement() {
        if (isAccept(IdentifierCategory.VAR)) {
            isExpect(Lexems.ASSIGN,19);
            expression();
        } else if (isAccept(Command.CALL)) {
            isExpect(Lexems.NAME,14);
        } else if (isAccept(Command.BEGIN)) {
            do {
                statement();
            } while (isAccept(Lexems.SEMICOLON));
            isExpect(Command.END,17);
        } else if (isAccept(Command.IF)) {
            condition();
            isExpect(Command.THEN,16);
            statement();
        } else if (isAccept(Command.WHILE)) {
            condition();
            isExpect(Command.DO,18);
            statement();
        }
        else {
            error(11);
            getNextKeyword();
        }
    }

    void block() {
        if (isAccept(Command.CONST)) {
            do {
                if (currentKeyword.lex == Lexems.NAME) {
                    updateIdentifierCategory(IdentifierCategory.CONST);
                }
                isExpect(Lexems.NAME,4);
                isExpect(Lexems.EQUAL,3);
                isExpect(Lexems.NUMBER,2);
            } while (isAccept(Lexems.SEMI));
            isExpect(Lexems.SEMICOLON,5);
        }
        if (isAccept(Command.VAR)) {
            do {
                if (currentKeyword.lex == Lexems.NAME) {
                    updateIdentifierCategory(IdentifierCategory.VAR);
                }
                isExpect(Lexems.NAME,4);
            } while (isAccept(Lexems.SEMI));
            isExpect(Lexems.SEMICOLON,5);
        }
        while (isAccept(Command.PROCEDURE)) {
            if (currentKeyword.lex == Lexems.NAME) {
                updateIdentifierCategory(IdentifierCategory.PROCEDURE_NAME);
            }
            isExpect(Lexems.NAME,4);
            isExpect(Lexems.SEMICOLON,5);
            block();
            isExpect(Lexems.SEMICOLON,5);
        }
        statement();
    }

    private void updateIdentifierCategory(IdentifierCategory category) {
       identifierTable.getIdentifier(currentKeyword.word).setCategory(category);
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
        isExpect(Lexems.DOT,9);
    }


    @Override
    public boolean checkSyntax() {
        program();
        return hasErrors;
    }
}
