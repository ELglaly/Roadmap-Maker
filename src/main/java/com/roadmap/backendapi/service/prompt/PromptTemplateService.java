package com.roadmap.backendapi.service.prompt;

import java.util.Map;

/**
 * Service interface for loading and rendering prompt templates.
 */
public interface PromptTemplateService {

    /**
     * Loads and renders a prompt template with variables.
     *
     * @param templateCategory template category (roadmap, milestone, resource)
     * @param templateName template name
     * @param variables variables to interpolate
     * @return rendered prompt
     */
    String renderPrompt(String templateCategory, String templateName, Map<String, Object> variables);

    /**
     * Loads a specific version of a template.
     *
     * @param templateCategory template category
     * @param templateName template name
     * @param version version string (e.g., "v1", "v2")
     * @param variables variables to interpolate
     * @return rendered prompt
     */
    String renderPrompt(String templateCategory, String templateName, String version, Map<String, Object> variables);
}
