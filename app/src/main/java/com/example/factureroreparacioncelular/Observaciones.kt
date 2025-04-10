package com.example.factureroreparacioncelular

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_observaciones.EditObservaciones
import kotlinx.android.synthetic.main.activity_observaciones.EditarObservaciones
import kotlinx.android.synthetic.main.activity_observaciones.GuardarObservaciones
import kotlinx.android.synthetic.main.activity_observaciones.TextoObservaciones
import kotlinx.android.synthetic.main.activity_ver_fotos.ImagenDeFotos
import kotlinx.android.synthetic.main.fragment_anadir.carnet
import kotlinx.android.synthetic.main.fragment_anadir.celularCliente
import kotlinx.android.synthetic.main.fragment_anadir.cliente
import kotlinx.android.synthetic.main.fragment_anadir.modelo
import kotlinx.android.synthetic.main.fragment_anadir.observaciones
import kotlinx.android.synthetic.main.fragment_anadir.precio
import kotlinx.android.synthetic.main.fragment_anadir.reparacion
import www.sanju.motiontoast.MotionToast

class Observaciones : AppCompatActivity() {

    var codigoCelular = ""
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observaciones)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        codigoCelular = intent.getStringExtra("codigoCelular").toString()
        db.collection("Celulares").document(codigoCelular).get().addOnSuccessListener {document ->
            val texto =  document.data?.get("Observaciones").toString()
            TextoObservaciones.text = texto
            EditarObservaciones.setOnClickListener {
                EditObservaciones.visibility = View.VISIBLE
                TextoObservaciones.visibility = View.INVISIBLE
                GuardarObservaciones.visibility = View.VISIBLE
                EditarObservaciones.visibility = View.INVISIBLE
                EditObservaciones.setText(texto)
            }
            GuardarObservaciones.setOnClickListener {
                EditObservaciones.visibility = View.INVISIBLE
                TextoObservaciones.visibility = View.VISIBLE
                GuardarObservaciones.visibility = View.INVISIBLE
                EditarObservaciones.visibility = View.VISIBLE
                var celular: Celular = Celular()
                celular.cliente = document.data?.get("Cliente").toString()
                celular.ci = document.data?.get("Ci").toString()
                celular.celular = document.data?.get("Celular").toString()
                celular.reparacion = document.data?.get("Reparacion").toString()
                celular.encargado = document.data?.get("Encargado").toString()
                celular.modelo = document.data?.get("Modelo").toString()
                celular.precio = document.data?.get("Precio").toString()
                celular.observaciones = EditObservaciones.text.toString()
                celular.estado = document.data?.get("Estado").toString()
                celular.fotoInicial = document.data?.get("FotoInicial").toString()
                celular.fotoFinal = document.data?.get("FotoFinal").toString()
                celular.fechaInicio = document.data?.get("FechaEntrada").toString()
                celular.fechaFin=document.data?.get("FechaSalida").toString()
                celular.mes=document.data?.get("Mes").toString()
                TextoObservaciones.text = celular.observaciones
                subirDatos(celular)
            }
        }
    }

    private fun subirDatos(celular: Celular) {
        val dato = hashMapOf(
                "Cliente" to celular.cliente,
            "Ci" to celular.ci,
            "Celular" to celular.celular,
            "Reparacion" to celular.reparacion,
            "Modelo" to celular.modelo,
            "Precio" to celular.precio,
            "Observaciones" to celular.observaciones,
            "FotoInicial" to celular.fotoInicial,
            "FotoFinal" to celular.fotoFinal,
            "Encargado" to celular.encargado,
            "Estado" to celular.estado,
            "FechaEntrada" to celular.fechaInicio,
            "FechaSalida" to celular.fechaFin,
            "Mes" to celular.mes
        )
        db.collection("Celulares").document(codigoCelular).set(dato).addOnSuccessListener {
            MotionToast.createToast(
                this, "Operacion Exitosa", "Registro Exitoso",
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION, null
,            )
        }
    }
}