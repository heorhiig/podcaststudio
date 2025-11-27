package ie.setu.Models

class Playlist(
    var name: String,
    var description: String,
    var favorite: Boolean,
    var podcasts: MutableList<Podcast> = mutableListOf()
)