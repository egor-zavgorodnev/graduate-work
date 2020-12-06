package com.tstu.backend.structures;

import com.tstu.backend.exceptions.ExpressionAnalyzeException;
import com.tstu.backend.model.Argument;

import java.util.ArrayList;
import java.util.List;

public class ArgumentList {

    static {
        arguments = new ArrayList<>();
    }

    private static List<Argument<String>> arguments;

    public static String getVariableValue(String word) throws ExpressionAnalyzeException {
        String value = arguments.stream()
                .filter(v -> v.getVariable().getName().equals(word))
                .findFirst()
                .orElseThrow(() -> new ExpressionAnalyzeException("Переменная не объявлена"))
                .getValue();

        try {
            Integer.parseInt(value);
            return value;
        }
        catch (NumberFormatException e) {
            return getVariableValue(value);
        }

    }

    public static void clear() {
        arguments.clear();
    }

    public static void addArgument(Argument<String> argument) {
        arguments.add(argument);
    }
}
