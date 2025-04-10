package com.example.factureroreparacioncelular

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_anadir_trabajador.SubirNuevoTrabajador
import kotlinx.android.synthetic.main.activity_anadir_trabajador.codigoTrabajador
import kotlinx.android.synthetic.main.activity_anadir_trabajador.contraseñaTrabajador
import kotlinx.android.synthetic.main.activity_anadir_trabajador.nombreTrabajador
import www.sanju.motiontoast.MotionToast

class   AnadirTrabajador : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_trabajador)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        SubirNuevoTrabajador.setOnClickListener {
            if (esValido()){
                val dato = hashMapOf(
                    "Contraseña" to contraseñaTrabajador.text.toString(),
                    "Nombre" to nombreTrabajador.text.toString(),
                    "Rol" to "Empleado",
                    "Usuario" to codigoTrabajador.text.toString()
                )
                db.collection("Usuarios").document(codigoTrabajador.text.toString()).set(dato).addOnSuccessListener {
                    MotionToast.createToast(
                        this, "Operacion Exitosa", "Registro Exitoso",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION, null
                    )
                    finish()
                }
            }
        }
    }

    private fun esValido(): Boolean {
        var valido = true
        if (nombreTrabajador.text.toString() == ""){
            valido = false
            MotionToast.createToast( this,"Operacion incompleta", "Ingrese nombre del trabajador", MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
        }
        if (contraseñaTrabajador.text.toString() == ""){
            valido = false
            MotionToast.createToast( this,"Operacion incompleta", "Ingrese contraseña del trabajador", MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
        }
        if (codigoTrabajador.text.toString() == ""){
            valido = false
            MotionToast.createToast( this,"Operacion incompleta", "Ingrese codigo del trabajador", MotionToast.TOAST_WARNING,
                MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
        }
        return valido
    }
}