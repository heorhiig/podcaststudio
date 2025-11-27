package ie.setu.utils

import ie.setu.Models.Playlist
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


fun formatViewListPlaylist(playlist: Playlist): String {
    return """
        ${playlist.name}          ${if (playlist.favorite) "★" else "☆"}
        ${playlist.description}
    """.trimIndent()
}

fun formatView(text: String) {
    val line = "-".repeat(text.length)
    val upArrow = "^".repeat(text.length)

    println(upArrow)
    println(text)
    println(line)

}
