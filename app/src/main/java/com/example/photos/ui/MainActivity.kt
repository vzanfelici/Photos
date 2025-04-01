package com.example.photos.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.ImageRequest
import com.example.photos.R
import com.example.photos.adapter.ProductAdapter
import com.example.photos.adapter.ProductImageAdapter
import com.example.photos.databinding.ActivityMainBinding
import com.example.photos.model.DummyJSONAPI
import com.example.photos.model.Product

class MainActivity : AppCompatActivity() {
    //    private val amb: ActivityMainBinding by lazy {
    //        ActivityMainBinding.inflate(layoutInflater)
    //    }
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val productList: MutableList<Product> = mutableListOf()
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(this, productList)
    }
    private val productImageList: MutableList<Bitmap> = mutableListOf()
    private val productImageAdapter: ProductImageAdapter by lazy {
        ProductImageAdapter(this, productImageList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = getString(R.string.app_name)
        })

        amb.productsSp.apply {
            adapter = productAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val size = productImageList.size
                    productImageList.clear()
                    productImageAdapter.notifyItemRangeChanged(0, size)
                    retrieveProductImages(productList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // NSA
                }
            }

        }
        amb.productImagesRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productImageAdapter
        }

        retrieveProducts()
    }

    private fun retrieveProducts() =
        DummyJSONAPI.ProductListRequest({ productList ->
            productList.products.also {
                productAdapter.addAll(it)
            }
        }, {
            Toast.makeText(
                this,
                getString(R.string.request_problem),
                Toast.LENGTH_SHORT
            ).show()
        }).also {
            DummyJSONAPI.getInstance(this).addToRequestQueue(it)
        }

    //    Primeira implementação com httpURLConnection
    //    private fun retrieveProducts() = Thread {
    //        val productsConnection = URL(PRODUCTS_ENDPOINT).openConnection() as HttpURLConnection
    //        try {
    //            if (productsConnection.responseCode == HTTP_OK) {
    //                InputStreamReader(productsConnection.inputStream).readText().let {
    //                    runOnUiThread {
    //                        productAdapter.addAll(Gson().fromJson(it, ProductList::class.java).products)
    //                    }
    //                }
    //            } else {
    //                runOnUiThread {
    //                    Toast.makeText(
    //                        this,
    //                        getString(R.string.request_problem),
    //                        Toast.LENGTH_SHORT
    //                    ).show()
    //                }
    //            }
    //        } catch (ioe: IOException) {
    //            runOnUiThread {
    //                Toast.makeText(
    //                    this,
    //                    getString(R.string.connection_failed),
    //                    Toast.LENGTH_SHORT
    //                ).show()
    //            }
    //        } catch (jse: JsonSyntaxException) {
    //            runOnUiThread {
    //                Toast.makeText(
    //                    this,
    //                    getString(R.string.response_problem),
    //                    Toast.LENGTH_SHORT
    //                ).show()
    //            }
    //        } finally {
    //            productsConnection.disconnect()
    //        }
    //    }.start()


    private fun retrieveProductImages(product: Product) = product.images.forEach { imageUrl ->
        ImageRequest(
            imageUrl,
            { response ->
                productImageList.add(response)
                productImageAdapter.notifyItemInserted(productImageList.lastIndex)
            },
            0,
            0,
            ImageView.ScaleType.CENTER,
            Bitmap.Config.ARGB_8888,
            {
                Toast.makeText(
                    this,
                    getString(R.string.request_problem),
                    Toast.LENGTH_SHORT
                ).show()
            }).also {
            DummyJSONAPI.getInstance(this).addToRequestQueue(it)
        }
    }
    //    Primeira implementação com httpURLConnection
    //    private fun retrieveProductImages(product: Product) = Thread {
    //        product.images.forEach { imageUrl ->
    //            val imageConnection = URL(imageUrl).openConnection() as HttpURLConnection
    //            try {
    //                if (imageConnection.responseCode == HTTP_OK) {
    //                    BufferedInputStream(imageConnection.inputStream).let {
    //                        val imageBitmap = BitmapFactory.decodeStream(it)
    //                        runOnUiThread {
    //                            productImageList.add(imageBitmap)
    //                            productImageAdapter.notifyItemInserted(productImageList.lastIndex)
    //                        }
    //                    }
    //                } else {
    //                    runOnUiThread {
    //                        Toast.makeText(
    //                            this,
    //                            getString(R.string.request_problem),
    //                            Toast.LENGTH_SHORT
    //                        ).show()
    //                    }
    //                }
    //            } catch (ioe: IOException) {
    //                runOnUiThread {
    //                    Toast.makeText(
    //                        this,
    //                        getString(R.string.connection_failed),
    //                        Toast.LENGTH_SHORT
    //                    ).show()
    //                }
    //            } finally {
    //                imageConnection.disconnect()
    //            }
    //        }
    //    }.start()
}