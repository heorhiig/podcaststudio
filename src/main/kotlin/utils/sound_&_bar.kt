package ie.setu.utils

import javazoom.jl.player.Player
import org.jaudiotagger.audio.AudioFileIO
import java.io.File
import java.io.FileInputStream
import kotlin.concurrent.thread

// This class handles audio playback and displays a progress bar in the console
class AudioPlayerWithProgress {
    private var player: Player? = null
    private var isPlaying = false
    private var currentPosition = 0L
    private var totalDuration = 0L

    //This function load file by path what user enter
    fun loadAudio(filePath: String) {
        val file = File(filePath)
        //Took path check for working
        if (!file.exists()) {
            println("File no exists: $filePath")
            return
        }

        //If path works we do try statement
        try {

            //took the file and use the method from library jaudiotagger and read it
            val audioFile = AudioFileIO.read(file)
            val audioHeader = audioFile.audioHeader
            //upload the file
            totalDuration = audioHeader.trackLength.toLong()

            println("The track has been uploaded: ${file.name}")
            println("Duration: ${formatTime(totalDuration)}")

        } catch (e: Exception) { //if uploading went wrong catch the error
            println("File upload error: ${e.message}")
        }
    }

    //Function play used to make the sound and shows the progressbar
    fun play(filePath: String) {
        //run the async function to doing this statement not synchronous with other code
        thread {
            try {
                //Reading the bits from filePath to use it in Player
                val input = FileInputStream(filePath)
                player = Player(input)
                isPlaying = true

                //Run progressbar for audio
                startProgressTracker()

                println("Playback...")
                player!!.play()

                //Throw the message when audio finish
                isPlaying = false
                println("\nPlayback complete")

            } catch (e: Exception) { //Catch the error if playback went wrong
                println("Playback error: ${e.message}")
                isPlaying = false
            }
        }
    }

    //Stop player by using close() method
    fun stop() {
        player?.close()

        //Change the isPlaying to false
        isPlaying = false
    }

    //Track the progress of audio
    private fun startProgressTracker() {
        thread { //Run as async function
            val startTime = System.currentTimeMillis()

            while (isPlaying && currentPosition < totalDuration) {
                // Update current position in seconds
                currentPosition = ((System.currentTimeMillis() - startTime) / 1000).coerceAtMost(totalDuration)

                // Print the progress bar in the same console line
                print("\r${createProgressBar(currentPosition, totalDuration)}")

                Thread.sleep(500)
            }
        }
    }

    //Create the progressbar
    private fun createProgressBar(current: Long, total: Long): String {
        val width = 30 //Total width of the bar
        val progress = if (total > 0) (current * width / total).toInt() else 0 //How many = will be added to the bar
        val remaining = width - progress

        val bar = "[" + "=".repeat(progress) + ">" + " ".repeat(remaining) + "]"
        return "$bar ${formatTime(current)} / ${formatTime(total)}" //Shows the seconds and minutes by taking current position and total size
    }

    //Division by 60 to track seconds and minutes
    private fun formatTime(seconds: Long): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}

//Run the audio with sound and progressbar
fun listenAudio(path: String) {
    val player = AudioPlayerWithProgress()

    player.loadAudio(path)
    player.play(path)

    println("Press ENTER to stop playback…")
    readln()
    player.stop()

    Thread.sleep(1000)
    while (true) {
        Thread.sleep(100)
    }
}