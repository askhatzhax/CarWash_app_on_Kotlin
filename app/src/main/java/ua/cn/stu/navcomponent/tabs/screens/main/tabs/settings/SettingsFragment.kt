    package ua.cn.stu.navcomponent.tabs.screens.main.tabs.settings

    import NotificationHelper
    import android.app.Activity
    import android.content.Intent
    import android.content.SharedPreferences
    import android.content.res.Configuration
    import android.media.MediaFormat.KEY_LANGUAGE
    import android.net.Uri
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.*
    import androidx.appcompat.app.AppCompatActivity
    import androidx.fragment.app.Fragment
    import androidx.preference.PreferenceManager
    import ua.cn.stu.navcomponent.tabs.R
    import ua.cn.stu.navcomponent.tabs.screens.main.usermaiil
    import java.util.*


    class SettingsFragment : Fragment(R.layout.fragment_setting) {
        private lateinit var versionTextView: TextView
        private lateinit var spinner: Spinner
        private lateinit var sharedPref: SharedPreferences
        val languages = arrayOf("Русский", "Қазақ тілі","English language")
        lateinit var language: String
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_setting, container, false)
            versionTextView = view.findViewById(R.id.version_text)
            val versionName = requireContext().packageManager.getPackageInfo(
                requireContext().packageName, 0
            ).versionName
            versionTextView.text = "Version: $versionName"
            // обработка нажатия на кнопку call
            val callButton: Button = view.findViewById(R.id.call_button)
            callButton.setOnClickListener {
                val phoneNumber = "8 700 246 59 68" // Ваш номер телефона
                openDialerWithNumber(phoneNumber)
            }
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = resources.getString(R.string.screen_settings)

            spinner = view.findViewById(R.id.language_spinner)
            sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    language = when (position) {
                        0 -> "ru"
                        1 -> "kz"
                        2 -> "en"
                        else -> "ru"
                    }
                    setLocale(language)
                    val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    // Действия для смены языка приложения на выбранный язык
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
            return view
        }
        private fun openDialerWithNumber(phoneNumber: String) {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$phoneNumber")

            // Проверка, есть ли приложение для обработки интента
            if (dialIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(dialIntent)
            } else {
                // Если нет приложения для обработки интента, выведите сообщение об ошибке или предупреждение
                Toast.makeText(requireContext(), "Приложение для звонков не найдено", Toast.LENGTH_SHORT).show()
            }
        }

        private fun updateViews() {
            val language = sharedPref.getString(KEY_LANGUAGE, language)
            setLocale(language)
        }

        private fun setLocale(language: String?) {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val resources = context?.resources
            val configuration = Configuration(resources?.configuration)
            configuration.setLocale(locale)

            resources?.updateConfiguration(configuration, resources.displayMetrics)
        }

        override fun onResume() {
            super.onResume()
            updateViews()
            val notificationHelper = NotificationHelper(requireContext())
            notificationHelper.showNotification("У вас важное уведомление","Время помыть машину, используйте наше приложение.")
        }

        fun openTermsOfUseUrl(view: View?) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com/terms"))
            startActivity(intent)
        }
    }






