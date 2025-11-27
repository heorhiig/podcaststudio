package ie.setu.Components

import ie.setu.Models.Playlist
import ie.setu.Presistence.JSONSerializer
import ie.setu.controllers.PlaylistAPI
import ie.setu.utils.*
import java.io.File

var playlistAPI = PlaylistAPI(JSONSerializer(File("playlist.json")))

fun addPlaylist() {
    val name = readNextLine(formatView("Enter the name for your Playlist").toString())
    val description = readNextLine(formatView("Enter the description to your playlist").toString())
    val favInput = readNextInt(
        formatView("Is this playlist favorite:\n1 - No\n2 - Yes").toString()
    )

    val favorite = when (favInput) {
        2 -> true
        else -> false
    }

    val playlist = playlistAPI.addPlaylist(Playlist(name, description, favorite))

    val playlistIndex = playlistAPI.numberOfPlaylists() - 1


    var adding = true

    while (adding) {
        println(podcastAPI.listAllPodcasts())

        val input = readNextInt(formatView("Enter podcast index to add (-1 to stop):").toString())

        if (input == -1) {
            adding = false
        } else {
            val success = playlistAPI.addPodcastToPlaylist(playlistIndex, input, podcastAPI)

            if (success) {
                println("Podcast added")
            } else {
                println("Invalid index")
            }
        }
    }

    println("Playlist created successfully")
}


fun listPlaylists() {
    if (playlistAPI.numberOfPlaylists() > 0) {
        do {
            println(
                """
                ^^^^^^^^^^^^^^^^^^^^^^^^^^
                1) List All playlists
                2) List Favorite playlists
                3) Open Playlist 
                4) Update Playlist
                5) Delete playlist
                0) Back
                ________________________
                """.trimIndent()
            )

            val option = readNextInt(">> ")

            when (option) {
                1 -> println(playlistAPI.listAllPlaylists())
                2 -> println(playlistAPI.listFavoritePlaylist())
                3 -> openPlaylist()
                4 ->updatePlaylist()
                5 ->deletePlaylist()
                0 -> println()
                else -> println("Invalid option")
            }
        } while (option != 0)
    } else {
        println("No podcasts available")
    }
}

fun updatePlaylist() {
    println(playlistAPI.listAllPlaylists())

    var indexToUpdate = readNextInt(formatView("Enter index of playlist to update").toString())
    var currentPlaylist = playlistAPI.findPlaylist(indexToUpdate)

    if (currentPlaylist != null) {
        var newName = readNextLine(formatView("Eneter new name").toString())
        var newDescription = readNextLine(formatView("Enter new description").toString())
        var favInput = readNextInt(
            formatView("Is this playlist favorite:\n1 - No\n2 - Yes").toString()
        )

        var favorite = when (favInput) {
            2 -> true
            else -> false
        }

        var updatedPlaylist = playlistAPI.updatePlaylist(indexToUpdate, Playlist(newName, newDescription, favorite,currentPlaylist.podcasts))


        if (updatedPlaylist == true) {
            println("Playlist updated")
        } else println("Updated failed")

    } else println("Invalid index")
}

fun deletePlaylist() {
    println(playlistAPI.listAllPlaylists())


    if (playlistAPI.numberOfPlaylists() != 0) {
        var indexToDelete = readNextInt(formatView("Enter index of playlist to delete").toString())

        var deletedPlaylist = playlistAPI.deletePlaylist(indexToDelete)

        if (deletedPlaylist != null) {
            println("Deleted Successful")
        } else println("Deleting failed")

    }
}

fun openPlaylist() {
    println(playlistAPI.listAllPlaylists())

    val playlistIndex = readNextInt(
        formatView("Enter the index of the playlist you want to open (-1 to go back):").toString()
    )

    val playlist = playlistAPI.findPlaylist(playlistIndex)

    if (playlist == null) {
        println("Invalid playlist index")
        return
    }
    println(formatViewListPlaylist(playlist))

    if (playlist.podcasts.isEmpty()) {
        println("No podcasts in this playlist.")
        return
    } else {
        playlistAPI.listPodcastInPl(playlistIndex)
    }

    while (true) {
        println(
            """
            ^^^^^^^^^^^^^^^^^^^^^^^^^^
                1) Listen to a podcast
                2) Add podcast
                3) Delete podcast 
                -1) Back
                ________________________
            """.trimIndent()
        )

        val choice = readNextInt("Enter the podcast index to listen it: ")

        when (choice) {
            1 -> listenPodcast(playlist)
            2 -> addPodcastToplaylist(playlistIndex)
            3 -> deletePodcastFromPlaylist(playlistIndex)
            -1 -> return

            else -> println("Invalid option")
        }
    }
}

fun addPodcastToplaylist(playlistIndex: Int) {
    println(podcastAPI.listAllPodcasts())
    var podcastIndex = readNextInt(formatView("Enter index of podcast to add").toString())


    var add = playlistAPI.addPodcastToPlaylist(playlistIndex, podcastIndex, podcastAPI)

    if (add) {
        println(formatView("Podcast add"))
    } else println(formatView("Adding failed"))
}

fun deletePodcastFromPlaylist(playlistIndex: Int) {
    println(podcastAPI.listAllPodcasts())
    var podcastIndex = readNextInt(formatView("Enter index of podcast to add").toString())

    var delete = playlistAPI.deletePodcastFromPlaylist(playlistIndex, podcastIndex)

    if (delete != null) {
        println("Podcast deleted")
    } else println("Deleting failed")

}

fun listenPodcast(playlist: Playlist) {
    println(podcastAPI.listAllPodcasts())
    var podcastIndex = readNextInt(formatView("Enter index of podcast to listen").toString())

    if (isValidListIndex(podcastIndex, playlist.podcasts)) {
        var selectedPodcast = playlist.podcasts[podcastIndex]
        var path = selectedPodcast.file

        if (isValidFilePath(path)) {
            listenAudio(path)
        } else println("Path is wrong")
    } else println("Invalid index")
}
