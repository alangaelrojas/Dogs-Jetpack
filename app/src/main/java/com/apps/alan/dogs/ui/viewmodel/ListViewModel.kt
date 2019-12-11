package com.apps.alan.dogs.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apps.alan.dogs.api.DogsApiService
import com.apps.alan.dogs.model.DogBreed
import com.apps.alan.dogs.model.DogDatabase
import com.apps.alan.dogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application){

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTIme = 5 * 60 * 1000 * 1000 * 1000L

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(){
        val updateTIme = prefHelper.getUpdateTime()
        if(updateTIme != null && updateTIme != 0L && System.nanoTime() - updateTIme < refreshTIme){
            fetchFromDatabase()
        }else {
            fetchFromRemote()
        }
    }

    private  fun fetchFromDatabase(){
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from db", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshByPassCache(){
        fetchFromRemote()
    }

    private fun fetchFromRemote(){
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)
                        Toast.makeText(getApplication(), "Dogs retrevieved from remote", Toast.LENGTH_SHORT).show()

                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogBreed>){
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(dogList: List<DogBreed>){
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteallDogs()
            val result = dao.insertAll(*dogList.toTypedArray())
            var i = 0
            while(i < dogList.size){
                dogList[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(dogList)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}