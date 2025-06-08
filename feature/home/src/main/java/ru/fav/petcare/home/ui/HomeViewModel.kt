package ru.fav.petcare.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.domain.usecase.common.GetFaqDataUseCase
import ru.fav.petcare.domain.usecase.common.GetMapDataUseCase
import ru.fav.petcare.home.ui.state.HomeEffect
import ru.fav.petcare.home.ui.state.HomeEvent
import ru.fav.petcare.home.ui.state.HomeState
import ru.fav.petcare.navigation.NavMain
import ru.fav.petcare.presentation.R
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMapDataUseCase: GetMapDataUseCase,
    private val getFaqDataUseCase: GetFaqDataUseCase,
    private val resourceProvider: ResourceProvider,
    private val navMain: NavMain,
    ): ViewModel() {

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState = _homeState.asStateFlow()


    fun reduce(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnServicesClicked -> navigateToServices()
            is HomeEvent.GetData -> loadData()
        }
    }

    private fun loadData() {
        _homeState.value = HomeState.Loading
        viewModelScope.launch {
            runCatching {
                delay(1000)
                val mapData = getMapDataUseCase()
                val faqData = getFaqDataUseCase()
                Pair(mapData, faqData)
            }.fold(
                onSuccess = { (map, faqList) ->
                    _homeState.value = HomeState.Success(map, faqList)
                },
                onFailure = { e ->
                    handleError(e)
                    _homeState.value = HomeState.Error
                }
            )
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        val message = when (throwable) {
            is NetworkException -> {
                resourceProvider.getString(R.string.error_network)
            }
            is ServerException -> {
                throwable.message ?: resourceProvider
                    .getString(R.string.error_server)
            }
            else -> {
                resourceProvider
                    .getString(R.string.error_unknown)
            }
        }

        _homeState.value = HomeState.Error
        _effect.emit(HomeEffect.ShowErrorDialog(message))
    }

    private fun navigateToServices() {
        navMain.goToServicesPage()
    }
}