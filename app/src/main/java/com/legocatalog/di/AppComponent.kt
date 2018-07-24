package com.legocatalog.di

import com.legocatalog.repository.remote.rebrickable.SetInfoWorker
import com.legocatalog.ui.main.MainActivity
import com.legocatalog.ui.main.setlist.SetListFragment
import com.legocatalog.ui.newset.NewSetActivity
import com.legocatalog.ui.partlist.PartListFragment
import com.legocatalog.ui.set.SetActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ViewModelModule::class, ServiceModule::class))
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(newSetActivity: NewSetActivity)
    fun inject(setActivity: SetActivity)

    fun inject(worker: SetInfoWorker)

    fun inject(setListFragment: SetListFragment)
    fun inject(partListFragment: PartListFragment)
}