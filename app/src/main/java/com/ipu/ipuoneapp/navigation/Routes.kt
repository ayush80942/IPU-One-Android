package com.ipu.ipuoneapp.navigation

sealed class Routes(val route: String) {
    object Auth : Routes("auth")

    object Otp : Routes("otp/{email}") {
        fun createRoute(email: String) = "otp/$email"
    }

    object Import : Routes("import?isRefresh={isRefresh}&enroll={enroll}") {
        fun createRoute(isRefresh: Boolean, enroll: String = "") = "import?isRefresh=$isRefresh&enroll=$enroll"
    }

    object Main : Routes("main")

    object Home : Routes("home")

    object Notices : Routes("notices")

    object Services : Routes("services")
    object Results : Routes("results")
    object Profile : Routes("profile")
}