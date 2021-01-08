package ru.tstu.tver.backend;

import com.tstu.backend.exceptions.*;
import ru.tstu.backend.exceptions.*;
import ru.tstu.tver.backend.exceptions.*;
import ru.tver.backend.exceptions.*;

public interface ISyntaxAnalyzer {
    boolean checkSyntax() throws SyntaxAnalyzeException, ExpressionAnalyzeException, ConditionAnalyzeException, LexicalAnalyzeException, WhileAnalyzeException;
}
