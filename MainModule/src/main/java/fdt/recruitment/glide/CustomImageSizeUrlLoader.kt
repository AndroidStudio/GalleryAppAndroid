package fdt.recruitment.glide

import androidx.annotation.Nullable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import java.io.InputStream

internal class CustomImageSizeUrlLoader(
    concreteLoader: ModelLoader<GlideUrl, InputStream>,
    @Nullable modelCache: ModelCache<CustomImageSizeModel, GlideUrl>
) :
    BaseGlideUrlLoader<CustomImageSizeModel>(concreteLoader, modelCache) {

    override fun getUrl(model: CustomImageSizeModel, width: Int, height: Int, options: Options): String {
        return model.requestCustomSizeUrl(width, height)
    }

    override fun handles(customImageSizeModel: CustomImageSizeModel): Boolean {
        return true
    }
}