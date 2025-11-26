package ie.setu.Models

import java.util.Date

data class Podcast(
    var podcastTitle: String,
    var podcastDescription: String,
    var podcastCategory: String,
    var authorName : String,
    var favorite : Boolean,
    var date : String,
    var file : String,
)