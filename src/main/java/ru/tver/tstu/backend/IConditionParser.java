package ru.tver.tstu.backend;

import ru.tver.tstu.backend.exceptions.ConditionAnalyzeException;
import ru.tver.tstu.backend.exceptions.ExpressionAnalyzeException;
import ru.tver.tstu.backend.exceptions.LexicalAnalyzeException;

public interface IConditionParser {
    boolean parseCondition() throws ConditionAnalyzeException, LexicalAnalyzeException, ExpressionAnalyzeException;
}
