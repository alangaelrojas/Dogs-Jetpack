package com.apps.alan.dogs.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.alan.dogs.R
import com.apps.alan.dogs.adapter.DogAdapter
import com.apps.alan.dogs.ui.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var viewMdoel: ListViewModel
    private var dogAdapter:DogAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        dogAdapter = DogAdapter(context!!)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewMdoel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewMdoel.refresh()

        rvList_dogs.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = dogAdapter
        }

        refresh_layout.setOnRefreshListener {
            rvList_dogs.visibility = View.GONE
            error_load_list.visibility = View.GONE
            pgb_loading_list.visibility = View.VISIBLE
            viewMdoel.refreshByPassCache()
            refresh_layout.isRefreshing = false
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewMdoel.dogs.observe(this, Observer { dogs ->
            dogs?.let{
                rvList_dogs.visibility = View.VISIBLE
                dogAdapter!!.addDogList(dogs)
            }
        })
        viewMdoel.dogsLoadError.observe(this, Observer{ isError ->
            isError?.let{
                if(it){
                    error_load_list.visibility = View.VISIBLE
                }else{
                    error_load_list.visibility = View.GONE
                    rvList_dogs.visibility = View.VISIBLE
                }
            }
        })
        viewMdoel.loading.observe(this, Observer{ isLoading ->
            isLoading?.let{
                pgb_loading_list.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    error_load_list.visibility = View.GONE
                    rvList_dogs.visibility = View.GONE
                }
            }
        })
    }
}
