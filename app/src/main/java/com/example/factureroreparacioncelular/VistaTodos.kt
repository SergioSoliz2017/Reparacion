package com.example.factureroreparacioncelular

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_vista_todos.listRecyclerViewTrabajadores
import www.sanju.motiontoast.MotionToast

class VistaTodos : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var elements : ArrayList<ListTrabajadores>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_todos)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        verificarInternet()
        obtenerLista()
    }

    private fun obtenerLista() {
        db.collection("Usuarios").get().addOnSuccessListener { resultado ->
            elements  = ArrayList<ListTrabajadores>()
            for (documento in resultado){
                var dato = ListTrabajadores (documento.data?.get("Nombre").toString(),
                    documento.data?.get("Usuario").toString(), documento.data?.get("Rol").toString())
                elements.add(dato)

            }
            var listAdapter : ListAdapterTrabajadores = ListAdapterTrabajadores(elements,this,ListAdapterTrabajadores.OnItemClickListener { item: ListTrabajadores? -> moveToDescription(item) })
            listRecyclerViewTrabajadores.setHasFixedSize(true)
            listRecyclerViewTrabajadores.setLayoutManager(LinearLayoutManager(this))
            listRecyclerViewTrabajadores.setAdapter(listAdapter)
        }
    }

    private fun moveToDescription(item: ListTrabajadores?) {
        val nombreUsuario = item?.usuario
        val inicio = Intent(this, Inicio:: class.java)
        inicio.putExtra("nombreUsuario",nombreUsuario)
        startActivity(inicio)
    }

    private fun verificarInternet(): Boolean {
        var internet = false
        val con: ConnectivityManager = applicationContext.
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = con.activeNetworkInfo
        if (networkInfo!= null && networkInfo.isConnected){
            internet=true
        }else{
            MotionToast.createToast(this,"Sin Internet", "Conectarse a internet", MotionToast.TOAST_NO_INTERNET,
                MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
        }
        return internet
    }
}