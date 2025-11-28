package ie.setu.controllers

import ie.setu.Models.Podcast
import ie.setu.Presistence.Serializer
import ie.setu.utils.formatViewList
import ie.setu.utils.isValidListIndex

class PodcastAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var podcasts = ArrayList<Podcast>()

    fun add(podcast: Podcast): Boolean {
        println("ADDING PODCAST: $podcast")
        return podcasts.add(podcast)
    }

    fun listAllPodcasts(): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts)

    fun listFavoritePodcast(): String =
        if (numberOfFavorite() == 0) "No favorite podcasts"
        else formatListString(podcasts.filter { podcast -> podcast.favorite })

    fun filterByCategory(category: String?): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts.filter { podcast ->
            podcast.podcastCategory.equals(
                category,
                ignoreCase = true
            )
        })

    fun filterByAuthor(): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts.sortedBy { podcast -> podcast.authorName.lowercase() })

    fun filterByDate(): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts.sortedBy { podcast -> podcast.date.lowercase() })


    fun deletePodcast(intexToDelete: Int): Podcast? {
        return if (isValidListIndex(intexToDelete, podcasts)) {
            podcasts.removeAt(intexToDelete)
        } else null
    }

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

    fun validIndex(index: Int): Boolean {
        return isValidListIndex(index, podcasts)
    }

    fun findPodcast(index: Int): Podcast? {
        return if (isValidListIndex(index, podcasts)) {
            podcasts[index]
        } else null
    }

    fun numberOfPodcasts() = podcasts.size
    fun numberOfFavorite() = podcasts.count { podcast: Podcast -> podcast.favorite }

    @Throws(Exception::class)
    fun load() {
        podcasts = serializer.read() as ArrayList<Podcast>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(podcasts)
    }

    fun formatListString(podcastToFormat: List<Podcast>): String {
        return podcastToFormat
            .joinToString(separator = "\n") { podcast ->
                val index = podcastToFormat.indexOf(podcast)
                "$index: " +
                        "\n${formatViewList(podcast)}"
            }
    }
}