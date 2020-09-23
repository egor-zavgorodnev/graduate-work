package com.tstu.backend.expressions;

import com.tstu.backend.IConditionParser;
import com.tstu.backend.INameTable;
import com.tstu.backend.exceptions.ConditionAnalyzeException;
import com.tstu.backend.exceptions.ExpressionAnalyzeException;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.generator.CodeGenerator;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.enums.Command;
import com.tstu.backend.model.enums.Lexems;
import com.tstu.backend.model.enums.tCat;

import java.util.List;

public class ConditionParser implements IConditionParser {

    private List<List<Keyword>> conditionArea;
    private INameTable nameTable;

    public ConditionParser(List<List<Keyword>> conditionArea, INameTable nameTable) {
        this.conditionArea = conditionArea;
        this.nameTable = nameTable;
    }

    @Override
    public boolean parseCondition() throws ConditionAnalyzeException, LexicalAnalyzeException, ExpressionAnalyzeException {

        ExpressionParser expressionParser;

        switch (conditionArea.size()) {
            case 3:
                parseIfThen(conditionArea.get(0));
                CodeGenerator.addInstruction("if: ");
                expressionParser = new ExpressionParser(conditionArea.get(1), nameTable);
                expressionParser.parseExpression();
                CodeGenerator.addInstruction("jmp cont");
                CodeGenerator.addInstruction("cont:");

                break;
            case 5:
                parseIfThen(conditionArea.get(0));
                if (!conditionArea.get(2).get(0).word.equals(Command.ELSEIF.getName())) {
                    throw new ConditionAnalyzeException("Ожидается команда ELSEIF");
                }
                parseIfThen(conditionArea.get(2));
                break;
            default:
                parseIfThen(conditionArea.get(0));
                if (!conditionArea.get(2).get(0).word.equals(Command.ELSEIF.getName())) {
                    throw new ConditionAnalyzeException("Ожидается команда ELSEIF");
                }
                parseIfThen(conditionArea.get(2));
                if (!conditionArea.get(4).get(0).word.equals(Command.ELSE.getName())) {
                    throw new ConditionAnalyzeException("Ожидается команда ELSE");
                }
                break;
        }

        return true;


    }

    private void parseIfThen(List<Keyword> ifThenCodeLine) throws ConditionAnalyzeException, LexicalAnalyzeException {

        if (!nameTable.getIdentifier(ifThenCodeLine.get(1).word).getCategory().equals(tCat.VAR)) {
            throw new ConditionAnalyzeException("Ожидается переменная");
        }

        if (!(ifThenCodeLine.get(2).lex.equals(Lexems.EQUAL) || ifThenCodeLine.get(2).lex.equals(Lexems.NOT_EQUAL))) {
            throw new ConditionAnalyzeException("Ожидается операция");
        }

        switch (ifThenCodeLine.get(3).lex) {
            case TRUE:
            case FALSE:
                break;
            case NAME:
                if (!nameTable.getIdentifier(ifThenCodeLine.get(3).word).getCategory().equals(tCat.VAR)) {
                    throw new ConditionAnalyzeException("Ожидается переменная");
                }
                break;
            default:
                throw new ConditionAnalyzeException("Ожидается переменная или значение");
        }

        if (!ifThenCodeLine.get(4).word.equals(Command.THEN.getName())) {
            throw new ConditionAnalyzeException("Ожидается команда THEN");
        }

        CodeGenerator.addInstruction("mov ax," + ifThenCodeLine.get(1).word);
        StringBuilder value = new StringBuilder();
        value.append(
                ifThenCodeLine.get(3).lex.equals(Lexems.NAME) ?
                        ifThenCodeLine.get(3).word : ifThenCodeLine.get(3).word + "b"
        );
        CodeGenerator.addInstruction("mov bx," + value);
        value = new StringBuilder();
        CodeGenerator.addInstruction("cmp ax,bx");
        value.append(ifThenCodeLine.get(2).lex.equals(Lexems.EQUAL) ? "je" : "jne");
        CodeGenerator.addInstruction(value + " if");
        CodeGenerator.addInstruction("jmp cont");

    }
}
