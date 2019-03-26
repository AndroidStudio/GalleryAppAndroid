package fdt.recruitment.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import fdt.recruitment.glide.CustomImageSizeModel
import fdt.recruitment.glide.CustomImageSizeUrlLoaderFactory
import fdt.recruitment.webservice.OkHttpClientBuilder
import java.io.InputStream

@GlideModule
class GlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(CustomImageSizeModel::class.java, InputStream::class.java, CustomImageSizeUrlLoaderFactory())
        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(OkHttpClientBuilder.getOkHttpBuilder().build())
        )
    }
}