package de.dbaelz.demo.pnpstats.ui.feature.overview

import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor() :
    BaseViewModel<OverviewContract.Event, OverviewContract.State, OverviewContract.Effect>() {

    override fun provideInitialState() = OverviewContract.State("todo")

}