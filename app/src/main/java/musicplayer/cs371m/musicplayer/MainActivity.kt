package musicplayer.cs371m.musicplayer

import android.app.Activity
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import musicplayer.cs371m.musicplayer.databinding.ActivityMainBinding
import musicplayer.cs371m.musicplayer.databinding.ContentMainBinding
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.IntStream.range
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope()
{

    // A repository can be a local database or the network
    //  or a combination of both
    private val repository = Repository()
    private var songResources = repository.fetchData()

    // Create a list from the keys, which are song names
    private var songList = songResources.keys.toMutableList()

    private lateinit var player: MediaPlayer
    private lateinit var adapter: RVDiffAdapter

    private lateinit var binding : ContentMainBinding

    // When this is true, the displayTime coroutine should not modify the seek bar
    val userModifyingSeekBar = AtomicBoolean(false)

    //You may need some variables to record the music player's state.
    //XXX Write me
     private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK)
        {
            val data: Intent? = result.data
            data?.extras?.apply {
                loopSet = getBoolean(SettingsManager.doLoopKey)
                if(loopSet)
                    binding.loopTv.setBackgroundColor(getColor(R.color.loop_bgnd))
                else
                    binding.loopTv.setBackgroundColor(getColor(R.color.loop_no_bgnd))
            }
        }
        else {
            Log.w(javaClass.simpleName, "Bad activity return code ${result.resultCode}")
        }
    }

    //My Variables
    private var currentSong = 0 //songList[0]
    private var songDuration = 0

    private var totalSongsPlayed = 0
    private var loopSet = false

    val playerPaused = AtomicBoolean(false)
    val toIncTotal = AtomicBoolean(false)

    private fun initRecyclerViewDividers(rv: RecyclerView) {
        // Let's have dividers between list items
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.mainToolbar)
        binding = activityMainBinding.contentMain

        //XXX Write me. Setup adapter.
        binding.songsRV.layoutManager = LinearLayoutManager(binding.songsRV.context)
        adapter = RVDiffAdapter(::onClickSong)
        binding.songsRV.adapter = adapter
        adapter.submitList(songResources.values.toMutableList())
        initRecyclerViewDividers(binding.songsRV)

        //XXX Write me. Hook up buttons.
        binding.previousBt.setOnClickListener {
            initializrMPlayer(--currentSong)
        }

        binding.nextBt.setOnClickListener {
            initializrMPlayer(++currentSong)
        }

        binding.playPauseBt.setOnClickListener {
            if(player.isPlaying)
            {
                playerPaused.set(true)
                player.pause()
                binding.playPauseBt.apply {
                    setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
                }
            }
            else
            {
                playerPaused.set(false)
                binding.playPauseBt.apply {
                    setBackgroundResource(R.drawable.ic_pause_black_24dp)
                }
                if(toIncTotal.get())
                {
                    toIncTotal.set(false)
                    totalSongsPlayed++
                }
                player.start()
            }
        }

        binding.shuffleBt.setOnClickListener {

            var prev_currSong = songList[currentSong]

            songList.shuffle()
            var newSongResources = mutableMapOf<String, SongInfo>()

            for(i in range(0, songList.size))
            {
                var songInfo  = songResources[songList[i]]
                if (songInfo != null)
                {
                    newSongResources[songList[i]] = songInfo
                }
            }
            adapter.submitList(newSongResources.values.toMutableList())

            //Update TVs
            currentSong = songList.indexOf(prev_currSong)
            binding.nextSongNameTv.text = songList[(currentSong+1)%songList.size]
        }

        //XXX Write me. Setup seekbar, set userModifyingSeekBar
        binding.seekBar.min = 0
        binding.seekBar.setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed

                if(fromUser)
                {
                    player.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
                userModifyingSeekBar.set(true)
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                userModifyingSeekBar.set(false)
            }
        })

        //XXX Write me. Setup media player and play the first song
        //player = MediaPlayer.create(this, R.raw.cant_let_go)
        //player.start()
        player = MediaPlayer()
        initializrMPlayer(0)


        // Don't change this code.  We are launching a coroutine (user-level thread) that will
        // execute concurrently with our code, but will update the displayed time
        val millisec = 100L
        launch {
            displayTime(millisec)
        }
    }

    private fun onClickSong(view: View, songName: String) {
        initializrMPlayer(songList.indexOf(songName))
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
        cancel()
    }

    // The suspend modifier marks this as a function called from a coroutine
    // Note, this whole function is somewhat reminiscent of the Timer class
    // from Fling and Peck.  We use an independent thread to manage one small
    // piece of our GUI.  This coroutine should not modify any data accessed
    // by the main thread (it can read property values)
    private suspend fun displayTime(misc: Long) {
        // While the coroutine is running and has not been canceled by its parent
        while (coroutineContext.isActive) {
            //XXX Write me, display passed and remaining time, update seek bar if the user isn't modifying it

            binding.lapsedTimeTv.text = convertTime(player.currentPosition)
            binding.remTimeTv.text = convertTime(songDuration - player.currentPosition)

            binding.seekBar.max = songDuration
            if(!userModifyingSeekBar.get())
                binding.seekBar.progress = player.currentPosition

            // Leave this code as is.  it inserts a delay so that this thread does
            // not consume too much CPU
            delay(misc)
        }
    }

    // This method converts time in milliseconds to minutes-second formatted string
    private fun convertTime(milliseconds: Int): String {
        //XXX Write me

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds.toLong()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong())))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.player_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId

        return if (id == R.id.action_settings) {
            settingsButton(item)
            true
        } else super.onOptionsItemSelected(item)

    }

    private fun settingsButton(@Suppress("UNUSED_PARAMETER") item: MenuItem) {
        // XXX Write me

        val updateSettings = Intent(this, SettingsManager::class.java)
        var bundle = Bundle()
        bundle.putString(SettingsManager.songsPlayedKey, totalSongsPlayed.toString())
        bundle.putBoolean(SettingsManager.doLoopKey, loopSet)
        updateSettings.putExtras(bundle)
        resultLauncher.launch(updateSettings)
    }

    // XXX Write me, handle player dynamics and currently playing/next song
    private fun initializrMPlayer(songIndex : Int)
    {
        var ind = songIndex

        if(songIndex < 0)
            ind = songList.size + songIndex
        else if(songIndex < songList.size)
            ind = songIndex
        else if(songIndex >= songList.size)
            ind = songIndex % songList.size

        currentSong = ind
        player.reset()

        val afd: AssetFileDescriptor = songResources[songList[ind]]?.rawId?.let {
            this.resources.openRawResourceFd(
                it
            )
        } ?: return
        player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()

        //Update text-views
        binding.currSongNameTv.text = songList[ind]
        binding.nextSongNameTv.text = songList[(ind+1)%songList.size]

        player.setOnPreparedListener {
            songDuration = player.duration
            if(!playerPaused.get())
            {
                totalSongsPlayed++
                it.start()
            }
            else
                toIncTotal.set(true)
        }

        player.setOnCompletionListener {
            if(loopSet)
                initializrMPlayer(currentSong)
            else
                initializrMPlayer(++currentSong)
        }

        player.prepareAsync()
    }

}
