package alex.zhurkov.gifloader.feature.main.ui

import alex.zhurkov.gifloader.R
import alex.zhurkov.gifloader.feature.main.model.GifItem
import alex.zhurkov.gifloader.feature.main.presentation.MainActivityModel
import alex.zhurkov.gifloader.feature.main.presentation.MainContentState
import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.LiveData
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.valentinilk.shimmer.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiModel: LiveData<MainActivityModel>,
    onQueryChanged: (String) -> Unit,
    onLastGifVisible: (gifId: String) -> Unit
) {
    val model by uiModel.observeAsState()
    model?.let { renderModel ->
        Column(modifier = modifier) {
            SearchView(
                modifier = Modifier
                    .fillMaxWidth(),
                onQueryChanged = onQueryChanged
            )
            AnimatedContent(
                targetState = renderModel.mainContentState::class
            ) {
                when (val state = renderModel.mainContentState) {
                    is MainContentState.Data -> GifGrid(
                        gifItems = state.gifItems,
                        onLastVisibleElementUpdated = onLastGifVisible
                    )
                    MainContentState.WelcomeScreen -> WelcomeScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                    )
                    MainContentState.SearchResultEmpty -> SearchEmptyView(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchView(
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit
) {
    var queryState by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        value = queryState,
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Black,
        ),
        onValueChange = {
            queryState = it
            onQueryChanged(it)
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_hint),
                color = Color.Gray
            )
        },
        singleLine = true,
        trailingIcon = {
            if (queryState.isNotEmpty()) {
                IconButton(onClick = {
                    queryState = ""
                    onQueryChanged("")
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
fun SearchEmptyView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieInfiniteAnimation(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(Dp(200f)),
            spec = LottieCompositionSpec.RawRes(R.raw.lottie_search_empty)
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.search_no_results),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LottieInfiniteAnimation(
    modifier: Modifier = Modifier,
    spec: LottieCompositionSpec.RawRes
) {
    val composition by rememberLottieComposition(
        spec = spec,
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        enableMergePaths = true,
        modifier = modifier
    )
}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier
) {
    LottieInfiniteAnimation(
        modifier = modifier,
        spec = LottieCompositionSpec.RawRes(R.raw.lottie_welcome)
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun GifGrid(
    modifier: Modifier = Modifier,
    gifItems: List<GifItem>,
    onLastVisibleElementUpdated: (id: String) -> Unit
) {
    val columns = 3
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        val state = rememberLazyGridState()
        LazyVerticalGrid(
            state = state,
            modifier = modifier,
            contentPadding = PaddingValues(
                vertical = dimensionResource(id = R.dimen.padding_8)
            ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_4)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_4)),
            columns = GridCells.Fixed(columns),
        ) {
            itemsIndexed(
                items = gifItems,
                key = { _, item -> item.id }
            ) { index, gifItem ->
                val lastVisibleId by remember {
                    derivedStateOf {
                        with(state.layoutInfo) {
                            (visibleItemsInfo.lastOrNull()?.key as? String).takeIf { it == (gifItem as? GifItem.Data)?.id }
                        }
                    }
                }
                lastVisibleId?.run(onLastVisibleElementUpdated)
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corners_4)))
                ) {
                    when (gifItem) {
                        is GifItem.Data -> GifPreview(
                            modifier = Modifier.fillMaxSize(),
                            item = gifItem
                        )
                        is GifItem.Loading -> GifLoading(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
fun GifLoading(modifier: Modifier = Modifier) {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = LocalShimmerTheme.current.copy(
            animationSpec = infiniteRepeatable(
                animation = tween(
                    600,
                    easing = LinearEasing,
                    delayMillis = 300,
                ),
                repeatMode = RepeatMode.Restart,
            )
        ),
    )
    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(MaterialTheme.colorScheme.primaryContainer)
    )
}

@Composable
fun GifPreview(
    modifier: Modifier = Modifier,
    item: GifItem.Data
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .placeholder(R.drawable.ic_loading_animated)
        .error(R.drawable.ic_loading_error)
        .build()
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(data = item.url)
            .crossfade(true)
            .build(),
        imageLoader = imageLoader,
        error = ColorPainter(Color.Black),
    )
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}