package dev.samuelmcmurray.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.preference.*
import com.google.firebase.auth.FirebaseAuth
import dev.samuelmcmurray.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val user = FirebaseAuth.getInstance().currentUser


        val emailPreference: CheckBoxPreference? = findPreference("notifications")
        emailPreference!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, emailNotifications ->
                if (emailNotifications == true) {
                    Toast.makeText(context, "You now receive notifications", Toast.LENGTH_SHORT).show()
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


        val deletePreference: Preference? = findPreference("delete")
        deletePreference!!.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { preference ->
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete your account?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                     user?.delete()
                        println("deleted " + user)
                        view?.findNavController()?.navigate(R.id.loginFragment)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()

                true
            }

        val helpPreference: Preference? = findPreference("help")
        helpPreference!!.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { preference  ->
                    view?.findNavController()?.navigate(R.id.helpFragment)

                true
            }



        val showNamePreference: CheckBoxPreference? = findPreference("showName")
        showNamePreference!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, showName ->
                if(showName == true) {
                    println("my name")
                }

                true
            }
    }


}