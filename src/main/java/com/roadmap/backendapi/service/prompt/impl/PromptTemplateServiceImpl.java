package com.roadmap.backendapi.service.prompt.impl;

import com.roadmap.backendapi.exception.PromptTemplateNotFoundException;
import com.roadmap.backendapi.service.prompt.PromptMetadataLoader;
import com.roadmap.backendapi.service.prompt.PromptService;
import com.roadmap.backendapi.service.prompt.PromptVariableResolver;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Implementation of prompt template service with caching support.
 */
@Service
public class PromptTemplateServiceImpl implements PromptService {

    private final ResourceLoader resourceLoader;
    private final PromptMetadataLoader metadataLoader;
    private final PromptVariableResolver variableResolver;

    public PromptTemplateServiceImpl(
            ResourceLoader resourceLoader,
            PromptMetadataLoader metadataLoader,
            PromptVariableResolver variableResolver) {
        this.resourceLoader = resourceLoader;
        this.metadataLoader = metadataLoader;
        this.variableResolver = variableResolver;
    }

    @Override
    @Cacheable(value = "promptTemplates", key = "#templateCategory + '_' + #templateName")
    public String renderPrompt(String templateCategory, String templateName, Map<String, Object> variables) {
        String version = metadataLoader.getLatestVersion(templateCategory, templateName);
        return renderPrompt(templateCategory, templateName, version, variables);
    }

    @Override
    @Cacheable(value = "promptTemplates", key = "#templateCategory + '_' + #templateName + '_' + #version")
    public String renderPrompt(String templateCategory, String templateName, String version, Map<String, Object> variables) {
        String templateContent = loadTemplate(templateCategory, templateName, version);
        return variableResolver.resolve(templateContent, variables);
    }

    @Override
    public void reloadTemplates() {
        metadataLoader.reload();
    }

    @Override
    public List<String> getAvailableVersions(String templateCategory, String templateName) {
        return metadataLoader.getVersions(templateCategory, templateName);
    }

    @Override
    public boolean validateTemplate(String templateCategory, String templateName) {
        try {
            String template = loadTemplate(templateCategory, templateName, "latest");
            return template != null && !template.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private String loadTemplate(String category, String name, String version) {
        String fileName = metadataLoader.getFileName(category, name, version);
        String path = String.format("classpath:prompts/%s/%s", category, fileName);

        try {
            Resource resource = resourceLoader.getResource(path);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PromptTemplateNotFoundException(
                    String.format("Template not found: %s/%s version %s", category, name, version)
            );
        }
    }
}
