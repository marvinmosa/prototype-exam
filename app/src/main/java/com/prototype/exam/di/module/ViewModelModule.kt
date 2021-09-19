package com.prototype.exam.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prototype.exam.ui.main.viewModel.UserDetailViewModel
import com.prototype.exam.ui.main.viewModel.UserViewModel
import com.prototype.exam.utils.ViewModelFactory
import com.prototype.exam.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    internal abstract fun bindForecastViewModel(viewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailViewModel::class)
    internal abstract fun bindForecastDetailViewModel(viewModel: UserDetailViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}