package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.BaseViewModel
import alex.zhurkov.gifloader.common.arch.Reducer
import alex.zhurkov.gifloader.common.arch.StateToModelMapper
import alex.zhurkov.gifloader.domain.service.GifSearchUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

private const val DELAY_SEARCH_MS = 300L
const val QUERY_MIN_LENGTH = 3

@OptIn(FlowPreview::class)
class MainActivityViewModel(
    private val gifSearchUseCase: GifSearchUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    reducer: Reducer<MainActivityState, MainActivityChange>,
    stateToModelMapper: StateToModelMapper<MainActivityState, MainActivityModel>
) : BaseViewModel<MainActivityAction, MainActivityChange, MainActivityState, MainActivityModel>(
    dispatcher = dispatcher, reducer = reducer, stateToModelMapper = stateToModelMapper
) {
    private val searchTextChannel = MutableStateFlow("")
    private var searchJob: Job? = null
    private var loadPageJob: Job? = null
    override var state = MainActivityState.EMPTY
        set(value) {
            val isQueryChanged = field.searchQuery != value.searchQuery
            val isLastGifUpdated = field.lastVisibleGifId != value.lastVisibleGifId
            field = value
            val shouldLoadNextPage = isLastGifUpdated && field.lastGifId() == field.lastVisibleGifId
            if (isQueryChanged) {
                loadPageJob?.cancel()
                loadGifPage(query = field.searchQuery, offset = 0)
            }
            if (shouldLoadNextPage) loadGifPage(query = field.searchQuery, offset = state.offset)
        }

    override fun onCleared() {
        searchJob?.cancel()
        loadPageJob?.cancel()
        super.onCleared()
    }

    override fun onObserverActive(isFirstTime: Boolean) {
        super.onObserverActive(isFirstTime)
        if (isFirstTime) {
            observeSearchQueryChanges()
        }
    }

    override suspend fun provideChangesObservable(): Flow<MainActivityChange> {
        return emptyFlow()
    }

    override fun processAction(action: MainActivityAction) = when (action) {
        is MainActivityAction.QueryChanged -> {
            viewModelScope.launch(dispatcher) { searchTextChannel.emit(action.data.trim()) }
            Unit
        }
        is MainActivityAction.LastVisibleGifChanged -> sendChange(
            MainActivityChange.LastVisibleGifChanged(action.id)
        )
    }

    private fun onError(e: Throwable) {
        if (e is CancellationException) return
        sendEvent(MainActivityEvent.DisplayError(e))
    }

    private fun loadGifPage(
        query: String,
        offset: Int
    ) {
        val pageId = query + offset
        if (query.length < QUERY_MIN_LENGTH) return
        if (state.loadingPageId == pageId) return
        if (state.isPageLoaded(pageId)) return
        if (state.hasMorePages().not()) {
            Timber.d("Last page (id=[$pageId]) for query [$query] is already loaded, not requesting more gifs")
            return
        }

        loadPageJob = viewModelScope.launch(dispatcher) {
            execute(
                action = { gifSearchUseCase.searchGifs(query = query, offset = offset) },
                onStart = { sendChange(MainActivityChange.GifPageLoadingIdChanged(id = pageId)) },
                onComplete = { sendChange(MainActivityChange.GifPageLoadingIdChanged(id = null)) },
                onSuccess = { sendChange(MainActivityChange.GifPageLoaded(it)) },
                onErrorOccurred = ::onError
            )
        }
    }

    private fun observeSearchQueryChanges() {
        searchJob = viewModelScope.launch(dispatcher) {
            searchTextChannel.debounce(DELAY_SEARCH_MS).distinctUntilChanged()
                .catch { e -> onError(e) }
                .onEach { sendChange(MainActivityChange.SearchQueryChanged(it)) }.launchIn(this)
        }
    }
}
