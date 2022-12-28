package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.singleItemList
import alex.zhurkov.gifloader.domain.model.GifPage
import alex.zhurkov.gifloader.feature.main.model.GifItem
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock


class MainStateToModelMapperTest {


    private val mapper = MainStateToModelMapper()

    @Test
    fun `given search query is above min length, when loadingPageId is not null, loading indicators are added`() {
        val state = MainActivityState.EMPTY.copy(
            searchQuery = QUERY_MIN_LENGTH.generateString(),
            loadingPageId = "loading page id"
        )
        val result = mapper.mapStateToModel(state)
        val gifItems = (result.mainContentState as MainContentState.Data).gifItems
        assertTrue(gifItems.all { it is GifItem.Loading })
    }

    @Test
    fun `when search query is below required minimum length, main content is MainContentState_WelcomeScreen`() {
        val state = MainActivityState.EMPTY.copy(
            searchQuery = (QUERY_MIN_LENGTH - 1).generateString()
        )
        val result = mapper.mapStateToModel(state)
        assertTrue(result.mainContentState is MainContentState.WelcomeScreen)
    }

    @Test
    fun `given page is not loading, when search query is above required minimum length, main content is MainContentState_SearchResultEmpty`() {
        val state = MainActivityState.EMPTY.copy(
            searchQuery = QUERY_MIN_LENGTH.generateString()
        )
        val result = mapper.mapStateToModel(state)
        assertTrue(result.mainContentState is MainContentState.SearchResultEmpty)
    }

    @Test
    fun `given search query is above required minimum, when State_GifPages is not empty, main content is MainContentState_Data`() {
        val state = MainActivityState.EMPTY.copy(
            searchQuery = QUERY_MIN_LENGTH.generateString(),
            gifPages = mock(GifPage::class.java).singleItemList()
        )
        val result = mapper.mapStateToModel(state)
        val gifs = (result.mainContentState as MainContentState.Data).gifItems
        assertTrue(gifs.all { it is GifItem.Data })
    }

    private fun Int.generateString() = (0 until this).joinToString(separator = "") { it.toString() }
}