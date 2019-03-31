package fdt.galleryapp.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fdt.galleryapp.ui.activities.PhotoDetailsActivity
import fdt.galleryapp.ui.activities.PhotoListActivity
import fdt.galleryapp.ui.activities.UserPhotoListActivity

@Suppress("unused")
@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    internal abstract fun contributePhotoListActivityInjector(): PhotoListActivity

    @ContributesAndroidInjector
    internal abstract fun contributePhotoDetailsActivity(): PhotoDetailsActivity

    @ContributesAndroidInjector
    internal abstract fun contributeUserPhotoListActivity(): UserPhotoListActivity

}