package com.example.factureroreparacioncelular

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_buscar.CarnetBuscar
import kotlinx.android.synthetic.main.activity_buscar.buscarCI
import kotlinx.android.synthetic.main.activity_buscar.listaBuscar

class Buscar : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var elements : ArrayList<ListElement>
    var carnet = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        buscarCI.setOnClickListener {
            carnet = CarnetBuscar.text.toString()
            obtenerLista()
        }
    }

    private fun obtenerLista() {
        db.collection("Celulares").whereEqualTo("Ci",carnet).get().addOnSuccessListener { resultado ->
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
            val listAdapter : ListAdapter = ListAdapter(elements,this,ListAdapter.OnItemClickListener { item: ListElement? -> moveToDescription(item) })
            listaBuscar.setHasFixedSize(true)
            listaBuscar.setLayoutManager(LinearLayoutManager(this))
            listaBuscar.setAdapter(listAdapter)
        }
    }

    private fun moveToDescription(item: ListElement?) {
        val descripcion = Intent(this, Descripcion:: class.java)
        descripcion.putExtra("codigo", item?.codigo)
        startActivity(descripcion)
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