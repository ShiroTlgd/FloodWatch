package br.com.fiap.rm98825.navigation

object Routes {
    const val ARG_ID = "id"

    const val SPLASH = "splash"
    const val INTRO = "intro"
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val WEATHER = "weather"
    const val FORM = "form"
    const val DETAILS = "details/{$ARG_ID}"
    const val EDIT = "edit/{$ARG_ID}"

    fun details(id: Int) = "details/$id"
    fun edit(id: Int) = "edit/$id"
}
