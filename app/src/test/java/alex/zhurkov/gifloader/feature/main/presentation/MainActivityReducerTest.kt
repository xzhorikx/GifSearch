import alex.zhurkov.gifloader.domain.model.GifPage
import alex.zhurkov.gifloader.feature.main.presentation.MainActivityChange
import alex.zhurkov.gifloader.feature.main.presentation.MainActivityReducer
import alex.zhurkov.gifloader.feature.main.presentation.MainActivityState
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class MainActivityReducerTest {

    private val reducer = MainActivityReducer()
    private val initialState = MainActivityState.EMPTY

    @Test
    fun `GifPageLoaded test`() {
        val value = mock(GifPage::class.java)
        val change = MainActivityChange.GifPageLoaded(value)
        val expectedState = initialState.copy(gifPages = listOf(value))
        val newState = reducer.reduce(initialState, change)
        assertEquals(expectedState, newState)
    }

    @Test
    fun `GifPageLoadingIdChanged test`() {
        val value = "new loading gif page id"
        val change = MainActivityChange.GifPageLoadingIdChanged(value)
        val expectedState = initialState.copy(loadingPageId = value)
        val newState = reducer.reduce(initialState, change)
        assertEquals(expectedState, newState)
    }

    @Test
    fun `SearchQueryChanged test`() {
        val value = "cats"
        val change = MainActivityChange.SearchQueryChanged(value)
        val expectedState = initialState.copy(searchQuery = value)
        val newState = reducer.reduce(initialState, change)
        assertEquals(expectedState, newState)
    }

    @Test
    fun `LastVisibleGifChanged test`() {
        val value = "new visible gif"
        val change = MainActivityChange.LastVisibleGifChanged(value)
        val expectedState = initialState.copy(lastVisibleGifId = value)
        val newState = reducer.reduce(initialState, change)
        assertEquals(expectedState, newState)
    }
}