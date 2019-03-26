package fdt.recruitment.ui.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fdt.recruitment.R
import fdt.recruitment.ui.adapters.PhotoListAdapter
import fdt.recruitment.viewmodel.PhotoListViewModel
import kotlinx.android.synthetic.main.photo_list_activity.*


class PhotoListActivity : BaseActivity() {

    private val photoListViewModel by lazy {
        ViewModelProviders.of(this).get(PhotoListViewModel::class.java)
    }

    private val photoListAdapter by lazy { PhotoListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_list_activity)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL, false
        )
        recyclerView.adapter = photoListAdapter
        loadPhotosLocal()
        loadPhotosRemote()
    }

    private fun loadPhotosLocal() {
        photoListViewModel.getPhotoListLocal {
            if (it.isNotEmpty()) {
                progressBar.visibility = View.GONE
                photoListAdapter.photoList = it
            }
        }
    }

    private fun loadPhotosRemote() {
        photoListViewModel.getPhotoListRemote {
            progressBar.visibility = View.GONE
            showMessage(it.message)
            it.printStackTrace()
        }
    }

    override fun getToolbarTitle(): String {
        return getString(fdt.recruitment.R.string.photoList).toUpperCase()
    }
}
