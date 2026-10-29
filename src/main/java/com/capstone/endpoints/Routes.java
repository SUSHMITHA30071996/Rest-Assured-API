// ABOUTME: Central registry of API endpoint paths for all services under test.
// ABOUTME: Provides constants for GoRest user/post APIs and AutomationExercise product APIs.

package com.capstone.endpoints;

public class Routes {

    // GoRest User endpoints
    public static final String USERS = "/public/v2/users";
    public static final String USER_BY_ID = "/public/v2/users/{userId}";

    // GoRest Post endpoints
    public static final String POSTS = "/public/v2/posts";
    public static final String USER_POSTS = "/public/v2/users/{userId}/posts";
    public static final String POST_COMMENTS = "/public/v2/posts/{postId}/comments";

}
