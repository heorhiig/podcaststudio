package ie.setu.controllers

import ie.setu.Models.Playlist
import ie.setu.Presistence.Serializer
import ie.setu.utils.formatView
import ie.setu.utils.formatViewList
import ie.setu.utils.formatViewListPlaylist
import ie.setu.utils.isValidListIndex

class PlaylistAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var playlists = ArrayList<Playlist>()

    // Add new playlist to ArrayList<Playlist>() by using method add()
    fun addPlaylist(playlist: Playlist): Boolean {
        return playlists.add(playlist)
    }

    //List all playlist in the ArrayList
    fun listAllPlaylists(): String =
        if (playlists.isEmpty()) "No playlist stored"
        else formatListString(playlists)

    //List all favorite playlist in the ArrayList
    fun listFavoritePlaylist(): String =
        if (numberOfFavorite() == 0) "No favorite podcasts"
        else formatListString(playlists.filter { playlist -> playlist.favorite })

    //Delete playlist from ArrayList
    fun deletePlaylist(index: Int): Playlist? {
        return if (isValidListIndex(index, playlists)) {
            playlists.removeAt(index)
        } else null
    }

    //Update playlist by adding new value for attributes
    fun updatePlaylist(index: Int, playlist: Playlist): Boolean {
        val fPlaylist = findPlaylist(index)

        if (fPlaylist != null && playlist != null) {
            fPlaylist.name = playlist.name
            fPlaylist.description = playlist.description
            fPlaylist.favorite = playlist.favorite
            fPlaylist.podcasts = playlist.podcasts
            return true
        }

        return false
    }

    //Find playlist by index in ArrayList
    fun findPlaylist(index: Int): Playlist? {
        return if (isValidListIndex(index, playlists)) {
            playlists[index]
        } else null
    }

    //Adding podcast to play list by getting podcast index and searching in podcast list
    fun addPodcastToPlaylist(playlistIndex: Int, podcastIndex: Int, podcastAPI: PodcastAPI): Boolean {
        val playlist = findPlaylist(playlistIndex)
        val podcastToAdd = podcastAPI.findPodcast(podcastIndex)

        if (playlist != null && podcastToAdd != null) {
            playlist.podcasts.add(podcastToAdd)
            return true
        }

        return false
    }

    //Deleting podcast from playlist by index, deleting going only in playlist
    fun deletePodcastFromPlaylist(playlistIndex: Int, podcastIndex: Int) {
        val playlist = findPlaylist(playlistIndex)
        val podcast = playlist?.let { isValidListIndex(podcastIndex, it.podcasts) }

        if (playlist != null && podcast != false) {
            playlist.podcasts.removeAt(podcastIndex)
        } else null
    }

    //Shows all podcast in playlist
    fun listPodcastInPl(playlistIndex: Int) {
        val playList = findPlaylist(playlistIndex)

        playList?.podcasts?.joinToString(separator = "\n") { podcast ->
            val index = playList.podcasts.indexOf(podcast)
            "$index: " +
                    "\n${formatViewList(podcast)}"
        }
    }


    //Make format for good view
    fun formatListString(platlistToFormat: List<Playlist>): String {
        return platlistToFormat
            .joinToString(separator = "\n") { playlist ->
                val index = platlistToFormat.indexOf(playlist)
                "$index: " +
                        "\n${formatViewListPlaylist(playlist)}"
            }
    }

    //Shows count of all playlists or only favorite
    fun numberOfPlaylists() = playlists.size
    fun numberOfFavorite() = playlists.count { playlist: Playlist -> playlist.favorite }


    //Function for load data from json file
    @Throws(Exception::class)
    fun load() {
        playlists = serializer.read() as ArrayList<Playlist>
    }

    //Function for saving data in json file
    @Throws(Exception::class)
    fun store() {
        serializer.write(playlists)
    }

}

