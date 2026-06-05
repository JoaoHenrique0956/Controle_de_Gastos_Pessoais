package com.example.projetopdm2

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GastosRepository {

    private val database = FirebaseDatabase.getInstance()
    private val despesasReference = database.getReference("despesas")

    fun salvarDespesa(
        descricao: String,
        valor: Double,
        categoria: String
    ) {

        val despesaId = despesasReference
            .push()
            .key ?: return

        val despesa = mapOf(
            "id" to despesaId,
            "descricao" to descricao,
            "valor" to valor,
            "categoria" to categoria,
            "data" to System.currentTimeMillis()
        )

        despesasReference
            .child(despesaId)
            .setValue(despesa)
    }

    fun observarDespesas(
        aoAtualizar: (List<Despesa>) -> Unit,
        aoErro: (DatabaseError) -> Unit
    ) {
        despesasReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val despesas = snapshot.children
                    .mapNotNull { it.getValue(Despesa::class.java) }
                    .sortedByDescending { it.data }

                aoAtualizar(despesas)
            }

            override fun onCancelled(error: DatabaseError) {
                aoErro(error)
            }
        })
    }

    fun editarDespesa(
        despesaId: String,
        descricao: String,
        valor: Double,
        categoria: String
    ) {

        val despesaAtualizada = mapOf(
            "id" to despesaId,
            "descricao" to descricao,
            "valor" to valor,
            "categoria" to categoria,
            "data" to System.currentTimeMillis()
        )

        despesasReference
            .child(despesaId)
            .setValue(despesaAtualizada)
    }

    fun excluirDespesa(despesaId: String) {

        despesasReference
            .child(despesaId)
            .removeValue()
    }
}
