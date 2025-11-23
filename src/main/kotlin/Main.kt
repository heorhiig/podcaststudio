package ie.setu

fun main() {

}

fun mainMenu() {
    print(
        """ 
         > ----------------------------------
         > |        Podcast Studio          |
         > ----------------------------------
         > | MENU                           |
         > |   1) Add a podcast             | 
         > |   2) Search for podcasts       |
         > |   3) Create a playlist         |
         > |   4) Go to the playlist        |
         > |   5) list all podcasts         |
         > ----------------------------------
         > |   20) Save podcast & playlists |
         > |   21) Load podcast & playlists |
         > ----------------------------------
         > |   0) Exit                      |
         > ---------------------------------- 
         >""".trimMargin(">")
    )
}

fun runMenu() {
    do {
        when(val option = mainMenu()) {
            1 -> addPodcast()
            2 -> searchPodcasts()
            3 -> createPlaylist()
            4 -> playlist()
            5 -> listPodcasts()
            20 -> save()
            21 -> load()
            else -> print("Invalide option: $option")
        }
    } while (true)
}