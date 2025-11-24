package ie.setu.Components

import ie.setu.Models.Podcast
import ie.setu.Presistence.JSONSerializer
import ie.setu.controllers.PodcastAPI
import ie.setu.podcastAPI
import ie.setu.utils.isValideCategory
import ie.setu.utils.readNextInt
import ie.setu.utils.readNextLine
import java.io.File
import java.nio.file.Paths

val podcastAPI = PodcastAPI(JSONSerializer(File("podcast.json")))

fun addPodcast() {
    var title = readNextLine("Add title for Podcast:")
    var description = readNextLine("Add description for Podcast $title:")
    var category = readNextLine("Add category for Podcast $title:")
    var author = readNextLine("Add name or names of author for Podcast $title:")
    var date = readNextLine("Add the release date:")
    var path = readNextLine("Enter path to your audio with podcast:")

//    var pathFile = File(path)
//
//    if (!pathFile.exists()) {
//        return println("File not exists")
//    }
//
//    var audioDir = File("src/audio")
//
//    var fileName = audioDir.name
//
//
//    var targetPath = Paths.get("src/audio", fileName)

    var isAdd = podcastAPI.add(Podcast(title, description, category, author, false, date, path))

    if (isAdd) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }

}

fun listPodcasts() {
    if (podcastAPI.numberOfPodcasts() > 0) {
        do {
            println(
                """
                ^^^^^^^^^^^^^^^^^^^^^^^^^^
                1) List All podcasts
                2) List Favorite podcasts
                3) Filter by category
                4) Filter by author
                5) Filter by date
                0) Back
                ________________________
                """.trimIndent()
            )

            val option = readNextInt(">> ")

                when (option) {
                    1 -> println(podcastAPI.listAllPodcasts())
                    2 -> println(podcastAPI.listFavoritePodcast())
                    3 -> listByCategory()
                    4 -> println(podcastAPI.filterByAuthor())
                    5 -> println(podcastAPI.filterByDate())
                    0 -> println()
                    else -> println("Invalid option")
                }
        } while (option != 0)
    } else {
        println("No podcasts available")
    }
}


fun listByCategory() {
    val category = readNextLine("Enter one of this category \n Technology, Music, Art, Education, News, Sport2" +
            "")

    println(podcastAPI.filterByCategory(isValideCategory(category).toString()))
}



