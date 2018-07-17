package com.legocatalog.di

import com.legocatalog.remote.rebrickable.SetInfoWorker
import com.legocatalog.ui.main.MainActivity
import com.legocatalog.ui.main.SetListFragment
import com.legocatalog.ui.set.NewSetActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ViewModelModule::class, ServiceModule::class))
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(newSetActivity: NewSetActivity)
    fun inject(worker: SetInfoWorker)
    fun inject(setListFragment: SetListFragment)
}