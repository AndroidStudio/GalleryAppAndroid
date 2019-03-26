package fdt.recruitment.glide

import com.bumptech.glide.load.model.*
import java.io.InputStream

class CustomImageSizeUrlLoaderFactory : ModelLoaderFactory<CustomImageSizeModel, InputStream> {
    private val modelCache = ModelCache<CustomImageSizeModel, GlideUrl>(500)

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<CustomImageSizeModel, InputStream> {
        val modelLoader = multiFactory.build<GlideUrl, InputStream>(GlideUrl::class.java, InputStream::class.java)
        return CustomImageSizeUrlLoader(modelLoader, modelCache)
    }

    override fun teardown() {

    }
}