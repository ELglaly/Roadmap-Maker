package com.roadmap.backendapi.service.prompt;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Metadata for prompt templates loaded from YAML configuration.
 */
@Data
public class PromptMetadata {
    private String latestVersion;
    private List<VersionInfo> versions;

    @Data
    public static class VersionInfo {
        private String version;
        private String file;
        private String createdAt;
        private String description;
        private List<String> activeProfiles;
    }

    /**
     * Creates PromptMetadata from a map (typically from YAML parsing).
     *
     * @param data the data map
     * @return the PromptMetadata instance
     */
    @SuppressWarnings("unchecked")
    public static PromptMetadata from(Map<String, Object> data) {
        PromptMetadata metadata = new PromptMetadata();
        metadata.setLatestVersion((String) data.get("latest_version"));

        List<Map<String, Object>> versionsList = (List<Map<String, Object>>) data.get("versions");
        if (versionsList != null) {
            List<VersionInfo> versions = versionsList.stream()
                    .map(PromptMetadata::createVersionInfo)
                    .toList();
            metadata.setVersions(versions);
        }

        return metadata;
    }

    @SuppressWarnings("unchecked")
    private static VersionInfo createVersionInfo(Map<String, Object> data) {
        VersionInfo info = new VersionInfo();
        info.setVersion((String) data.get("version"));
        info.setFile((String) data.get("file"));
        info.setCreatedAt((String) data.get("created_at"));
        info.setDescription((String) data.get("description"));
        info.setActiveProfiles((List<String>) data.get("active_profiles"));
        return info;
    }

    /**
     * Gets the file name for a specific version.
     *
     * @param version the version string
     * @return the file name
     */
    public String getFileName(String version) {
        if (versions == null) {
            return null;
        }
        return versions.stream()
                .filter(v -> v.getVersion().equals(version))
                .map(VersionInfo::getFile)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a list of all version strings.
     *
     * @return list of version strings
     */
    public List<String> getVersionList() {
        if (versions == null) {
            return List.of();
        }
        return versions.stream()
                .map(VersionInfo::getVersion)
                .toList();
    }
}
