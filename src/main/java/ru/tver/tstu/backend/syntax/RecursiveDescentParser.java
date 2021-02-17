package ru.tver.tstu.backend.syntax;

import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.backend.model.Identifier;
import ru.tver.tstu.backend.model.Keyword;
import ru.tver.tstu.backend.model.enums.Command;
import ru.tver.tstu.backend.model.enums.IdentifierCategory;
import ru.tver.tstu.backend.model.enums.Lexem;
import ru.tver.tstu.util.*;

import java.util.Iterator;
import java.util.List;

public class RecursiveDescentParser {

    protected static final Logger logger = new CustomLogger(RecursiveDescentParser .class.getName());

    protected List<Keyword> lexems;
    protected final IdentifierTable identifierTable;

    protected Keyword currentKeyword;
    protected Iterator<Keyword> iterator;
    protected boolean hasErrors;

    public RecursiveDescentParser(List<Keyword> lexems, IdentifierTable nameTable) {
        this.lexems = lexems;
        this.identifierTable = nameTable;
        iterator = lexems.iterator();
    }

    protected void getNextKeyword() {
        if (iterator.hasNext()) {
            currentKeyword = iterator.next();
        }
    }

    // accept not command
    protected boolean isAccept(Lexem lex) {
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
    protected boolean isAccept(Command command) {
        if (currentKeyword.lex == Lexem.NAME) {
            Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
            if (identifier.getCategory().equals(IdentifierCategory.COMMAND)) {
                if (identifier.getName().toLowerCase().equals(command.getName().toLowerCase())) {
                    getNextKeyword();
                    return true;
                }
            }
        }
        return false;
    }

    // accept ident with category
    protected boolean isAccept(IdentifierCategory category) {
        if (currentKeyword.lex == Lexem.NAME) {
            Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
            if (identifier.getCategory().equals(category)) {
                getNextKeyword();
                return true;
            }
        }
        return false;
    }

    protected boolean isExpect(Lexem lex, int errorCode) {
        if (isAccept(lex)) {
            return true;
        }
        error(errorCode);
        return false;
    }

    protected boolean isExpect(Command command, int errorCode) {
        if (isAccept(command)) {
            return true;
        }
        error(errorCode);
        return false;
    }

    protected void factor() {
        if (currentKeyword.lex == Lexem.NAME) { // var or const
            isAccept(Lexem.NAME);
        } else if (currentKeyword.lex == Lexem.NUMBER) {
            isAccept(Lexem.NUMBER);
        } else if (isAccept(Lexem.LEFT_BRACKET)) {
            expression();
            isExpect(Lexem.RIGHT_BRACKET, 22);
        } else {
            error(12);
            getNextKeyword();
        }
    }

    protected void term() {
        factor();
        while (currentKeyword.lex == Lexem.MULTIPLICATION || currentKeyword.lex == Lexem.DIVISION) {
            getNextKeyword();
            factor();
        }
    }

    protected void expression() {
        if (currentKeyword.lex == Lexem.ADDITION || currentKeyword.lex == Lexem.SUBTRACTION) {
            getNextKeyword();
        }
        term();
        while (currentKeyword.lex == Lexem.ADDITION || currentKeyword.lex == Lexem.SUBTRACTION) {
            getNextKeyword();
            term();
        }
    }

    protected void condition() {
        if (isAccept(Command.ODD)) {
            expression();
        } else {
            expression();
            Lexem operator = currentKeyword.lex;
            if (operator == Lexem.EQUAL ||
                    operator == Lexem.NOT_EQUAL ||
                    operator == Lexem.LESS_THAN ||
                    operator == Lexem.LESS_OR_EQUAL_THAN ||
                    operator == Lexem.MORE_THAN ||
                    operator == Lexem.MORE_OR_EQUAL_THAN) {
                getNextKeyword();
                expression();
            } else {
                error(20);
                getNextKeyword();
            }
        }
    }


    protected void statement() {
        if (currentKeyword.lex == Lexem.NAME) {
        } else {
            error(11);
            getNextKeyword();
            return;
        }
        if (isAccept(IdentifierCategory.LOCAL_VAR)) {
            isExpect(Lexem.ASSIGN, 19);
            expression();
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

    protected void block() {
        if (isAccept(Command.CONST)) {
            do {
                Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
                if (currentKeyword.lex == Lexem.NAME) {
                    updateIdentifierInfo(IdentifierCategory.CONST);
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
                    updateIdentifierInfo(IdentifierCategory.LOCAL_VAR);
                }
                isExpect(Lexem.NAME, 4);
            } while (isAccept(Lexem.SEMI));
            isExpect(Lexem.SEMICOLON, 5);
        }
        while (isAccept(Command.PROCEDURE)) {
            if (currentKeyword.lex == Lexem.NAME) {
                updateIdentifierInfo(IdentifierCategory.PROCEDURE_NAME);
            }
            isExpect(Lexem.NAME, 4);
            isExpect(Lexem.SEMICOLON, 5);
            block();
            isExpect(Lexem.SEMICOLON, 5);
        }
        statement();
    }

    private void updateIdentifierInfo(IdentifierCategory category) {
        identifierTable.getIdentifier(currentKeyword.word).setCategory(category);
    }

    protected void error(int errorCode) {
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

    protected void program() {
        getNextKeyword();
        block();
        isExpect(Lexem.DOT, 9);
    }


    public boolean checkSyntax() {
        program();
        return !hasErrors;
    }
}
