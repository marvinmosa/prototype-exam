package com.prototype.exam.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prototype.exam.ui.main.viewModel.ForecastDetailViewModel
import com.prototype.exam.ui.main.viewModel.ForecastViewModel
import com.prototype.exam.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel::class)
    internal abstract fun bindForecastViewModel(viewModel: ForecastViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForecastDetailViewModel::class)
    internal abstract fun bindForecastDetailViewModel(viewModel: ForecastDetailViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}