package com.example.factureroreparacioncelular

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_configuracion.AñadirTrabajadores

class Anadir : AppCompatActivity() {
    val añadirFragment :AnadirFragment = AnadirFragment()
    var encargado = ""
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        encargado = intent.getStringExtra("nombreUsuario").toString()
        if (encargado != null) {
            abrirFragment (añadirFragment,encargado)
        }

    }

    private fun abrirFragment(fragment: Fragment , encargado : String) {
        val bundle = Bundle()
        bundle.putString("nombreUsuario", encargado)
        fragment.arguments = bundle
        val transaccion = supportFragmentManager.beginTransaction()
        transaccion.replace(R.id.fragmentAñadir,fragment)
        transaccion.commit()
    }
}