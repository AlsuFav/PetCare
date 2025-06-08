package ru.fav.petcare.home.ui.state
import ru.fav.petcare.domain.model.FaqModel
import ru.fav.petcare.domain.model.MapModel

sealed class HomeState {
    object Initial : HomeState()
    object Loading : HomeState()
    data class Success(val map: MapModel, val faqList: List<FaqModel>) : HomeState()
    object Error : HomeState()
}
