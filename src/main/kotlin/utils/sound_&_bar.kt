package ie.setu.utils

import javazoom.jl.player.Player
import org.jaudiotagger.audio.AudioFileIO
import java.io.File
import java.io.FileInputStream
import kotlin.concurrent.thread

class AudioPlayerWithProgress {
    private var player: Player? = null
    private var isPlaying = false
    private var currentPosition = 0L
    private var totalDuration = 0L

    fun loadAudio(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            println("File no exists: $filePath")
            return
        }

        try {

            val audioFile = AudioFileIO.read(file)
            val audioHeader = audioFile.audioHeader
            totalDuration = audioHeader.trackLength.toLong()

            println("Загружен трек: ${file.name}")
            println("Длительность: ${formatTime(totalDuration)}")

        } catch (e: Exception) {
            println("Ошибка загрузки файла: ${e.message}")
        }
    }

    fun play(filePath: String) {
        thread {
            try {
                val input = FileInputStream(filePath)
                player = Player(input)
                isPlaying = true

                startProgressTracker()

                println("Воспроизведение...")
                player!!.play()

                isPlaying = false
                println("\nВоспроизведение завершено")

            } catch (e: Exception) {
                println("Ошибка воспроизведения: ${e.message}")
                isPlaying = false
            }
        }
    }

    fun stop() {
        player?.close()
        isPlaying = false
    }

    private fun startProgressTracker() {
        thread {
            val startTime = System.currentTimeMillis()

            while (isPlaying && currentPosition < totalDuration) {
                currentPosition = ((System.currentTimeMillis() - startTime) / 1000).coerceAtMost(totalDuration)


                print("\r${createProgressBar(currentPosition, totalDuration)}")

                Thread.sleep(500) // Обновляем каждые 500 мс
            }
        }
    }

    private fun createProgressBar(current: Long, total: Long): String {
        val width = 30
        val progress = if (total > 0) (current * width / total).toInt() else 0
        val remaining = width - progress

        val bar = "[" + "=".repeat(progress) + ">" + " ".repeat(remaining) + "]"
        return "$bar ${formatTime(current)} / ${formatTime(total)}"
    }

    private fun formatTime(seconds: Long): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}

fun main() {
    val player = AudioPlayerWithProgress()
    val filePath = "src/song/505.mp3"

    player.loadAudio(filePath)
    player.play(filePath)


    Thread.sleep(1000)
    while (true) {
        Thread.sleep(100)
    }
}