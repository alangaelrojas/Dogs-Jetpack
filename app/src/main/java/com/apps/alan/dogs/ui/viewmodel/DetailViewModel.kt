package com.apps.alan.dogs.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.alan.dogs.model.DogBreed
import com.apps.alan.dogs.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(uuid: Int){
        launch {
            val dog = DogDatabase(getApplication()).dogDao().getDog(uuid)
            dogRetrieved(dog)
        }
    }
    private fun dogRetrieved(dog: DogBreed) {
        dogLiveData.value = dog
    }
}