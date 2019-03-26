package fdt.recruitment.glide

class CustomImageSize(private val baseImageUrl: String?) : CustomImageSizeModel {

    override fun requestCustomSizeUrl(width: Int, height: Int): String {
        return "$baseImageUrl&w=$width"
    }
}