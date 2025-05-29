package com.example.productapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Product(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val categoria: String,
    val preco: Double
) : Parcelable
