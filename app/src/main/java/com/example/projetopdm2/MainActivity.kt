package com.example.projetopdm2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val repository = GastosRepository()
    private val adapter = DespesasAdapter()
    private val despesas = mutableListOf<Despesa>()
    private lateinit var tvTotal: TextView
    private var categoriaSelecionada = "Todas"
    private val moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNovaDespesa = findViewById<Button>(R.id.btnNovaDespesa)
        val spFiltro = findViewById<Spinner>(R.id.spFiltro)
        val rvDespesas = findViewById<RecyclerView>(R.id.rvDespesas)
        tvTotal = findViewById(R.id.tvTotal)

        rvDespesas.layoutManager = LinearLayoutManager(this)
        rvDespesas.adapter = adapter

        configurarFiltro(spFiltro)
        carregarDespesas()

        btnNovaDespesa.setOnClickListener {
            val intent = Intent(this, Cadastro_despesas::class.java)
            startActivity(intent)
        }
    }

    private fun configurarFiltro(spFiltro: Spinner) {
        val categorias = listOf(
            "Todas",
            "Alimentação",
            "Transporte",
            "Lazer",
            "Saúde",
            "Outros"
        )

        val filtroAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categorias
        )

        filtroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFiltro.adapter = filtroAdapter

        spFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoriaSelecionada = categorias[position]
                aplicarFiltro()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun carregarDespesas() {
        repository.observarDespesas(
            aoAtualizar = { novasDespesas ->
                despesas.clear()
                despesas.addAll(novasDespesas)
                aplicarFiltro()
            },
            aoErro = {
                Toast.makeText(this, "Erro ao carregar despesas", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun aplicarFiltro() {
        val despesasFiltradas = if (categoriaSelecionada == "Todas") {
            despesas
        } else {
            despesas.filter { it.categoria == categoriaSelecionada }
        }

        adapter.atualizar(despesasFiltradas)

        val totalMes = despesasFiltradas
            .filter { pertenceAoMesAtual(it.data) }
            .sumOf { it.valor }

        tvTotal.text = "Total do mês: ${moeda.format(totalMes)}"
    }

    private fun pertenceAoMesAtual(data: Long): Boolean {
        val hoje = Calendar.getInstance()
        val dataDespesa = Calendar.getInstance().apply {
            timeInMillis = data
        }

        return hoje.get(Calendar.YEAR) == dataDespesa.get(Calendar.YEAR) &&
            hoje.get(Calendar.MONTH) == dataDespesa.get(Calendar.MONTH)
    }
}
