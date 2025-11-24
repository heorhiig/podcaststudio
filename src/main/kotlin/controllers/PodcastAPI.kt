package ie.setu.controllers

import ie.setu.Models.Podcast
import ie.setu.Presistence.Serializer

class PodcastAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var podcasts  = ArrayList<Podcast>()

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
        else formatListString(podcasts.filter { podcast -> podcast.podcastCategory.equals(category, ignoreCase = true) })

    fun filterByAuthor(): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts.sortedBy { podcast -> podcast.authorName.lowercase() })

    fun filterByDate(): String =
        if (podcasts.isEmpty()) "No podcasts stored"
        else formatListString(podcasts.sortedBy { podcast -> podcast.date.lowercase() })


    fun numberOfPodcasts() = podcasts.size
    fun numberOfFavorite() = podcasts.count {podcast: Podcast -> podcast.favorite}

    @Throws(Exception::class)
    fun load() {
        podcasts = serializer.read() as ArrayList<Podcast>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(podcasts)
    }

    fun formatListString(podcastToFormat: List<Podcast>) : String =
        podcastToFormat
            .joinToString(separator = "\n") {podcast ->
                podcasts.indexOf(podcast).toString() + ": " + podcast.toString()}

}