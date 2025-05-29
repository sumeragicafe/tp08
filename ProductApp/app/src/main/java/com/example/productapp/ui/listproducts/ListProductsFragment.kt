package com.example.productapp.ui.listproducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productapp.Product
import com.example.productapp.ProductAdapter
import com.example.productapp.databinding.FragmentListProductsBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListProductsFragment : Fragment() {

    private var _binding: FragmentListProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = FirebaseFirestore.getInstance()

        setupRecyclerView()
        fetchProducts()

        return root
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(productList)
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }
    }

    private fun fetchProducts() {
        db.collection("Produtos")
            .get()
            .addOnSuccessListener { result ->
                productList.clear()
                for (document in result) {
                    val product = document.toObject(Product::class.java).copy(id = document.id)
                    productList.add(product)
                }
                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Erro ao buscar produtos: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
