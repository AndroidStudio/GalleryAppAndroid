package fdt.galleryapp.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.AndroidInjection
import fdt.galleryapp.R
import fdt.galleryapp.models.PhotoListItemModel
import fdt.galleryapp.ui.adapters.PhotoListAdapter
import fdt.galleryapp.viewmodel.PhotoListViewModel
import kotlinx.android.synthetic.main.photo_list_activity.*
import javax.inject.Inject

class PhotoListActivity : BaseActivity() {

    private val photoListAdapter by lazy { PhotoListAdapter(this) }

    @Inject
    lateinit var photoListViewModel: PhotoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.photo_list_activity)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL, false
        )
        recyclerView.adapter = photoListAdapter
        loadPhotosList()
    }

    private fun loadPhotosList() {
        photoListViewModel.getPhotoList(::onPublishPhotoLost, ::onErrorLoadingPhotoList)
    }

    private fun onPublishPhotoLost(list: List<PhotoListItemModel>) {
        progressBar.visibility = View.GONE
        photoListAdapter.photoList = list
    }

    private fun onErrorLoadingPhotoList(throwable: Throwable) {
        progressBar.visibility = View.GONE
        showErrorMessage(throwable.message)
        throwable.printStackTrace()
    }

    override fun getToolbarTitle(): String {
        return getString(fdt.galleryapp.R.string.photoList).toUpperCase()
    }

    override fun inBackArrowEnabled(): Boolean {
        return false
    }
}
