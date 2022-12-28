package alex.zhurkov.gifloader.feature.main

import alex.zhurkov.gifloader.R
import alex.zhurkov.gifloader.common.arch.UIEvent
import alex.zhurkov.gifloader.feature.main.di.MainActivityComponent
import alex.zhurkov.gifloader.feature.main.presentation.MainActivityAction
import alex.zhurkov.gifloader.feature.main.presentation.MainActivityEvent
import alex.zhurkov.gifloader.feature.main.presentation.MainActivityViewModel
import alex.zhurkov.gifloader.feature.main.presentation.MainActivityViewModelFactory
import alex.zhurkov.gifloader.feature.main.ui.MainScreen
import alex.zhurkov.gifloader.ui.theme.GifLoaderTheme
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.Observer
import timber.log.Timber
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    private val component: MainActivityComponent by lazy {
        (application as MainActivityComponent.ComponentProvider).provideMainComponent(this)
    }

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory
    private val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        viewModel.observableEvents.observe(this, Observer(::renderEvent))
        setContent {
            GifLoaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                PaddingValues(
                                    start = dimensionResource(id = R.dimen.padding_16),
                                    end = dimensionResource(id = R.dimen.padding_16),
                                    top = dimensionResource(id = R.dimen.padding_16)
                                )
                            ),
                        uiModel = viewModel.observableModel,
                        onQueryChanged = {
                            viewModel.dispatch(MainActivityAction.QueryChanged(it))
                        },
                        onLastGifVisible = {
                            viewModel.dispatch(MainActivityAction.LastVisibleGifChanged(it))
                        }
                    )
                }
            }
        }
    }

    private fun renderEvent(event: UIEvent) {
        if (event is MainActivityEvent) {
            when (event) {
                is MainActivityEvent.DisplayError -> {
                    Toast.makeText(
                        this,
                        getString(R.string.error_message_template, event.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}