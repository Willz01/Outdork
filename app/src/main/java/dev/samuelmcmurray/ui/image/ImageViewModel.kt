package dev.samuelmcmurray.ui.image

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import dev.samuelmcmurray.data.database.ImageDatabase
import dev.samuelmcmurray.data.repository.ImageRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel(application: Application): AndroidViewModel(application) {
    var readAllImages: List<Image?>
    private val repository: ImageRepository
    var imageLiveData: MutableLiveData<Image>

    init {
        val imageDao = ImageDatabase.getDatabase(application).imageDao()
        repository = ImageRepository(imageDao!!)
        readAllImages = readAllImages()
        imageLiveData = repository.imageLiveData
    }

    fun readAllImages(): List<Image?> {
        CoroutineScope(Dispatchers.IO).launch {
            readAllImages = repository.readAllImages!!
        }
        return readAllImages
    }

    fun addImage(image: Image) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.addImage(image)
        }
    }

    fun removeImageByFullId(fullId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.removeImageFullId(fullId)
        }
    }

    fun removeImageById(imageId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.removeImageById(imageId)
        }
    }

    fun getImageById(imageId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getImageId(imageId)
        }
    }

    fun getImageByFullId(fullId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getByFullId(fullId)
        }
    }
}