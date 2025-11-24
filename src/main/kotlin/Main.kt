package ie.setu

import ie.setu.utils.readNextInt
import java.io.File
import ie.setu.controllers.PodcastAPI
import ie.setu.Presistence.JSONSerializer
import ie.setu.Components.addPodcast
import ie.setu.Components.listPodcasts

val podcastAPI = PodcastAPI(JSONSerializer(File("podcast.json")))

fun main() {
    runMenu()
}

fun mainMenu(): Int {
    print(
        """ 
         > ----------------------------------
         > |        Podcast Studio          |
         > ----------------------------------
         > | MENU                           |
         > |   1) Add a podcast             | 
         > |   2) List podcasts             |
         > |   3) Create a playlist         |
         > |   4) Go to the playlist        |
         > |   5) list all podcasts         |
         > ----------------------------------
         > |   20) Save podcast & playlists |
         > |   21) Load podcast & playlists |
         > ----------------------------------
         > |   0) Exit                      |
         > ---------------------------------- 
         >""".trimMargin(">"))
    return readNextInt(" > ==>>")
}

fun runMenu() {
    do {
        when(val option = mainMenu()) {
            1 -> addPodcast()
            2 -> listPodcasts()
//            3 -> createPlaylist()
//            4 -> playlist()
//            5 -> listPodcasts()
            20 -> save()
            21 -> load()
            else -> print("Invalide option: $option")
        }
    } while (true)
}



fun save() {
    try {
        podcastAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        podcastAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file:: $e")
    }
}