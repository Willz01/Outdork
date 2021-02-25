package dev.samuelmcmurray.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import dev.samuelmcmurray.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val emailPreference: CheckBoxPreference? = findPreference("notifications")
        emailPreference!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, emailNotifications ->
                if(emailNotifications == true) {
                        TODO()
                    //set up notifications??
                }

                true
            }



        val themePreference: SwitchPreferenceCompat? = findPreference("theme")
        themePreference!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, darkTheme ->
                if (darkTheme == true) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                true
            }
    }
}