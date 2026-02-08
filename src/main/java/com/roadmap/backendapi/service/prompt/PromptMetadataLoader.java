package com.roadmap.backendapi.service.prompt;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads and caches prompt metadata from YAML configuration.
 */
@Component
public class PromptMetadataLoader {

    private final ResourceLoader resourceLoader;
    private final Map<String, PromptMetadata> metadataCache;

    public PromptMetadataLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.metadataCache = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void loadMetadata() {
        try {
            Resource resource = resourceLoader.getResource("classpath:prompts/prompt-metadata.yml");
            Yaml yaml = new Yaml();

            @SuppressWarnings("unchecked")
            Map<String, Object> data = yaml.load(resource.getInputStream());

            // Parse and cache metadata
            @SuppressWarnings("unchecked")
            Map<String, Object> templates = (Map<String, Object>) data.get("templates");

            if (templates != null) {
                templates.forEach((category, categoryData) -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> categoryMap = (Map<String, Object>) categoryData;
                    categoryMap.forEach((name, nameData) -> {
                        String key = category + ":" + name;
                        @SuppressWarnings("unchecked")
                        PromptMetadata metadata = PromptMetadata.from((Map<String, Object>) nameData);
                        metadataCache.put(key, metadata);
                    });
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prompt metadata", e);
        }
    }

    /**
     * Gets the latest version for a template.
     *
     * @param category template category
     * @param name template name
     * @return the latest version string
     */
    public String getLatestVersion(String category, String name) {
        PromptMetadata metadata = metadataCache.get(category + ":" + name);
        return metadata != null ? metadata.getLatestVersion() : "v1";
    }

    /**
     * Gets the file name for a specific version.
     *
     * @param category template category
     * @param name template name
     * @param version version string
     * @return the file name
     */
    public String getFileName(String category, String name, String version) {
        PromptMetadata metadata = metadataCache.get(category + ":" + name);
        if (metadata == null) {
            return name + "_" + version + ".txt";
        }

        if ("latest".equals(version)) {
            version = metadata.getLatestVersion();
        }

        return metadata.getFileName(version);
    }

    /**
     * Gets all available versions for a template.
     *
     * @param category template category
     * @param name template name
     * @return list of version strings
     */
    public List<String> getVersions(String category, String name) {
        PromptMetadata metadata = metadataCache.get(category + ":" + name);
        return metadata != null ? metadata.getVersionList() : List.of("v1");
    }

    /**
     * Reloads metadata from disk.
     */
    public void reload() {
        metadataCache.clear();
        loadMetadata();
    }
}
