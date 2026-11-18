package br.com.fiap.rm98825.data.local

import android.content.Context

class UserPreferences(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("floodwatch", Context.MODE_PRIVATE)

    var introSeen: Boolean
        get() = prefs.getBoolean(KEY_INTRO_SEEN, false)
        set(value) {
            prefs.edit().putBoolean(KEY_INTRO_SEEN, value).apply()
        }

    private companion object {
        const val KEY_INTRO_SEEN = "intro_seen"
    }
}
