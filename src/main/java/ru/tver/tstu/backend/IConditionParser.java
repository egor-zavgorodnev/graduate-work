package ru.tstu.tver.backend;

import ru.tstu.tver.backend.exceptions.ConditionAnalyzeException;
import ru.tstu.tver.backend.exceptions.ExpressionAnalyzeException;
import ru.tstu.tver.backend.exceptions.LexicalAnalyzeException;

public interface IConditionParser {
    boolean parseCondition() throws ConditionAnalyzeException, LexicalAnalyzeException, ExpressionAnalyzeException;
}
