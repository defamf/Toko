package com.example.apptoko0149

import com.example.apptoko0149.response.cart.Cart

interface CallbackInterface {
    fun passResultCallback(total:String,cart:ArrayList<Cart>)
}