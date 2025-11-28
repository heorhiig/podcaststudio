package controllers

import ie.setu.Models.Podcast
import ie.setu.Presistence.JSONSerializer
import ie.setu.Presistence.XMLSerializer
import ie.setu.controllers.PodcastAPI
import org.junit.jupiter.api.AfterEach
import java.io.File
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PodcastAPITest {

    private var techInterview: Podcast? = null
    private var ArtDiscus: Podcast? = null
    private var newsShow: Podcast? = null
    private var sportNews: Podcast? = null
    private var educationTops: Podcast? = null
    private var populatedPodcasts: PodcastAPI? = PodcastAPI(XMLSerializer(File("podcasts.xml")))
    private var emptyPodcasts: PodcastAPI? = PodcastAPI(XMLSerializer(File("empty-podcasts.xml")))

    @BeforeEach
    fun setup(){
        techInterview = Podcast("Tech Talk", "Tech podcast", "Technology", "John", true, "2025-01-01", "tech.mp3")
        newsShow = Podcast("Daily News", "News podcast", "News", "Anna", false, "2025-01-02", "news.mp3")
        educationTops = Podcast("Education Hour", "Education podcast", "Education", "Mike", false, "2025-01-03", "education.mp3")
        ArtDiscus = Podcast("Art Time", "Art podcast", "Art", "Lisa", true, "2025-01-04", "art.mp3")
        sportNews = Podcast("Sport Weekly", "Sport podcast", "Sport", "Tom", false, "2025-01-05", "sport.mp3")


        populatedPodcasts!!.add(techInterview!!)
        populatedPodcasts!!.add(newsShow!!)
        populatedPodcasts!!.add(educationTops!!)
        populatedPodcasts!!.add(ArtDiscus!!)
        populatedPodcasts!!.add(sportNews!!)
    }

    @AfterEach
    fun teraDown() {
        techInterview = null
        newsShow = null
        educationTops = null
        ArtDiscus = null
        sportNews = null
        populatedPodcasts = null
        emptyPodcasts = null
    }

    @Nested
    inner class AddPodcast{
        @Test
        fun `add podcast to populated list adds to ArrayList`() {
            val newPodcat = Podcast("Music Time", "descripton", "Music", "John", true, "2025-02-04", "music.mp3")
            assertEquals(5, populatedPodcasts!!.numberOfPodcasts())
            assertTrue(populatedPodcasts!!.add(newPodcat))
            assertEquals(6, populatedPodcasts!!.numberOfPodcasts())
            assertEquals(newPodcat, populatedPodcasts!!.findPodcast(populatedPodcasts!!.numberOfPodcasts() - 1))
        }

        @Test
        fun `add podcast to empty list adds to ArrayList`() {
            val newPodcat = Podcast("Music Time", "descripton", "Music", "John", true, "2025-02-04", "music.mp3")
            assertEquals(0, emptyPodcasts!!.numberOfPodcasts())
            assertTrue(emptyPodcasts!!.add(newPodcat))
            assertEquals(1, emptyPodcasts!!.numberOfPodcasts())
            assertEquals(newPodcat, emptyPodcasts!!.findPodcast(emptyPodcasts!!.numberOfPodcasts() - 1))
        }

    }

    @Nested
    inner class ListPodcast{
        @Test
        fun `listAllPodcasts return No podcasts stored if ArrayList is empty`() {
            assertEquals(0, emptyPodcasts!!.numberOfPodcasts())
            assertTrue(emptyPodcasts!!.listAllPodcasts().lowercase().contains("no podcasts"))
        }

        @Test
        fun `listAllPodcasts return podcasts when ArrayList no empty`() {
            assertEquals(5, populatedPodcasts!!.numberOfPodcasts())
            val podcastsString = populatedPodcasts!!.listAllPodcasts().lowercase()
            assertTrue(podcastsString.contains("tech talk"))
            assertTrue(podcastsString.contains("daily news"))
            assertTrue(podcastsString.contains("education hour"))
            assertTrue(podcastsString.contains("art time"))
            assertTrue(podcastsString.contains("sport weekly"))
        }

        @Test
        fun `listFavoritePodcast return no favorites when ArrayList empty`() {
            assertEquals(0, emptyPodcasts!!.numberOfPodcasts())
            assertTrue(emptyPodcasts!!.listFavoritePodcast().lowercase().contains("no favorite"))
        }

        @Test
        fun `listFavoritePodcast return podcast when ArrayList no empty`() {
            assertEquals(5, populatedPodcasts!!.numberOfPodcasts())
            val podcastString = populatedPodcasts!!.listFavoritePodcast().lowercase()
            assertTrue(podcastString.contains("tech talk"))
            assertTrue(podcastString.contains("art time"))
            assertFalse(podcastString.contains("sport weekly"))
        }
    }

    @Nested
    inner  class FilterPodcasts{
        @Test
        fun `filterByCategory return no podcast when ArrayList empty`() {
            assertTrue(emptyPodcasts!!.filterByCategory("Art").contains("No podcasts"))
        }

        @Test
        fun `filterByCategory return podcast by categori whne ArrayList no empty`() {
            assertEquals(5, populatedPodcasts!!.numberOfPodcasts())
            val category = populatedPodcasts!!.filterByCategory("Technology").lowercase()
            assertTrue(category.contains("tech talk"))
            assertFalse(category.contains("sport weekly"))
            assertFalse(category.contains("art time"))
        }

        @Test
        fun `filterByDate return no podcast when ArrayList empty`() {
            assertTrue(emptyPodcasts!!.filterByDate().contains("No podcasts"))
        }

        @Test
        fun `filterByDate return podcast in right order when ArrayList no empty`() {
            assertEquals(5, populatedPodcasts!!.numberOfPodcasts())
            val firstDate = populatedPodcasts!!.filterByDate().indexOf("2025-01-01")
            val secondDate = populatedPodcasts!!.filterByDate().indexOf("2025-01-02")
            val thirdDate = populatedPodcasts!!.filterByDate().indexOf("2025-01-03")

            assertTrue(firstDate < secondDate)
            assertTrue(secondDate < thirdDate)
        }

        @Test
        fun `filterByAuthor return no podcast when ArrayList is empty`() {
            assertTrue(emptyPodcasts!!.filterByAuthor().contains("No podcasts"))
        }

        @Test
        fun `filterByAuthor return podcasts in right order when ArrayList is no empty`() {
            assertEquals(5, populatedPodcasts!!.numberOfPodcasts())
            val firsIndex = populatedPodcasts!!.filterByAuthor().indexOf("Anna")
            val secondIndex = populatedPodcasts!!.filterByAuthor().indexOf("Lisa")
            val thirdIndex = populatedPodcasts!!.filterByAuthor().indexOf("Tom")

            assertTrue(firsIndex < secondIndex)
            assertTrue(secondIndex < thirdIndex)
        }
    }

    @Nested
    inner class DeletePodcasts{
        @Test
        fun `deletePodcast that does not exist and returns null`() {
            assertNull(emptyPodcasts!!.deletePodcast(0))
            assertNull(emptyPodcasts!!.deletePodcast(-1))
            assertNull(emptyPodcasts!!.deletePodcast(5))
        }

        @Test
        fun `deletePodcast delete podcast and return it`() {
            assertEquals(ArtDiscus, populatedPodcasts!!.deletePodcast(3))
            assertEquals(4, populatedPodcasts!!.numberOfPodcasts())
            assertFalse(populatedPodcasts!!.filterByAuthor().contains("Lisa"))
        }
    }

    @Nested
    inner class UpdatePodcasts{
        @Test
        fun `updatePodcast when ArrayList is empty`() {
            val updatePodcast = Podcast("Tech Talk", "Tech podcast", "Technology", "John", true, "2025-01-01", "tech.mp3")

            assertFalse(emptyPodcasts!!.updatePodcast(0, updatePodcast) ?: false)
            assertFalse(emptyPodcasts!!.updatePodcast(2, updatePodcast) ?: false)
            assertFalse(emptyPodcasts!!.updatePodcast(-1, updatePodcast) ?: false)
        }

        @Test
        fun `updatePodcast that does not exist returns false`() {
            val updatePodcast = Podcast("Tech Talk", "Tech interview", "Technology", "John", true, "2025-01-01", "tech.mp3")
            assertFalse(populatedPodcasts!!.updatePodcast(0, updatePodcast) ?: false)
            assertFalse(populatedPodcasts!!.updatePodcast(-1, updatePodcast) ?: false)
            assertFalse(populatedPodcasts!!.updatePodcast(6, updatePodcast) ?: false)
        }

        @Test
        fun `updatePodcast that does exist, returns true and updates`() {
            assertEquals(educationTops, populatedPodcasts!!.findPodcast(2))
            assertEquals("Education", populatedPodcasts!!.findPodcast(2)!!.podcastCategory)
            assertEquals("Mike", populatedPodcasts!!.findPodcast(2)!!.authorName)
            assertEquals("Education Hour", populatedPodcasts!!.findPodcast(2)!!.podcastTitle)

            assertTrue(populatedPodcasts!!.updatePodcast(2, Podcast("Art Hour", "Art podcast", "Art", "Billy", true, "2025-01-03", "education.mp3")) ?: true)
            assertEquals("Art Hour", populatedPodcasts!!.findPodcast(2)!!.podcastTitle)
            assertEquals("Billy", populatedPodcasts!!.findPodcast(2)!!.authorName)
            assertEquals(true, populatedPodcasts!!.findPodcast(2)!!.favorite)
        }

    }

    @Nested
    inner class PersistenceTests{
        @Test
        fun `saving and loading an empty collection`() {
            val saved = PodcastAPI(XMLSerializer(File("podcast.xml")))
            saved.store()

            val loaded = PodcastAPI(XMLSerializer(File("podcast.xml")))
            loaded.load()

            assertEquals(0, saved.numberOfPodcasts())
            assertEquals(0, loaded.numberOfPodcasts())
            assertEquals(saved.numberOfPodcasts(), loaded.numberOfPodcasts())
        }

        @Test
        fun `save and load podcast without loose data`() {
            val saved = PodcastAPI(XMLSerializer(File("podcast.xml")))
            saved.add(techInterview!!)
            saved.add(newsShow!!)
            saved.add(educationTops!!)
            saved.add(ArtDiscus!!)
            saved.store()

            val loaded = PodcastAPI(XMLSerializer(File("podcast.xml")))
            loaded.load()

            assertEquals(4, saved.numberOfPodcasts())
            assertEquals(4, loaded.numberOfPodcasts())
            assertEquals(saved.findPodcast(0), loaded.findPodcast(0))
            assertEquals(saved.findPodcast(1), loaded.findPodcast(1))
            assertEquals(saved.findPodcast(2), loaded.findPodcast(2))
            assertEquals(saved.findPodcast(3), loaded.findPodcast(3))

        }

        @Test
        fun `saving and loading an empty collection for json`() {
            val saved = PodcastAPI(XMLSerializer(File("podcast.json")))
            saved.store()

            val loaded = PodcastAPI(XMLSerializer(File("podcast.json")))
            loaded.load()

            assertEquals(0, saved.numberOfPodcasts())
            assertEquals(0, loaded.numberOfPodcasts())
            assertEquals(saved.numberOfPodcasts(), loaded.numberOfPodcasts())
        }

        @Test
        fun `save and load podcast without loose data for json`() {
            val saved = PodcastAPI(JSONSerializer(File("podcast.json")))
            saved.add(techInterview!!)
            saved.add(newsShow!!)
            saved.add(educationTops!!)
            saved.add(ArtDiscus!!)
            saved.store()

            val loaded = PodcastAPI(JSONSerializer(File("podcast.json")))
            loaded.load()

            assertEquals(4, saved.numberOfPodcasts())
            assertEquals(4, loaded.numberOfPodcasts())
            assertEquals(saved.findPodcast(0), loaded.findPodcast(0))
            assertEquals(saved.findPodcast(1), loaded.findPodcast(1))
            assertEquals(saved.findPodcast(2), loaded.findPodcast(2))
            assertEquals(saved.findPodcast(3), loaded.findPodcast(3))
        }
    }

    @Nested
    inner class CountFunctions{
        @Test
        fun countNumberOfPodcastsCorrectly() {
            assertEquals(5, populatedPodcasts!!.numberOfPodcasts())
            assertEquals(0, emptyPodcasts!!.numberOfPodcasts())
        }

        @Test
        fun countNumberOfFavoritePodcastsCorrectly() {
            assertEquals(2, populatedPodcasts!!.numberOfFavorite())
            assertEquals(0, emptyPodcasts!!.numberOfFavorite())
        }
    }

    @Nested
    inner class ValidIndexTests{
        @Test
        fun `validIndex returns false if list is empty`() {
            assertFalse(emptyPodcasts!!.validIndex(0))
            assertFalse(emptyPodcasts!!.validIndex(2))
            assertFalse(emptyPodcasts!!.validIndex(4))
        }

        @Test
        fun `validIndex returns true when list is not empty`() {
            assertTrue(populatedPodcasts!!.validIndex(0))
            assertTrue(populatedPodcasts!!.validIndex(3))
            assertTrue(populatedPodcasts!!.validIndex(2))
        }

        @Test
        fun `validIndex returns false when index bigger that list size`() {
            assertFalse(populatedPodcasts!!.validIndex(10))
            assertFalse(populatedPodcasts!!.validIndex(55))
            assertFalse(populatedPodcasts!!.validIndex(11))

        } @Test
        fun `validIndex returns false when index negative `() {
            assertFalse(populatedPodcasts!!.validIndex(-10))
            assertFalse(populatedPodcasts!!.validIndex(-1))
            assertFalse(populatedPodcasts!!.validIndex(-2))
        }
    }

    @Nested
    inner class FindPodcastTests{
        @Test
        fun `findPodcast return null when list is empty`() {
            assertNull(emptyPodcasts!!.findPodcast(0))
            assertNull(emptyPodcasts!!.findPodcast(3))
        }

        @Test
        fun `findPodcast return podcast for valid index`() {
            assertEquals(techInterview, populatedPodcasts!!.findPodcast(0))
            assertEquals(newsShow, populatedPodcasts!!.findPodcast(1))
            assertEquals(educationTops, populatedPodcasts!!.findPodcast(2))
        }

        @Test
        fun `findPodcast return null if index bigger than ist size`() {
            assertNull(populatedPodcasts!!.findPodcast(10))
            assertNull(populatedPodcasts!!.findPodcast(21))
            assertNull(populatedPodcasts!!.findPodcast(8))
        }

        @Test
        fun `findPodcast return null if index negative`() {
            assertNull(populatedPodcasts!!.findPodcast(-10))
            assertNull(populatedPodcasts!!.findPodcast(-1))
            assertNull(populatedPodcasts!!.findPodcast(-5))
        }
    }
}
