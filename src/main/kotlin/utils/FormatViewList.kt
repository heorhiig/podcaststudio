package ie.setu.utils

import ie.setu.Models.Podcast

fun formatViewList(podcast: Podcast): String {
    return """
        ${podcast.podcastTitle}      ${if (!podcast.favorite) "★" else "☆"}
        ${podcast.podcastCategory}      ${podcast.date}
        ${podcast.authorName}
        ${podcast.podcastDescription}
        ------------------------------------------------
    """.trimIndent()
}




