package com.example.photos.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.photos.model.Product

class ProductAdapter(
    private val activityContext: Context,
    private val productList: MutableList<Product>
) : ArrayAdapter<Product>(activityContext, R.layout.simple_list_item_1, productList) {
    private data class ProductHolder(val productTitleTV: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val productview = convertView ?: LayoutInflater.from(activityContext)
            .inflate(R.layout.simple_list_item_1, parent, false).apply {
                tag = ProductHolder(findViewById(R.id.text1))
            }

        (productview.tag as ProductHolder).productTitleTV.text = productList[position].title

        return productview
    }
}