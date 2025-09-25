package com.example.zadanie2004

import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class ShoppingListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var products = mutableListOf<Product>()
    private var nextId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        recyclerView = findViewById(R.id.recyclerView)
        btnAdd = findViewById(R.id.btnAdd)
        btnBack = findViewById(R.id.btnBack)

        sharedPreferences = getSharedPreferences("shopping_list", MODE_PRIVATE)
        loadProducts()

        adapter = ProductAdapter(
            products = products,
            onDeleteClick = { product ->
                deleteProduct(product)
            },
            onEditClick = { product ->
                editProduct(product)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            showAddProductDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showAddProductDialog(productToEdit: Product? = null) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)
        val etName = dialogView.findViewById<EditText>(R.id.etProductName)
        val etQuantity = dialogView.findViewById<EditText>(R.id.etProductQuantity)

        val title = if (productToEdit == null) "Добавить товар" else "Редактировать товар"

        if (productToEdit != null) {
            etName.setText(productToEdit.name)
            etQuantity.setText(productToEdit.quantity.toString())
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Сохранить") { dialog, _ ->
                val name = etName.text.toString()
                val quantity = etQuantity.text.toString().toIntOrNull() ?: 1

                if (name.isNotEmpty()) {
                    if (productToEdit == null) {
                        addProduct(Product(nextId++, name, quantity))
                    } else {
                        updateProduct(productToEdit.copy(name = name, quantity = quantity))
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun addProduct(product: Product) {
        adapter.addProduct(product) // Используем метод адаптера для добавления
        saveProducts()
    }

    private fun updateProduct(product: Product) {
        val position = products.indexOfFirst { it.id == product.id }
        if (position != -1) {
            adapter.updateProduct(position, product) // Используем метод адаптера для обновления
            saveProducts()
        }
    }

    private fun deleteProduct(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Удаление")
            .setMessage("Вы уверены, что хотите удалить ${product.name}?")
            .setPositiveButton("Да") { dialog, _ ->
                adapter.removeProduct(product) // Используем метод адаптера для удаления
                saveProducts()
                dialog.dismiss()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun editProduct(product: Product) {
        showAddProductDialog(product)
    }

    private fun saveProducts() {
        val jsonArray = JSONArray()
        products.forEach { product ->
            val jsonObject = JSONObject()
            jsonObject.put("id", product.id)
            jsonObject.put("name", product.name)
            jsonObject.put("quantity", product.quantity)
            jsonArray.put(jsonObject)
        }

        sharedPreferences.edit().apply {
            putString("products", jsonArray.toString())

            // Сохраняем nextId
            val maxId = products.maxByOrNull { it.id }?.id ?: 0
            putInt("nextId", maxId + 1)
            apply()
        }
    }

    private fun loadProducts() {
        val jsonString = sharedPreferences.getString("products", null)
        if (!jsonString.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(jsonString)
                products.clear()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val product = Product(
                        id = jsonObject.getInt("id"),
                        name = jsonObject.getString("name"),
                        quantity = jsonObject.getInt("quantity")
                    )
                    products.add(product)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                products.clear()
            }
        }

        nextId = sharedPreferences.getInt("nextId", 1)
    }
}