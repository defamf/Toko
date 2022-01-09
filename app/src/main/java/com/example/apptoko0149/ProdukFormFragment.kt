package com.example.apptoko0149

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.apptoko0149.api.BaseRetrofit
import com.example.apptoko0149.response.login.LoginResponse
import com.example.apptoko0149.response.produk.Produk
import com.example.apptoko0149.response.produk.ProdukResponsePost
import com.google.android.material.textfield.TextInputLayout

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProdukFormFragment : Fragment() {

    private val api by lazy { BaseRetrofit().endpoint }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_produk_form, container, false)
        val btnProses = view.findViewById<Button>(R.id.btnProses)

        val txtFormNama = view.findViewById<TextView>(R.id.txtFormNama)
        val txtFormHarga = view.findViewById<TextView>(R.id.txtFormHarga)
        val txtFormStok = view.findViewById<TextView>(R.id.txtFormStok)

        val status = arguments?.getString("status")
        val produk = arguments?.getParcelable<Produk>("produk")

        if(status=="edit"){
            txtFormNama.setText(produk?.nama.toString())
            txtFormHarga.setText(produk?.harga.toString())
            txtFormStok.setText(produk?.stok.toString())
        }

        Log.d("produkform", produk.toString())

        btnProses.setOnClickListener {
            val txtFormNama = view.findViewById<TextInputLayout>(R.id.txtFormNama)
            val txtFormStok = view.findViewById<TextInputLayout>(R.id.txtFormStok)
            val txtFormHarga = view.findViewById<TextInputLayout>(R.id.txtFormHarga)

            val token = LoginActivity.sessionManager.getString("TOKEN")
            val adminId = LoginActivity.sessionManager.getString("ADMIN_ID")

            if(status=="edit"){
                api.putProduk(
                    token.toString(),
                    produk?.id.toString().toInt(),
                    adminId.toString().toInt(),
                    txtFormNama.textAlignment.toString(),
                    txtFormHarga.textAlignment.toString().toInt(),
                    txtFormStok.textAlignment.toString().toInt()
                ).enqueue(object :
                    Callback<ProdukResponsePost> {
                    override fun onResponse(
                        call: Call<ProdukResponsePost>,
                        response: Response<ProdukResponsePost>
                    ) {
                        Log.d("ResponData", response.body()!!.data.toString())
                        Toast.makeText(
                            activity?.applicationContext,
                            "Data" + response.body()!!.data.produk.nama.toString() + " di edit",
                            Toast.LENGTH_LONG
                        ).show()

                        findNavController().navigate(R.id.produkFragment)
                    }

                    override fun onFailure(call: Call<ProdukResponsePost>, t: Throwable) {
                        Log.e("Error", t.toString())
                    }

                })

            }else {


                api.postProduk(
                    token.toString(),
                    adminId.toString().toInt(),
                    txtFormNama.textAlignment.toString(),
                    txtFormHarga.textAlignment.toString().toInt(),
                    txtFormStok.textAlignment.toString().toInt()
                ).enqueue(object :
                    Callback<ProdukResponsePost> {
                    override fun onResponse(
                        call: Call<ProdukResponsePost>,
                        response: Response<ProdukResponsePost>
                    ) {
                        Log.d("Data", response.toString())
                        Toast.makeText(
                            activity?.applicationContext,
                            "Data di input",
                            Toast.LENGTH_LONG
                        ).show()

                        findNavController().navigate(R.id.produkFragment)
                    }

                    override fun onFailure(call: Call<ProdukResponsePost>, t: Throwable) {
                        Log.e("Error", t.toString())
                    }

                })
            }
        }
        return view
    }

}