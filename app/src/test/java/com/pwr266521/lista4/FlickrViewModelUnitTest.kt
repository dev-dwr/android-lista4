package com.pwr266521.lista4

import com.pwr266521.lista4.model.FlickrResponse
import com.pwr266521.lista4.model.Item
import com.pwr266521.lista4.model.Media
import com.pwr266521.lista4.repository.FlickrRepository
import com.pwr266521.lista4.service.FlickrApiService
import com.pwr266521.lista4.viewmodel.FlickrViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import kotlin.RuntimeException as RuntimeException1


class FlickrViewModelTest {

    private lateinit var viewModel: FlickrViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    private val service: FlickrApiService = mock()

    private lateinit var flickerRepository: FlickrRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        flickerRepository = FlickrRepository(service)
        viewModel = FlickrViewModel(flickerRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `load photos with success`() = testDispatcher.runBlockingTest {
        // given
        val fakeResponse = createSampleData()
        `when`(service.getPublicPhotos()).thenReturn(fakeResponse)
        `when`(flickerRepository.getPublicPhotos()).thenReturn(fakeResponse)

        // when
        viewModel.loadPhotos()

        // then
        verify(service, times(2)).getPublicPhotos()
        assert(viewModel.photos.value == fakeResponse)
        assert(!viewModel.isLoading.value)
    }


    @Test
    fun `should set null values when api call fails`() = testDispatcher.runBlockingTest {
        //given
        `when`(service.getPublicPhotos()).thenThrow(RuntimeException())

        //when
        viewModel.loadPhotos()

        //then
        assertEquals(true, viewModel.isLoading.value)
        assertEquals(null, viewModel.photos.value)
    }

    private fun createSampleData(): FlickrResponse {
        val media = Media(m = "https://example.com/media.jpg")
        val item = Item(
            title = "Sample Item",
            link = "https://example.com/item",
            media = media,
            date_taken = "2023-12-18",
            description = "A sample item description",
            published = "2023-12-18",
            author = "Author Name",
            author_id = "author123",
            tags = "tag1, tag2, tag3"
        )
        val items = listOf(item)
        return FlickrResponse(
            title = "Flickr Response Title",
            link = "https://example.com/flickrresponse",
            description = "Description of Flickr Response",
            modified = "2023-12-18",
            generator = "Flickr Generator",
            items = items
        )
    }
}