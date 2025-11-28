package ie.setu.Components

import ie.setu.Models.Playlist
import ie.setu.Models.Podcast
import ie.setu.Presistence.JSONSerializer
import ie.setu.controllers.PodcastAPI
import ie.setu.utils.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


val podcastAPI = PodcastAPI(JSONSerializer(File("podcast.json")))

fun addPodcast() {
    //Read podcast details from user
    var title = readNextLine(formatView("Add title for Podcast:").toString())
    var description = readNextLine(formatView("Add description for Podcast $title:").toString())
    var category = readValidCategory(
        formatView(
            "Add category for Podcast $title:\n Use only this category Technology, Music, Art, Education, News, Sport"
        ).toString()
    )
    var author = readNextLine(formatView("Add name or names of author for Podcast $title:").toString())
    var date = readNextLine(formatView("Add the release date:").toString())
    var path = readNextLine(
        formatView("Enter path to your audio with podcast:\n Enter here path to your audio file correct:").toString()
    )

    //Ask if the podcast is a favorite
    val favInput = readNextInt(formatView("Is this podcast is favorite: \n1 - No \n2 - Yes").toString())
    val favorite = when (favInput) {
        2 -> true
        else -> false
    }

    //Add podcast to API and print success/failure message
    var isAdd = podcastAPI.add(Podcast(title, description, category, author, favorite, date, path))
    if (isAdd) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

//Function to list podcasts and provide user menu options
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
                6) Listen podcast
                0) Back
                ________________________
                """.trimIndent()
            )

            val option = readNextInt(">> ")

            when (option) {
                1 -> println(podcastAPI.listAllPodcasts()) //Show all podcasts
                2 -> println(podcastAPI.listFavoritePodcast()) //Show favorites
                3 -> listByCategory() //Filter by category
                4 -> println(podcastAPI.filterByAuthor()) //Filter by author
                5 -> println(podcastAPI.filterByDate()) //Filter by date
                6 -> listenPodcast() //Play selected podcast
                0 -> println() //Back to previous menu
                else -> println("Invalid option")
            }
        } while (option != 0)
    } else {
        println("No podcasts available")
    }
}

//Function to filter podcasts by category
fun listByCategory() {
    val category = readNextLine("Enter one of this category \n Technology, Music, Art, Education, News, Sport:\n")
    println(podcastAPI.filterByCategory(isValideCategory(category).toString()))
}

//Function to update a podcast

fun updatePodcastView() {
    podcastAPI.listAllPodcasts()

    if (podcastAPI.numberOfPodcasts() > 0) {
        val indexToUpdate = readNextInt(formatView("Enter the index of Podcast what you want to update").toString())
        if (podcastAPI.validIndex(indexToUpdate)) {
            var title = readNextLine(formatView("Add title for Podcast:").toString())
            var description = readNextLine(formatView("Add description for Podcast $title:").toString())
            var category = readNextLine(
                formatView(
                    "Add category for Podcast $title:\n Use only this category Technology, Music, Art, Education, News, Sport"
                ).toString()
            )
            var author = readNextLine(formatView("Add name or names of author for Podcast $title:").toString())
            var date = readNextLine(formatView("Add the release date:").toString())
            var path = readNextLine(
                formatView("Enter path to your audio with podcast:\n Enter here path to your audio file correct:").toString()
            )

            //Update podcast at given index
            podcastAPI.updatePodcast(indexToUpdate, Podcast(title, description, category, author, false, date, path))
        }
    }
}

//Function to delete a podcast
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

//Function to listen to a podcast
fun listenPodcast() {
    println(podcastAPI.listAllPodcasts())

    if (podcastAPI.numberOfPodcasts() == 0) {
        println("No podcast here")
        return
    }

    val podcastIndex = readNextInt(formatView("Enter index of podcast to listen (-1 to go back):").toString())
    if (podcastIndex == -1) return

    val selectedPodcst = podcastAPI.findPodcast(podcastIndex)

    if (selectedPodcst != null) {
        if (isValidFilePath(selectedPodcst.file)) {
            listenAudio(selectedPodcst.file) //Play audio file
        } else println("Path is wrong")
    }
}
