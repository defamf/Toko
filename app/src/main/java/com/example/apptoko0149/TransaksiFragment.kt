package com.example.apptoko0149

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptoko0149.adapter.ProdukAdapter
import com.example.apptoko0149.adapter.TransaksiAdapter
import com.example.apptoko0149.api.BaseRetrofit
import com.example.apptoko0149.response.cart.Cart
import com.example.apptoko0149.response.produk.ProdukResponse
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class TransaksiFragment : Fragment() {

    private val api by lazy { BaseRetrofit().endpoint }
    private lateinit var my_cart : ArrayList<Cart>
    private lateinit var total_bayar : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view = inflater.inflate(R.layout.fragment_transaksi, container, false)
        getProduk(view)

        val btnBayar = view.findViewById<Button>(R.id.btnBayar)
        btnBayar.setOnClickListener {

            val bundle = Bundle()
            bundle.putParcelableArrayList("MY_CART", my_cart)
            bundle.putString("TOTAL", total_bayar)
            findNavController().navigate(R.id.bayarFragment,bundle)
        }

        return view
    }

    fun getProduk(view: View) {
        val token = LoginActivity.sessionManager.getString("TOKEN")

        api.getProduk(token.toString()).enqueue(object : Callback<ProdukResponse> {
            override fun onResponse(
                call: Call<ProdukResponse>,
                response: Response<ProdukResponse>
            ) {
                Log.d("Produk data", response.body().toString())

                val rv = view.findViewById<RecyclerView>(R.id.rv_transaksi)


                rv.setHasFixedSize(true)
                rv.layoutManager = LinearLayoutManager(activity)
                val rvAdapter = TransaksiAdapter(response.body()!!.data.produk)
                rv.adapter = rvAdapter

                rvAdapter.callbackInterface = object : CallbackInterface{
                    override fun passResultCallback(total: String, cart: ArrayList<Cart>) {
                        val txtTotalBayar = activity?.findViewById<TextView>(R.id.txtTotalBayar)

                        val localeID = Locale("in", "ID")
                        val numberFormat = NumberFormat.getCurrencyInstance(localeID)


                        txtTotalBayar?.setText(numberFormat.format(total.toDouble()).toString())

                        total_bayar = total
                        my_cart = cart

                        Log.d("My cart", cart.toString())
                    }

                }
            }

            override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                Log.e("Produk error", t.toString())
            }
        })
    }
    }