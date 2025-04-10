package com.example.factureroreparacioncelular

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_inicio.listRecyclerView


class InicioFragment : Fragment(R.layout.fragment_inicio) {

    private lateinit var elements : ArrayList<ListElement>

    val db = Firebase.firestore
    var nombreUsuario = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null){
            nombreUsuario = requireArguments().getString("nombreUsuario").toString()
            obtenerLista()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    private fun obtenerLista() {
        db.collection("Celulares").whereEqualTo("Encargado",nombreUsuario).get().addOnSuccessListener { resultado ->
            elements  = ArrayList<ListElement>()
            for (documento in resultado){
                var dato = ListElement (
                    documento.data?.get("Modelo").toString(),
                    documento.data?.get("Cliente").toString(),
                    documento.data?.get("Estado").toString(),
                    "",
                    documento.data?.get("Mes").toString(),
                    documento.id
                )
                elements.add(dato)
            }
            ponerColor()
            var listAdapter : ListAdapter = ListAdapter(elements,this.context,ListAdapter.OnItemClickListener { item: ListElement? -> moveToDescription(item) })
            listRecyclerView.setHasFixedSize(true)
            listRecyclerView.setLayoutManager(LinearLayoutManager(this.context))
            listRecyclerView.setAdapter(listAdapter)
        }
    }

    private fun moveToDescription(item: ListElement?) {
        val descripcion = Intent(this.context, Descripcion:: class.java)
        descripcion.putExtra("codigo", item?.codigo)
        startActivity(descripcion)
        activity?.finish()
    }

    private fun ponerColor() {
        var i : Int=0
        while (i < elements.size){
            if (elements.get(i).estado=="Activo"){
                elements.get(i).color="#FF0000"
            }else{
                if (elements.get(i).estado=="En Proceso"){
                    elements.get(i).color="#FFFF00"
                }else{
                    elements.get(i).color="#00FF00"
                }
            }
            i ++
        }
    }
}