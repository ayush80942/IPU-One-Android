package com.ipu.ipuoneapp.core.config

object AppConfig {
    /**
     * The base URL of the backend API.
     */
    const val BASE_URL = "http://tmsk864q5tetm736i55qlyi0.213.210.37.18.sslip.io/"

    /**
     * Web OAuth client ID used as the audience for native Google Sign-In (Credential Manager).
     * This is the same client ID the backend validates ID tokens against — it is not a secret.
     */
    const val GOOGLE_WEB_CLIENT_ID = "983536591953-5veu6443bvjb5ibv7tndr9veejnf9k2a.apps.googleusercontent.com"
}