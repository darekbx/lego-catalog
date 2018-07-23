package com.legocatalog.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.legocatalog.ui.main.MainViewModel
import com.legocatalog.ui.main.setlist.SetListViewModel
import com.legocatalog.ui.newset.NewSetViewModel
import com.legocatalog.ui.set.SetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(NewSetViewModel::class)
    internal abstract fun bindNewSetViewModel(viewModel: NewSetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(SetListViewModel::class)
    internal abstract fun bindSetListViewModel(viewModel: SetListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(SetViewModel::class)
    internal abstract fun bindSetViewModel(viewModel: SetViewModel): ViewModel
}