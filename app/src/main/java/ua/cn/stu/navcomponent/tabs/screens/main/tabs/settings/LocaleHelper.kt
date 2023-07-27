import android.content.Context
import androidx.preference.PreferenceManager
import java.util.*

class LocaleHelper {
    companion object {
        fun setLocale(context: Context, language: String) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

        fun onAttach(context: Context): Context {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val language = preferences.getString("language", "ru") // "en" - язык по умолчанию
            setLocale(context, language!!)
            return context
        }
    }
}
