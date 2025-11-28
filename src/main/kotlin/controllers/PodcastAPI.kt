package ie.setu.controllers

import ie.setu.Models.Podcast
import ie.setu.Presistence.Serializer
import ie.setu.utils.formatViewList
import ie.setu.utils.isValidListIndex

class PodcastAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var podcasts = ArrayList<Podcast>()

    //Adds a new podcast to the collection
    fun add(podcast: Podcast): Boolean {
        println("ADDING PODCAST: $podcast")
        return podcasts.add(podcast)
    }

    //List all podcast if empty return mesege
    fun listAllPodcasts(): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts)

    //List only favorite podcast
    fun listFavoritePodcast(): String =
        if (numberOfFavorite() == 0) "No favorite podcasts"
        else formatListString(podcasts.filter { podcast -> podcast.favorite })

    //Filter podcast by category, ignore the case
    fun filterByCategory(category: String?): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts.filter { podcast ->
            podcast.podcastCategory.equals(
                category,
                ignoreCase = true
            )
        })

    //Filter podcast by author, ignore the case
    fun filterByAuthor(): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts.sortedBy { podcast -> podcast.authorName.lowercase() })

    //Filter podcast by date
    fun filterByDate(): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts.sortedBy { podcast -> podcast.date.lowercase() })


    //Delete podcast from list by index
    fun deletePodcast(intexToDelete: Int): Podcast? {
        return if (isValidListIndex(intexToDelete, podcasts)) {
            podcasts.removeAt(intexToDelete)
        } else null
    }

    //Update podcast by adding new value to the attributes
    fun updatePodcast(indexToUpdate: Int, podcast: Podcast?): Boolean? {
        val fPodcast = findPodcast(indexToUpdate)

        if (fPodcast != null && podcast != null) {
            fPodcast.podcastTitle = podcast.podcastTitle
            fPodcast.podcastDescription = podcast.podcastDescription
            fPodcast.podcastCategory = podcast.podcastCategory
            fPodcast.date = podcast.date
            fPodcast.authorName = podcast.authorName
            fPodcast.favorite = podcast.favorite
            fPodcast.file = podcast.file
            return null

        }
        return false
    }

    //Check for valid index in list
    fun validIndex(index: Int): Boolean {
        return isValidListIndex(index, podcasts)
    }

    // Search podcast in the podcast ArrayList
    fun findPodcast(index: Int): Podcast? {
        return if (isValidListIndex(index, podcasts)) {
            podcasts[index]
        } else null
    }

    //Count of all podcast or only favorite
    fun numberOfPodcasts() = podcasts.size
    fun numberOfFavorite() = podcasts.count { podcast: Podcast -> podcast.favorite }


    //Function for load data from json file
    @Throws(Exception::class)
    fun load() {
        podcasts = serializer.read() as ArrayList<Podcast>
    }


    //Function for saving data in json file
    @Throws(Exception::class)
    fun store() {
        serializer.write(podcasts)
    }

    //Make list format better for user
    fun formatListString(podcastToFormat: List<Podcast>): String {
        return podcastToFormat
            .joinToString(separator = "\n") { podcast ->
                val index = podcastToFormat.indexOf(podcast)
                "$index: " +
                        "\n${formatViewList(podcast)}"
            }
    }
}