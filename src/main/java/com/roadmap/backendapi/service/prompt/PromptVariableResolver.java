package com.roadmap.backendapi.service.prompt;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves variables in prompt templates using {{variable.path}} syntax.
 */
@Component
public class PromptVariableResolver {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{([^}]+)\\}\\}");

    /**
     * Resolves variables in template using {{variable.path}} syntax.
     *
     * @param template the template string
     * @param variables the variables map
     * @return the resolved template
     */
    public String resolve(String template, Map<String, Object> variables) {
        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String variablePath = matcher.group(1).trim();
            String value = resolveVariablePath(variablePath, variables);
            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    private String resolveVariablePath(String path, Map<String, Object> variables) {
        String[] parts = path.split("\\.");
        Object current = variables;

        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else if (current != null) {
                current = getProperty(current, part);
            }

            if (current == null) {
                return "";
            }
        }

        return current.toString();
    }

    private Object getProperty(Object obj, String propertyName) {
        try {
            String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            return obj.getClass().getMethod(methodName).invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
