package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.Reducer
import alex.zhurkov.gifloader.common.arch.StateToModelMapper
import alex.zhurkov.gifloader.common.getPrivateProperty
import alex.zhurkov.gifloader.common.setAndReturnPrivateProperty
import alex.zhurkov.gifloader.common.singleItemList
import alex.zhurkov.gifloader.domain.model.Gif
import alex.zhurkov.gifloader.domain.model.GifPage
import alex.zhurkov.gifloader.domain.service.GifSearchUseCase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Job
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var gifSearchUseCase: GifSearchUseCase

    @Mock
    lateinit var reducer: Reducer<MainActivityState, MainActivityChange>

    @Mock
    lateinit var stateToModelMapper: StateToModelMapper<MainActivityState, MainActivityModel>

    private lateinit var observer: Observer<in MainActivityModel>

    private val viewModel by lazy {
        MainActivityViewModel(
            gifSearchUseCase = gifSearchUseCase,
            reducer = reducer,
            stateToModelMapper = stateToModelMapper
        )
    }

    @Before
    fun setup() {
        observer = mock<Observer<MainActivityModel>>()
        viewModel.observableModel.observeForever(observer)
    }

    @Test
    fun `when observer is active for the first time, observeSearchQueryChanges is invoked`() {
        viewModel.getPrivateProperty<MainActivityViewModel, Job>("searchJob").run {
            assertNotNull(this)
        }
    }

    @Test
    fun `given state is updated, when query is changed, loadPageJob is cancelled`() {
        val loadPageJobMock = mock<Job>()
        viewModel.setAndReturnPrivateProperty(variableName = "loadPageJob", data = loadPageJobMock)
        viewModel.updateState(MainActivityState.EMPTY.copy(searchQuery = "search query"))
        verify(loadPageJobMock).cancel()
    }

    @Test
    fun `given state is updated, when query is changed, loadGifPage is invoked`() {
        viewModel.updateState(MainActivityState.EMPTY.copy(searchQuery = "search query"))
        viewModel.getPrivateProperty<MainActivityViewModel, Job>("loadPageJob").run {
            assertNotNull(this)
        }
    }

    @Test
    fun `given last gif id is updated, when it is equal to the last gif in the state, loadGifPage is invoked`() {
        val lastVisibleGifId = "last visible gif id"
        val gifMock = mock<Gif> {
            on { id } doReturn lastVisibleGifId
        }
        val gifPageMock = mock<GifPage> {
            on { gifs } doReturn gifMock.singleItemList()
        }
        viewModel.updateState(
            MainActivityState.EMPTY.copy(
                searchQuery = "search query",
                lastVisibleGifId = lastVisibleGifId,
                gifPages = gifPageMock.singleItemList()
            )
        )
        viewModel.getPrivateProperty<MainActivityViewModel, Job>("loadPageJob").run {
            assertNotNull(this)
        }
    }

    private fun MainActivityViewModel.updateState(state: MainActivityState) =
        this::class.java.kotlin.memberProperties.first { it.name == "state" }.run {
            isAccessible = true
            (this as KMutableProperty<MainActivityState>).setter.call(viewModel, state)
        }


}