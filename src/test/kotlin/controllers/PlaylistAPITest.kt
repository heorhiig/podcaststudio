package controllers

import ie.setu.Models.Playlist
import ie.setu.Models.Podcast
import ie.setu.Presistence.JSONSerializer
import ie.setu.Presistence.XMLSerializer
import ie.setu.controllers.PlaylistAPI
import ie.setu.controllers.PodcastAPI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class PlaylistAPITest {

    private var ownPlaylist: Playlist? = null
    private var techPlaylist: Playlist? = null
    private var artPlaylist: Playlist? = null
    private var techInterview: Podcast? = null
    private var ArtDiscus: Podcast? = null
    private var newsShow: Podcast? = null
    private var sportNews: Podcast? = null
    private var educationTops: Podcast? = null
    private var populatedPlaylists: PlaylistAPI? = PlaylistAPI(XMLSerializer(File("playlist-test.xml")))
    private var emptyPlaylists: PlaylistAPI? = PlaylistAPI(XMLSerializer(File("empty-playlist-test.xml")))
    private var podcastsList: PodcastAPI? = PodcastAPI(XMLSerializer(File("podcasts-list.xml")))


    @BeforeEach
    fun setup() {
        techInterview = Podcast("Tech Talk", "Tech podcast", "Technology", "John", true, "2025-01-01", "tech.mp3")
        newsShow = Podcast("Daily News", "News podcast", "News", "Anna", false, "2025-01-02", "news.mp3")
        educationTops = Podcast("Education Hour", "Education podcast", "Education", "Mike", false, "2025-01-03", "education.mp3")
        ArtDiscus = Podcast("Art Time", "Art podcast", "Art", "Lisa", true, "2025-01-04", "art.mp3")
        sportNews = Podcast("Sport Weekly", "Sport podcast", "Sport", "Tom", false, "2025-01-05", "sport.mp3")

        podcastsList!!.add(techInterview!!)
        podcastsList!!.add(newsShow!!)
        podcastsList!!.add(educationTops!!)
        podcastsList!!.add(ArtDiscus!!)
        podcastsList!!.add(sportNews!!)


        ownPlaylist = Playlist("My dreams", "No Descripton", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))
        techPlaylist = Playlist("My dreams", "No Descripton", true, mutableListOf(educationTops!!, newsShow!!, sportNews!!))
        artPlaylist = Playlist("My dreams", "No Descripton", false, mutableListOf(ArtDiscus!!, newsShow!!, educationTops!!))

        populatedPlaylists!!.addPlaylist(ownPlaylist!!)
        populatedPlaylists!!.addPlaylist(techPlaylist!!)
        populatedPlaylists!!.addPlaylist(artPlaylist!!)
    }

    @AfterEach
    fun downTear() {
        ownPlaylist = null
        techPlaylist = null
        artPlaylist = null
        populatedPlaylists = null
        emptyPlaylists = null
    }

    @Nested
    inner class AddPlaylist{
        @Test
        fun `add playlist in populated list`() {
            val newPlaylist = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))
            assertEquals(3, populatedPlaylists!!.numberOfPlaylists())
            assertTrue(populatedPlaylists!!.addPlaylist(newPlaylist))
            assertEquals(4, populatedPlaylists!!.numberOfPlaylists())
            assertEquals(
                newPlaylist,
                populatedPlaylists!!.findPlaylist(populatedPlaylists!!.numberOfPlaylists() - 1)
            )
        }

        @Test
        fun`add podcast to empty list and return podcast`() {
            val newPlaylist = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())
            assertTrue(emptyPlaylists!!.addPlaylist(newPlaylist))
            assertEquals(1, emptyPlaylists!!.numberOfPlaylists())
            assertEquals(
                newPlaylist,
                emptyPlaylists!!.findPlaylist(emptyPlaylists!!.numberOfPlaylists() - 1)
            )
        }

        @Test
        fun `add many podcasts in playlist in populatedPlaylists`() {
            val newPlaylist1 = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))
            val newPlaylist2 = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))
            val newPlaylist3 = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))

            assertTrue(populatedPlaylists!!.addPlaylist(newPlaylist1))
            assertTrue(populatedPlaylists!!.addPlaylist(newPlaylist2))
            assertTrue(populatedPlaylists!!.addPlaylist(newPlaylist3))

            assertEquals(6, populatedPlaylists!!.numberOfPlaylists())
        }

        @Test
        fun `add many podcasts in emptyPlaylists`() {
            val newPlaylist1 = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))
            val newPlaylist2 = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))
            val newPlaylist3 = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))

            assertTrue(emptyPlaylists!!.addPlaylist(newPlaylist1))
            assertTrue(emptyPlaylists!!.addPlaylist(newPlaylist2))
            assertTrue(emptyPlaylists!!.addPlaylist(newPlaylist3))

            assertEquals(3, emptyPlaylists!!.numberOfPlaylists())
        }
    }

    @Nested
    inner class ListAllPlaylists{
        @Test
        fun `list all playlists on empty returns message`() {
            assertEquals("No playlist stored", emptyPlaylists!!.listAllPlaylists())
        }

        @Test
        fun `list all on populated returns formatted output`() {
            val result = populatedPlaylists!!.listAllPlaylists()
            assertTrue(result.contains("0:"))
            assertTrue(result.contains("1:"))
            assertTrue(result.contains("2:"))
        }
    }

    @Nested
    inner class ListFavoritePlaylist{

        @Test
        fun `favorite list empty returns message`() {
            val api = PlaylistAPI(XMLSerializer(File("test.xml")))
            api.addPlaylist(Playlist("A", "B", false))
            assertEquals("No favorite podcasts", api.listFavoritePlaylist())
        }

        @Test
        fun `favorite list returns only favorites`() {
            val result = populatedPlaylists!!.listFavoritePlaylist()
            assertTrue(result.contains("0:"))
            assertFalse(result.contains("2:"))
        }
    }

    @Nested
    inner class FindPlaylist{

        @Test
        fun `find valid index in populated list returns playlist`() {
            val result = populatedPlaylists!!.findPlaylist(0)
            assertEquals(ownPlaylist, result)
        }


        @Test
        fun `find invalid negative index returns null`() {
            assertEquals(null, populatedPlaylists!!.findPlaylist(-1))
        }

        @Test
        fun `find index larger than size returns null`() {
            assertEquals(null, populatedPlaylists!!.findPlaylist(99))
        }

        @Test
        fun `find playlist in empty list return null`() {
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())

            assertEquals(null, emptyPlaylists!!.findPlaylist(0))
            assertEquals(null, emptyPlaylists!!.findPlaylist(1))
            assertEquals(null, emptyPlaylists!!.findPlaylist(-1))
        }
    }

    @Nested
    inner class DeletePlaylist{

        @Test
        fun `delete valid index removes playlist`() {
            assertEquals(ownPlaylist, populatedPlaylists!!.deletePlaylist(0))
            assertEquals(2, populatedPlaylists!!.numberOfPlaylists())
        }

        @Test
        fun `delete invalid index returns null`() {
            assertEquals(null, populatedPlaylists!!.deletePlaylist(10))
        }

        @Test
        fun `deleting from empty list always returns null`() {
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())

            assertNull(emptyPlaylists!!.deletePlaylist(0))
            assertNull(emptyPlaylists!!.deletePlaylist(1))
            assertNull(emptyPlaylists!!.deletePlaylist(-1))

            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())
        }
    }

    @Nested
    inner class UpdatePlaylist{

        @Test
        fun `updating a playlist at valid index updates data and returns true`() {
            val updatedPlaylist = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))


            val result = populatedPlaylists!!.updatePlaylist(0, updatedPlaylist)

            assertTrue(result)
            val found = populatedPlaylists!!.findPlaylist(0)

            assertEquals("New playlist", found?.name)
            assertEquals("New playlist", found?.description)
            assertEquals(true, found?.favorite)
            assertEquals(3, found?.podcasts?.size)
        }

        @Test
        fun `updating last playlist works correctly`() {
            val lastIndex = populatedPlaylists!!.numberOfPlaylists() - 1

            val updated = Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!))

            val result = populatedPlaylists!!.updatePlaylist(lastIndex, updated)

            assertTrue(result)
            assertEquals("New playlist", populatedPlaylists!!.findPlaylist(lastIndex)?.name)
        }

        @Test
        fun `updating with invalid negative index returns false`() {
            val sizeBefore = populatedPlaylists!!.numberOfPlaylists()

            val result = populatedPlaylists!!.updatePlaylist(-1, Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!)))

            assertFalse(result)
            assertEquals(sizeBefore, populatedPlaylists!!.numberOfPlaylists())
        }

        @Test
        fun `updating with index larger than size returns false`() {
            val sizeBefore = populatedPlaylists!!.numberOfPlaylists()

            val result = populatedPlaylists!!.updatePlaylist(100, Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!)))

            assertFalse(result)
            assertEquals(sizeBefore, populatedPlaylists!!.numberOfPlaylists())
        }

        @Test
        fun `updating in empty playlist list always returns false`() {
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())

            val result = emptyPlaylists!!.updatePlaylist(0, Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!)))
            assertFalse(result)
        }

        @Test
        fun `updatePlaylist does not modify list when update fails`() {
            val sizeBefore = populatedPlaylists!!.numberOfPlaylists()

            populatedPlaylists!!.updatePlaylist(99, Playlist("New playlist", "New playlist", true, mutableListOf(techInterview!!, newsShow!!, educationTops!!)))

            assertEquals(sizeBefore, populatedPlaylists!!.numberOfPlaylists())
        }
    }

    @Nested
    inner class AddPodcastToPlaylist{

        @Test
        fun `add podcast valid indexes returns true`() {
            val result = populatedPlaylists!!.addPodcastToPlaylist(0, 0, podcastsList!!)
            assertTrue(result)
            assertEquals(4, populatedPlaylists!!.findPlaylist(0)!!.podcasts.size)
        }

        @Test
        fun `invalid playlist index returns false`() {
            assertFalse(populatedPlaylists!!.addPodcastToPlaylist(99, 0, podcastsList!!))
        }

        @Test
        fun `invalid podcast index returns false`() {
            assertFalse(populatedPlaylists!!.addPodcastToPlaylist(0, 99, podcastsList!!))
        }
    }


    @Nested
    inner class DeletePodcastFromPlaylist{

        @Test
        fun `delete podcast valid`() {
            val initial = populatedPlaylists!!.findPlaylist(0)!!.podcasts.size
            populatedPlaylists!!.deletePodcastFromPlaylist(0, 0)
            assertEquals(initial - 1, populatedPlaylists!!.findPlaylist(0)!!.podcasts.size)
        }

        @Test
        fun `delete podcast invalid playlist index does nothing`() {
            val before = populatedPlaylists!!.findPlaylist(0)!!.podcasts.size
            populatedPlaylists!!.deletePodcastFromPlaylist(99, 0)
            assertEquals(before, populatedPlaylists!!.findPlaylist(0)!!.podcasts.size)
        }

        @Test
        fun `delete podcast invalid podcast index does nothing`() {
            val before = populatedPlaylists!!.findPlaylist(0)!!.podcasts.size
            populatedPlaylists!!.deletePodcastFromPlaylist(0, 99)
            assertEquals(before, populatedPlaylists!!.findPlaylist(0)!!.podcasts.size)
        }
    }

    @Nested
    inner class CountFunctions{

        @Test
        fun `numberOfPlaylists returns correct count`() {
            assertEquals(3, populatedPlaylists!!.numberOfPlaylists())
        }

        @Test
        fun `numberOfFavorite returns correct count`() {
            val fav = populatedPlaylists!!.numberOfFavorite()
            assertTrue(fav >= 1)
        }
    }

    @Nested
    inner class PersistenceTests{

        @Test
        fun `saving and loading an empty playlist collection xml`() {
            val saved = PlaylistAPI(XMLSerializer(File("playlist.xml")))
            saved.store()

            val loaded = PlaylistAPI(XMLSerializer(File("playlist.xml")))
            loaded.load()

            assertEquals(0, saved.numberOfPlaylists())
            assertEquals(0, loaded.numberOfPlaylists())
            assertEquals(saved.numberOfPlaylists(), loaded.numberOfPlaylists())
        }

        @Test
        fun `save and load playlists without losing data xml`() {
            val saved = PlaylistAPI(XMLSerializer(File("playlist.xml")))

            saved.addPlaylist(ownPlaylist!!)
            saved.addPlaylist(techPlaylist!!)
            saved.addPlaylist(artPlaylist!!)
            saved.store()

            val loaded = PlaylistAPI(XMLSerializer(File("playlist.xml")))
            loaded.load()

            assertEquals(3, saved.numberOfPlaylists())
            assertEquals(3, loaded.numberOfPlaylists())

            assertEquals(saved.findPlaylist(0), loaded.findPlaylist(0))
            assertEquals(saved.findPlaylist(1), loaded.findPlaylist(1))
            assertEquals(saved.findPlaylist(2), loaded.findPlaylist(2))
        }

        @Test
        fun `saving and loading an empty playlist collection json`() {
            val saved = PlaylistAPI(JSONSerializer(File("playlist.json")))
            saved.store()

            val loaded = PlaylistAPI(JSONSerializer(File("playlist.json")))
            loaded.load()

            assertEquals(0, saved.numberOfPlaylists())
            assertEquals(0, loaded.numberOfPlaylists())
            assertEquals(saved.numberOfPlaylists(), loaded.numberOfPlaylists())
        }

        @Test
        fun `save and load playlists without losing data json`() {
            val saved = PlaylistAPI(JSONSerializer(File("playlist.json")))

            saved.addPlaylist(ownPlaylist!!)
            saved.addPlaylist(techPlaylist!!)
            saved.addPlaylist(artPlaylist!!)
            saved.store()

            val loaded = PlaylistAPI(JSONSerializer(File("playlist.json")))
            loaded.load()

            assertEquals(3, saved.numberOfPlaylists())
            assertEquals(3, loaded.numberOfPlaylists())

            assertEquals(saved.findPlaylist(0), loaded.findPlaylist(0))
            assertEquals(saved.findPlaylist(1), loaded.findPlaylist(1))
            assertEquals(saved.findPlaylist(2), loaded.findPlaylist(2))
        }
    }

}