package com.ipu.ipuoneapp.core.config

object AppConfig {
    /**
     * The base URL of the backend API.
     */
    const val BASE_URL = "http://tmsk864q5tetm736i55qlyi0.213.210.37.18.sslip.io/"

    /**
     * Endpoint for initiating GitHub OAuth.
     */
    const val GITHUB_OAUTH_URL = "${BASE_URL}oauth2/authorization/github"

    /**
     * Endpoint for initiating Google OAuth.
     */
    const val GOOGLE_OAUTH_URL = "${BASE_URL}oauth2/authorization/google"
}