package fdt.galleryapp.ui.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import fdt.galleryapp.R
import fdt.galleryapp.constants.USER_FIRST_NAME
import fdt.galleryapp.constants.USER_LAST_NAME
import fdt.galleryapp.constants.USER_NAME
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.ui.adapters.UserPhotoListAdapter
import fdt.galleryapp.utils.getExtra
import fdt.galleryapp.viewmodel.UserPhotoListViewModel
import kotlinx.android.synthetic.main.user_photo_list_activity.*

class UserPhotoListActivity : BaseActivity() {
    private val userPhotoListAdapter by lazy { UserPhotoListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_photo_list_activity)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        recyclerView.adapter = userPhotoListAdapter
        loadUserPhotoList()
    }

    private fun loadUserPhotoList() {
        val model = ViewModelProviders.of(this).get(UserPhotoListViewModel::class.java)
        val userName = getExtra<String>(USER_NAME)
        if (!userName.isNullOrEmpty()) {
            model.getUserPhotoList(userName, ::updateUserPhotoList, ::loadUserPhotoListError)
        } else {
            showErrorMessage(getString(R.string.incorrect_user_name))
        }
    }

    private fun updateUserPhotoList(mutableList: MutableList<PhotoModel>) {
        if (!mutableList.isNullOrEmpty()) {
            userPhotoListAdapter.photoList = mutableList
        }
        progressBar.visibility = View.GONE
    }

    private fun loadUserPhotoListError(throwable: Throwable) {
        progressBar.visibility = View.GONE
        showErrorMessage(throwable.message)
        throwable.printStackTrace()
    }

    override fun getToolbarTitle(): String {
        return "FROM ${intent?.extras?.getString(USER_FIRST_NAME) ?: ""} ${intent?.extras?.getString(USER_LAST_NAME)
            ?: ""}"
            .toUpperCase()
    }
}