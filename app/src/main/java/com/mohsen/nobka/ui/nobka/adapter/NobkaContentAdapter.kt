package com.mohsen.nobka.ui.nobka.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.nobka.databinding.ContentItemRecyclerBinding
import com.mohsen.nobka.domain.model.NobkaMoviesModel
import java.util.Collections
import javax.inject.Inject

class NobkaContentAdapter @Inject constructor() :
    RecyclerView.Adapter<NobkaContentAdapter.ViewHolder>() {

    private var dataList: List<NobkaMoviesModel> = Collections.emptyList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemBinding =
            ContentItemRecyclerBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return ViewHolder(itemBinding.root)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(dataList[p1])
    }

    override fun getItemCount(): Int = dataList.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ContentItemRecyclerBinding.bind(view)
        fun bind(data: NobkaMoviesModel) {
            with(binding) {
                title.text = data.title
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: List<NobkaMoviesModel>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
}