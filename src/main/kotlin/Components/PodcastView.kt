package ie.setu.Components

import ie.setu.Models.Podcast
import ie.setu.Presistence.JSONSerializer
import ie.setu.controllers.PodcastAPI
import ie.setu.utils.isValideCategory
import ie.setu.utils.readNextInt
import ie.setu.utils.readNextLine
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

val podcastAPI = PodcastAPI(JSONSerializer(File("podcast.json")))



fun addPodcast() {
    var title = readNextLine(formatView("Add title for Podcast:").toString())
    var description = readNextLine(formatView("Add description for Podcast $title:").toString())
    var category = readNextLine(formatView( "Add category for Podcast $title:" +
            "\n Use only this category Technology, Music, Art, Education, News, Sport" ).toString())
    var author = readNextLine(formatView("Add name or names of author for Podcast $title:").toString())
    var date = readNextLine(formatView("Add the release date:").toString() )
    var path = readNextLine(
        formatView("Enter path to your audio with podcast:" +
                "\n Enter here path to your audio file correct:").toString()
    )

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
    val category = readNextLine("Enter one of this category \n Technology, Music, Art, Education, News, Sport:\n" +
            "")

    println(podcastAPI.filterByCategory(isValideCategory(category).toString()))
}

fun updatePodcastView() {
    podcastAPI.listAllPodcasts()

    if (podcastAPI.numberOfPodcasts() > 0) {
        val indexToUpdate = readNextInt(formatView("Enter the index of Podcast what you want to update").toString())
        if (podcastAPI.validIndex(indexToUpdate)) {
            var title = readNextLine(formatView("Add title for Podcast:").toString())
            var description = readNextLine(formatView("Add description for Podcast $title:").toString())
            var category = readNextLine(formatView( "Add category for Podcast $title:" +
                    "\n Use only this category Technology, Music, Art, Education, News, Sport" ).toString())
            var author = readNextLine(formatView("Add name or names of author for Podcast $title:").toString())
            var date = readNextLine(formatView("Add the release date:").toString() )
            var path = readNextLine(
                formatView("Enter path to your audio with podcast:" +
                        "\n Enter here path to your audio file correct:").toString()
            )

            podcastAPI.updatePodcast(indexToUpdate, Podcast(title, description, category, author, false, date, path))
        }
    }
}

fun deletePodcastView() {
    podcastAPI.listAllPodcasts()

    if (podcastAPI.numberOfPodcasts() > 0) {
        val indexToDelete = readNextInt(formatView("Enter the index of Podcast what you want to delete:").toString())

        val deletedPodcast = podcastAPI.deletePodcast(indexToDelete)

        if (deletedPodcast != null) {
            println(formatView("Delete Successful Done!"))
        } else println(formatView("Error in deleting"))
    }
}


fun formatView(text: String) {
    val line = "-".repeat(text.length)
    val upArrow = "^".repeat(text.length)

    println(upArrow)
    println(text)
    println(line)

}
