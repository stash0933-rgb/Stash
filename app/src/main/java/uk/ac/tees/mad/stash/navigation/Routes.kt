package uk.ac.tees.mad.stash.navigation

object NavRoutes {

    const val LOGIN = "login"
    const val LOGIN_WITH_ARG = "login?email={email}"

    fun loginRoute(email: String? = null): String {
        return if (email != null) {
            "login?email=$email"
        } else {
            "login"
        }
    }
}
