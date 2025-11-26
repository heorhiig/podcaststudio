package ie.setu

import ie.setu.utils.readNextInt
import java.io.File
import ie.setu.controllers.PodcastAPI
import ie.setu.Presistence.JSONSerializer
import ie.setu.Components.addPodcast
import ie.setu.Components.deletePodcastView
import ie.setu.Components.listPodcasts
import ie.setu.Components.updatePodcastView
import ie.setu.utils.readNextLine

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
         > |   2) Update a podcast          |
         > |   3) Delete a podcast          |
         > |   4) List podcasts             |
         > |   5) Create a playlist         |
         > |   6) Playlists                 |
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
    var option: Int
    do {
        option = mainMenu()

        when(option) {
            1 -> addPodcast()
            2 -> updatePodcastView()
            3 -> deletePodcastView()
            4 -> listPodcasts()
//            3 -> createPlaylist()
//            4 -> playlist()
            0 -> println("See you again")
            20 -> save()
            21 -> load()
            else -> print("Invalide option: $option")
        }
    } while (option != 0)
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