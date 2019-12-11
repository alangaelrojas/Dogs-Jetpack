package com.apps.alan.dogs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.apps.alan.dogs.R
import com.apps.alan.dogs.databinding.ItemDogBinding
import com.apps.alan.dogs.model.DogBreed
import com.apps.alan.dogs.ui.DogClickListener
import com.apps.alan.dogs.ui.ListFragmentDirections
import kotlinx.android.synthetic.main.item_dog.view.*

class DogAdapter(c:Context) : RecyclerView.Adapter<DogAdapter.HolderDog>(), DogClickListener {

    private val context:Context = c
    private var listDogs:ArrayList<DogBreed> = ArrayList()

    fun addDogList(dogList: List<DogBreed>){
        listDogs.clear()
        listDogs.addAll(dogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDog {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)
        return HolderDog(view)
    }

    override fun onBindViewHolder(holder: HolderDog, position: Int) {
        holder.view.dog = listDogs[position]
        holder.view.listener = this
    }

    override fun getItemCount(): Int {
        return listDogs.size
    }

    override fun onDogClicked(v: View) {
        val uuid = v.dogId.text
        val action = ListFragmentDirections.actionListFragmentToDetailFragment()
        action.dogUuid = uuid.toString().toInt()
        Navigation.findNavController(v).navigate(action)
    }

    inner class HolderDog(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)
}