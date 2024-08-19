package utils

import android.content.Context

class TokenManager(context: Context) {
    private val prefs = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)
    private val tokenKey = "token"

    fun saveToken(token: String) {
        if (token.isNotEmpty()) {
            prefs.edit().putString(tokenKey, token).apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString(tokenKey, null)
    }
}
