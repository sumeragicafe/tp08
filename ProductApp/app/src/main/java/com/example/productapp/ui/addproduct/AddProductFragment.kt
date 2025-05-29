package com.example.productapp.ui.addproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.productapp.Product
import com.example.productapp.databinding.FragmentAddProductBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = FirebaseFirestore.getInstance()

        binding.buttonAddProduct.setOnClickListener {
            val nome = binding.editTextProductName.text.toString()
            val categoria = binding.editTextProductCategory.text.toString()
            val precoText = binding.editTextProductPrice.text.toString()

            if (nome.isNotEmpty() && categoria.isNotEmpty() && precoText.isNotEmpty()) {
                val preco = precoText.toDoubleOrNull()
                if (preco != null) {
                    val product = Product(nome = nome, categoria = categoria, preco = preco)
                    addProductToFirestore(product)
                } else {
                    Toast.makeText(context, "Preço inválido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun addProductToFirestore(product: Product) {
        db.collection("Produtos")
            .add(product)
            .addOnSuccessListener {
                Toast.makeText(context, "Produto adicionado!", Toast.LENGTH_SHORT).show()
                binding.editTextProductName.text.clear()
                binding.editTextProductCategory.text.clear()
                binding.editTextProductPrice.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao adicionar produto: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
