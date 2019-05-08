
import android.content.Context
import android.preference.PreferenceManager

object PreferenceUtils {

    private const val PREF_LOGGED = "pref_logged"

    private fun getPreferences(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveLogged(context: Context, isLogged: Boolean) {
        getPreferences(context).edit().putBoolean(PREF_LOGGED, isLogged).apply()
    }

    fun getLogged(context: Context) = getPreferences(context).getBoolean(PREF_LOGGED, false)
}