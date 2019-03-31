package fdt.galleryapp.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.android.AndroidInjection
import fdt.galleryapp.R
import fdt.galleryapp.constants.USER_FIRST_NAME
import fdt.galleryapp.constants.USER_LAST_NAME
import fdt.galleryapp.constants.USER_NAME
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.models.decorators.UserPhotoListItemModel
import fdt.galleryapp.ui.adapters.UserPhotoListAdapter
import fdt.galleryapp.utils.getExtra
import fdt.galleryapp.viewmodel.UserPhotoListViewModel
import kotlinx.android.synthetic.main.user_photo_list_activity.*
import javax.inject.Inject

class UserPhotoListActivity : BaseActivity() {
    private val userPhotoListAdapter by lazy { UserPhotoListAdapter(this) }

    @Inject
    lateinit var userPhotoListViewModel: UserPhotoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.user_photo_list_activity)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        recyclerView.adapter = userPhotoListAdapter
        getUserPhotoList()
    }

    private fun getUserPhotoList() {
        val userName = getExtra<String>(USER_NAME)
        if (!userName.isNullOrEmpty()) {
            userPhotoListViewModel.getUserPhotoListFromServer(
                userName, ::onPublishUserPhotoList,
                ::onErrorLoadingUserPhotoList
            )
        } else {
            showErrorMessage(getString(R.string.incorrectUserName))
        }
    }

    private fun onPublishUserPhotoList(list: List<PhotoModel>) {
        if (!list.isNullOrEmpty()) {
            userPhotoListAdapter.photoList = list.map { UserPhotoListItemModel(it) }
        }
        progressBar.visibility = View.GONE
    }

    private fun onErrorLoadingUserPhotoList(throwable: Throwable) {
        progressBar.visibility = View.GONE
        showErrorMessage(throwable.message)
        throwable.printStackTrace()
    }

    override fun getToolbarTitle(): String {
        val firstName = getExtra<String>(USER_FIRST_NAME) ?: ""
        val lastName = getExtra<String>(USER_LAST_NAME) ?: ""
        return getString(R.string.from) + " " + firstName.toUpperCase() + " " + lastName.toUpperCase()
    }
}