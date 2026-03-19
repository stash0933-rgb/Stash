package uk.ac.tees.mad.stash.navigation

object NavRoutes {

    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val ADD_RECORD = "add_record"
    const val EDIT_RECORD = "edit_record/{recordId}"

    fun editRecordRoute(recordId: String): String {
        return "edit_record/$recordId"
    }
}
