package dev.samuelmcmurray.data.repository

import androidx.lifecycle.MutableLiveData
import dev.samuelmcmurray.data.dao.ImageDAO
import dev.samuelmcmurray.ui.image.Image


class ImageRepository (private val imageDAO: ImageDAO) {

    var imageLiveData: MutableLiveData<Image>
    val readAllImages: List<Image?>? = imageDAO.getAllImage()

    init {
        imageLiveData = MutableLiveData()
    }

    fun addImage(image: Image) {
        imageDAO.insert(image)
    }

    fun removeImageFullId(fullId: String) {
        imageDAO.deleteImageByFullId(fullId)
    }

    fun removeImageById(imageId: Int) {
        imageDAO.deleteImageById(imageId)
    }

    fun getImageId(imageId: Int) {
        imageLiveData.value = imageDAO.getImageByImageId(imageId )
    }

    fun getByFullId(fullId: String) {
        imageLiveData.value = imageDAO.getImageByFullId(fullId)
    }
}