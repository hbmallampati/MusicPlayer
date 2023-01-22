package musicplayer.cs371m.musicplayer

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import musicplayer.cs371m.musicplayer.databinding.ActivitySettingsBinding
import musicplayer.cs371m.musicplayer.databinding.ContentSettingsBinding
import kotlin.properties.Delegates

class SettingsManager : AppCompatActivity() {
    companion object {
        const val doLoopKey = "doLoop"
        const val songsPlayedKey = "songsPlayed"
    }
    private lateinit var binding : ContentSettingsBinding

    // XXX probably want a member variable
    private var loopingObtained : Boolean = false
    private var loopingSelected : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        setSupportActionBar(activitySettingsBinding.settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = activitySettingsBinding.contentSettings

        // XXX Write me

        val activityThatCalled = intent
        val callingBundle = activityThatCalled.extras
        binding.noOfSongsTv.text = String.format("%s", callingBundle?.getString(SettingsManager.songsPlayedKey))
        loopingObtained = callingBundle?.getBoolean(SettingsManager.doLoopKey) == true

        binding.loopSwitch.isChecked = loopingObtained
        loopingSelected = loopingObtained

        binding.loopSwitch?.setOnCheckedChangeListener { _, isChecked ->
            loopingSelected = isChecked
        }

        binding.cancelBt.setOnClickListener {
            cancelButton()
        }

        binding.okBt.setOnClickListener {
            okButton()
        }
    }

    private fun cancelButton() {
        // XXX Write me
        doFinish(loopingObtained)
    }

    private fun okButton() {
        // XXX Write me
        doFinish(loopingSelected)
    }

    // Return to MainActivity
    private fun doFinish(loop: Boolean) {

        // XXX Write me.  This function contains most of the "code" in this activity

        val returnIntent = Intent().apply {
            putExtra(doLoopKey, loop)
        }
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == android.R.id.home) {
            // If user clicks "up", then it it as if they clicked OK.  We commit
            // changes and return to parent
            okButton()
            true
        } else super.onOptionsItemSelected(item)
    }
}
