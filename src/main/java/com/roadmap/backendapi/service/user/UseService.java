package com.roadmap.backendapi.service.user;

/**
 * Comprehensive user service interface that combines user management,
 * search capabilities, and authentication services.
 * <p>
 * This interface provides a unified API for all user-related operations by
 * composing multiple specialized service interfaces:
 * <ul>
 *   <li>{@link UserManagementService} - User CRUD operations and lifecycle management</li>
 *   <li>{@link UserSearchService} - User search and retrieval capabilities</li>
 *   <li>{@link AuthService} - Authentication and authorization operations</li>
 * </ul>
 * </p>
 */

public interface UseService extends UserManagementService, UserSearchService, AuthService {
}
