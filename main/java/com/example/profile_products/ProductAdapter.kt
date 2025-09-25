package com.example.zadanie2004

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var products: MutableList<Product>,
    private val onDeleteClick: (Product) -> Unit,
    private val onEditClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvProductQuantity)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvName.text = product.name
        holder.tvQuantity.text = "Количество: ${product.quantity}"

        holder.btnDelete.setOnClickListener {
            onDeleteClick(product)
        }

        holder.itemView.setOnLongClickListener {
            onEditClick(product)
            true
        }
    }

    override fun getItemCount(): Int = products.size

    fun addProduct(product: Product) {
        products.add(product)
        notifyItemInserted(products.size - 1) // Уведомляем о добавлении нового элемента
    }

    fun updateProduct(position: Int, product: Product) {
        products[position] = product
        notifyItemChanged(position) // Уведомляем об изменении элемента
    }

    fun removeProduct(product: Product) {
        val position = products.indexOfFirst { it.id == product.id }
        if (position != -1) {
            products.removeAt(position)
            notifyItemRemoved(position) // Уведомляем об удалении элемента
        }
    }

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged() // Уведомляем о полном обновлении списка
    }
}