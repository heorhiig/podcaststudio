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

    fun addPlaylist(playlist: Playlist): Boolean {
        return playlists.add(playlist)
    }

    fun listAllPlaylists(): String =
        if (playlists.isEmpty()) "No playlist stored"
        else formatListString(playlists)

    fun listFavoritePlaylist(): String =
        if (numberOfFavorite() == 0) "No favorite podcasts"
        else formatListString(playlists.filter { playlist -> playlist.favorite })

    fun deletePlaylist(index: Int): Playlist? {
        return if (isValidListIndex(index, playlists)) {
            playlists.removeAt(index)
        } else null
    }

    fun updatePlaylist(index: Int, playlist: Playlist): Boolean? {
        val fPlaylist = findPlaylist(index)

        if (fPlaylist != null && playlist != null) {
            fPlaylist.name = playlist.name
            fPlaylist.description = playlist.description
            fPlaylist.favorite = playlist.favorite
            fPlaylist.podcasts = playlist.podcasts
            return null
        }

        return false
    }

    fun findPlaylist(index: Int): Playlist? {
        return if (isValidListIndex(index, playlists)) {
            playlists[index]
        } else null
    }

    fun addPodcastToPlaylist(playlistIndex: Int, podcastIndex: Int, podcastAPI: PodcastAPI): Boolean {
        val playlist = findPlaylist(playlistIndex)
        val podcastToAdd = podcastAPI.findPodcast(podcastIndex)

        if (playlist != null && podcastToAdd != null) {
            playlist.podcasts.add(podcastToAdd)
            return true
        }

        return false
    }

    fun deletePodcastFromPlaylist(playlistIndex: Int, podcastIndex: Int) {
        val playlist = findPlaylist(playlistIndex)
        val podcast = playlist?.let { isValidListIndex(podcastIndex, it.podcasts) }

        if (playlist != null && podcast != null) {
            playlist.podcasts.removeAt(podcastIndex)
        } else null
    }

    fun listPodcastInPl(playlistIndex: Int) {
        val playList = findPlaylist(playlistIndex)

        if (playList != null) {
            if (playList == null && playList.podcasts.isEmpty()) {
                return formatView("No podcast in this playlist")
            } else {
                playList.podcasts
                    .joinToString(separator = "\n") { podcast ->
                        val index = playList.podcasts.indexOf(podcast)
                        "$index: " +
                                "\n${formatViewList(podcast)}"
                    }
            }
        }
    }


    fun formatListString(platlistToFormat: List<Playlist>): String {
        return platlistToFormat
            .joinToString(separator = "\n") { playlist ->
                val index = platlistToFormat.indexOf(playlist)
                "$index: " +
                        "\n${formatViewListPlaylist(playlist)}"
            }
    }

    fun numberOfPlaylists() = playlists.size
    fun numberOfFavorite() = playlists.count { playlist: Playlist -> playlist.favorite }


    @Throws(Exception::class)
    fun load() {
        playlists = serializer.read() as ArrayList<Playlist>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(playlists)
    }

}

