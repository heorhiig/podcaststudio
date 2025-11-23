package ie.setu.Models

class Playlist(
    var playlistId : Int,
    var name: String,
    var description: String,
    var podcasts: MutableList<Podcast>
)