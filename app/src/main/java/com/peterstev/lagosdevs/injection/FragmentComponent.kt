package com.peterstev.lagosdevs.injection

import android.app.Activity
import androidx.fragment.app.Fragment
import com.peterstev.data.injection.ApiModule
import com.peterstev.data.injection.DataModule
import com.peterstev.database.injection.DatabaseModule
import com.peterstev.database.injection.DbRepoModule
import com.peterstev.domain.injection.DomainModule
import com.peterstev.lagosdevs.fragment.DetailFragment
import com.peterstev.lagosdevs.fragment.FavouriteFragment
import com.peterstev.lagosdevs.fragment.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DomainModule::class,
        DatabaseModule::class,
        DbRepoModule::class,
        DataModule::class,
        ApiModule::class,
        AppModule::class,
    ]
)
interface FragmentComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity): FragmentComponent
    }

    fun inject(mainFragment: MainFragment)
    fun inject(favouriteFragment: FavouriteFragment)
    fun inject(detailFragment: DetailFragment)

    companion object {
        fun create(fragment: Fragment): FragmentComponent {
            return DaggerFragmentComponent.factory().create(fragment.requireActivity())
        }
    }
}
