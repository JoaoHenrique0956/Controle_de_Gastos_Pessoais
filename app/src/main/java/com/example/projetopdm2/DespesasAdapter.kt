package com.example.projetopdm2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class DespesasAdapter : RecyclerView.Adapter<DespesasAdapter.DespesaViewHolder>() {

    private val despesas = mutableListOf<Despesa>()
    private val moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

    fun atualizar(novasDespesas: List<Despesa>) {
        despesas.clear()
        despesas.addAll(novasDespesas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_despesa, parent, false)

        return DespesaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DespesaViewHolder, position: Int) {
        holder.bind(despesas[position])
    }

    override fun getItemCount() = despesas.size

    inner class DespesaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDescricao = itemView.findViewById<TextView>(R.id.tvDescricaoItem)
        private val tvCategoria = itemView.findViewById<TextView>(R.id.tvCategoriaItem)
        private val tvValor = itemView.findViewById<TextView>(R.id.tvValorItem)

        fun bind(despesa: Despesa) {
            tvDescricao.text = despesa.descricao
            tvCategoria.text = despesa.categoria
            tvValor.text = moeda.format(despesa.valor)
        }
    }
}
