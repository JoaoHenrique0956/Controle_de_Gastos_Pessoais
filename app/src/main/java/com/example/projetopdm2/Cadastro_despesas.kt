package com.example.projetopdm2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Cadastro_despesas : AppCompatActivity() {

    private lateinit var repository: GastosRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_despesas)

        repository = GastosRepository()

        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        val etValor = findViewById<EditText>(R.id.etValor)
        val spCategoria = findViewById<Spinner>(R.id.spCategoria)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        val categorias = listOf(
            "Alimentação",
            "Transporte",
            "Lazer",
            "Saúde",
            "Outros"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categorias
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = adapter

        btnSalvar.setOnClickListener {
            val descricao = etDescricao.text.toString().trim()

            if (descricao.isEmpty()) {
                etDescricao.error = "Digite uma descrição"
                return@setOnClickListener
            }

            val valorTexto = etValor.text.toString().trim()

            if (valorTexto.isEmpty()) {
                etValor.error = "Digite um valor"
                return@setOnClickListener
            }

            val valor = valorTexto.replace(",", ".").toDoubleOrNull()

            if (valor == null) {
                etValor.error = "Digite um valor válido"
                return@setOnClickListener
            }

            repository.salvarDespesa(
                descricao,
                valor,
                spCategoria.selectedItem.toString()
            )

            finish()
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }
}
