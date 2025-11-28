package ie.setu.utils

import ie.setu.Models.Playlist
import ie.setu.Models.Podcast


//Create form view for podcast when they list
fun formatViewList(podcast: Podcast): String {
    return """
        ${podcast.podcastTitle}      ${if (!podcast.favorite) "★" else "☆"}
        ${podcast.podcastCategory}      ${podcast.date}
        ${podcast.authorName}
        ${podcast.podcastDescription}
        ------------------------------------------------
    """.trimIndent()
}

//Create form view fro playlist
fun formatViewListPlaylist(playlist: Playlist): String {
    return """
        ${playlist.name}          ${if (playlist.favorite) "★" else "☆"}
        ${playlist.description}
    """.trimIndent()
}

//Create regular format for strings when user when add something
fun formatView(text: String) {
    val line = "-".repeat(text.length)
    val upArrow = "^".repeat(text.length)

    println(upArrow)
    println(text)
    println(line)

}
