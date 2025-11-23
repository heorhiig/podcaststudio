package ie.setu.controllers

import ie.setu.Models.Podcast

class PodcastAPI {
    private var podcasts  = ArrayList<Podcast>()

    fun add(podcast: Podcast): Boolean {
        return podcasts.add(podcast)
    }
}