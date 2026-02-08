package com.roadmap.backendapi.service.prompt;

import java.util.List;

/**
 * Service interface for managing prompt templates.
 */
public interface PromptTemplateManagement {

    /**
     * Reloads all templates from disk.
     */
    void reloadTemplates();

    /**
     * Gets available versions for a template.
     *
     * @param templateCategory template category
     * @param templateName template name
     * @return list of version strings
     */
    List<String> getAvailableVersions(String templateCategory, String templateName);

    /**
     * Validates template syntax.
     *
     * @param templateCategory template category
     * @param templateName template name
     * @return true if template is valid, false otherwise
     */
    boolean validateTemplate(String templateCategory, String templateName);
}
