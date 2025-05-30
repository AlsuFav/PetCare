package ru.fav.petcare.appointment.add.timeslot.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.androidbroadcast.vbpd.viewBinding
import ru.fav.petcare.appointment.add.timeslot.databinding.FragmentSelectTimeslotBinding
import ru.fav.petcare.appointment.add.timeslot.R
import ru.fav.petcare.appointment.add.timeslot.ui.adapter.DatesAdapter
import ru.fav.petcare.appointment.add.timeslot.ui.adapter.DatesShimmerAdapter
import ru.fav.petcare.appointment.add.timeslot.ui.state.SelectTimeSlotEffect
import ru.fav.petcare.appointment.add.timeslot.ui.state.SelectTimeSlotEvent
import ru.fav.petcare.appointment.add.timeslot.ui.state.SelectTimeSlotState
import ru.fav.petcare.util.extension.observe
import ru.fav.petcare.util.extension.observeNotSuspend
import ru.fav.petcare.util.extension.showErrorDialog
import kotlin.getValue

@AndroidEntryPoint
class SelectTimeSlotFragment: Fragment(R.layout.fragment_select_timeslot) {

    private val viewBinding: FragmentSelectTimeslotBinding by viewBinding(FragmentSelectTimeslotBinding::bind)
    private val args: SelectTimeSlotFragmentArgs by navArgs()
    private val selectTimeSlotViewModel: SelectTimeSlotViewModel by viewModels()

    private var rvAdapter: DatesAdapter? = null
    private var rvShimmerAdapter: DatesShimmerAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()

        selectTimeSlotViewModel.reduce(event = SelectTimeSlotEvent.GetAllTimeSlots)
    }

    private fun initViews() = with(viewBinding) {
        setupRecyclerViews()

        this.buttonCancel.setOnClickListener {
            selectTimeSlotViewModel.reduce(SelectTimeSlotEvent.OnCancelClicked)
        }

        this.buttonBack.setOnClickListener {
            selectTimeSlotViewModel.reduce(SelectTimeSlotEvent.OnBackClicked)
        }
    }

    private fun observeViewModel() {
        selectTimeSlotViewModel.effect.observeNotSuspend(viewLifecycleOwner) { state ->
            when (state) {
                is SelectTimeSlotEffect.ShowErrorDialog -> showErrorDialog(state.message)
            }
        }

        selectTimeSlotViewModel.selectTimeSlotState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SelectTimeSlotState.Loading -> {
                    hideErrorField()
                    showLoading(true)
                }
                is SelectTimeSlotState.Success -> {
                    showLoading(false)
                    hideErrorField()
                    rvAdapter?.updateData(state.timeslots.toMutableList())
                }

                is SelectTimeSlotState.Error.FieldError -> {
                    showLoading(false)
                    showErrorField(state.message)
                }
                is SelectTimeSlotState.Error.GlobalError -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        rvAdapter = DatesAdapter { timeSlot ->
            selectTimeSlotViewModel.reduce(SelectTimeSlotEvent.OnTimeSlotClicked(
                petId = args.petId,
                serviceId = args.serviceId,
                timeSlotId = timeSlot.id
            ))
        }
        viewBinding.recyclerViewDates.adapter = rvAdapter

        rvShimmerAdapter = DatesShimmerAdapter()
        viewBinding.recyclerViewShimmer.adapter = rvShimmerAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        viewBinding.apply {
            if (isLoading) {
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
                recyclerViewDates.visibility = View.GONE
            } else {
                shimmerLayout.visibility = View.GONE
                shimmerLayout.stopShimmer()
                recyclerViewDates.visibility = View.VISIBLE
            }
        }
    }

    private fun showErrorField(message: String) {
        viewBinding.textError.apply {
            text = message
            visibility = View.VISIBLE
        }

        viewBinding.textViewSelectTimeSlot.visibility = View.GONE
    }

    private fun hideErrorField() {
        viewBinding.textError.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.shimmerLayout.stopShimmer()
    }
}