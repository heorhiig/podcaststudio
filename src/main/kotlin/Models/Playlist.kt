package ie.setu.Models

class Playlist(
    var name: String,
    var description: String,
    var favorite: String,
    var podcasts: MutableList<Podcast>
)