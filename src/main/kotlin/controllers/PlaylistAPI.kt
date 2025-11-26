package ie.setu.controllers

import ie.setu.Models.Playlist

class PlaylistAPI {
    private var playlists = ArrayList<Playlist>()

    fun createPlaylist(playlist: Playlist): Boolean {
        return playlists.add(playlist)
    }
}