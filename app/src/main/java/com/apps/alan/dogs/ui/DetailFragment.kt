package com.apps.alan.dogs.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.apps.alan.dogs.R
import com.apps.alan.dogs.databinding.FragmentDetailBinding
import com.apps.alan.dogs.ui.viewmodel.DetailViewModel
import com.apps.alan.dogs.util.getProgressDrawable
import com.apps.alan.dogs.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private lateinit var viewModel : DetailViewModel
    private var dogUuid = 0

    private lateinit var dataBinding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        observerViewModel()

        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }

        if(dogUuid != 0){
            viewModel.fetch(dogUuid)
        }
    }

    private fun observerViewModel() {
        viewModel.dogLiveData.observe(this, Observer{ dog ->
            dog?.let {
                dataBinding.dogDetail = dog
            }
        })
    }
}
