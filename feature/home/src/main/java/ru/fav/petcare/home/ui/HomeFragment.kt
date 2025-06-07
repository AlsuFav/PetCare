package ru.fav.petcare.home.ui

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.domain.model.FaqModel
import ru.fav.petcare.domain.model.MapModel
import ru.fav.petcare.home.databinding.FragmentHomeBinding
import ru.fav.petcare.home.R
import ru.fav.petcare.home.ui.adapter.FaqAdapter
import ru.fav.petcare.home.ui.state.HomeEffect
import ru.fav.petcare.home.ui.state.HomeEvent
import ru.fav.petcare.home.ui.state.HomeState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by viewModels()

    private var rvAdapter: FaqAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()

        homeViewModel.reduce(HomeEvent.GetData)
    }

    private fun initViews() = with(viewBinding) {
        setupFaqRecyclerView()

        this.servicesCard.setOnClickListener {
            homeViewModel.reduce(HomeEvent.OnServicesClicked)
        }
    }

    private fun observeViewModel() {
        homeViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is HomeEffect.ShowErrorDialog -> showErrorDialog(state.message)
            }
        }

        homeViewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Initial -> {
                    showLoading(false)
                    showFields(false)
                }
                is HomeState.Loading -> {
                    showLoading(true)
                    showFields(false)
                }
                is HomeState.Success -> {
                    setupYandexMap(state.map)
                    rvAdapter?.updateData(state.faqList.toMutableList())
                    showLoading(false)
                    showFields(true)
                }
                is HomeState.Error -> {
                    showLoading(false)
                    showFields(false)
                }
            }
        }
    }

    private fun setupYandexMap(mapData: MapModel) {
        val salonLocation = Point(mapData.latitude, mapData.longitude)

        viewBinding.tvAddress.text = mapData.address

        viewBinding.mapView.map.move(
            CameraPosition(salonLocation, 15.0f, 150.0f, 30.0f)
        )

        val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.ic_marker)
        val placemark = viewBinding.mapView.map.mapObjects.addPlacemark().apply {
            geometry = salonLocation
            setIcon(imageProvider)
            setIconStyle(IconStyle()
                .setScale(0.1f)
                .setAnchor(PointF(0.5f, 1.0f))
            )
        }

        placemark.addTapListener { _, _ ->
            true
        }
    }

    private fun setupFaqRecyclerView() = with(viewBinding) {
        rvAdapter = FaqAdapter()
        faqRecyclerView.adapter = rvAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showFields(show: Boolean) {
        viewBinding.apply {
            addressTitle.visibility = if (show) View.VISIBLE else View.GONE
            mapCardView.visibility = if (show) View.VISIBLE else View.GONE
            addressCard.visibility = if (show) View.VISIBLE else View.GONE
            tvFaqTitle.visibility = if (show) View.VISIBLE else View.GONE
            faqRecyclerView.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        viewBinding.mapView.onStart()
    }

    override fun onStop() {
        viewBinding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}