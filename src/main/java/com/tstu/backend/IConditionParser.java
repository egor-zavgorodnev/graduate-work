package com.tstu.backend;

import com.tstu.backend.exceptions.ConditionAnalyzeException;
import com.tstu.backend.exceptions.ExpressionAnalyzeException;
import com.tstu.backend.exceptions.LexicalAnalyzeException;

public interface IConditionParser {
    boolean parseCondition() throws ConditionAnalyzeException, LexicalAnalyzeException, ExpressionAnalyzeException;
}
