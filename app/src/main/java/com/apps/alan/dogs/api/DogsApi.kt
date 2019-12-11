package com.apps.alan.dogs.api

import com.apps.alan.dogs.model.DogBreed
import io.reactivex.Single
import retrofit2.http.GET

interface DogsApi {

    @GET("devtides/dogsapi/master/dogs.json")
    fun getDogs() : Single<List<DogBreed>> //Single es el equivalente reactivo a Call

}