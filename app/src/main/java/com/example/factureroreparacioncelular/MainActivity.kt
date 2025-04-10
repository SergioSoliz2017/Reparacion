package com.example.factureroreparacioncelular

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.contraseña
import kotlinx.android.synthetic.main.activity_main.entrar
import kotlinx.android.synthetic.main.activity_main.usuario
import www.sanju.motiontoast.MotionToast

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        verificarInternet()
        iniciarSesion()
        sesion()
    }

    private fun sesion() {

        val preferencias : SharedPreferences = getSharedPreferences("Credenciales",Context.MODE_PRIVATE)
        val user = preferencias.getString("USER",null)
        val contra = preferencias.getString("CONTRASEÑA",null)
        if (user != null && contra!= null){
            db.collection("Usuarios").document(user).get().addOnSuccessListener { documento->
                if (documento.exists()){
                    usuario.setText(user)
                    if (documento.data?.get("Rol").toString() == "Jefe"){
                        if (contra == documento.data?.get("Contraseña").toString()){
                            MotionToast.createToast(this,"Operacion Exitosa", "Operacion realizada exitosa",MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                            val inicio = Intent(this, VistaTodos:: class.java)
                            startActivity(inicio)
                            finish()
                        }else{
                            MotionToast.createToast(this,"Operacion Fallida", "Contraseña incorrecta",MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                        }
                    }else{
                        if (contra == documento.data?.get("Contraseña").toString()){
                            MotionToast.createToast(this,"Operacion Exitosa", "Operacion realizada exitosa",MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                            val inicio = Intent(this, Inicio:: class.java)
                            inicio.putExtra("nombreUsuario",documento.data?.get("Usuario").toString())
                            startActivity(inicio)
                            finish()
                        }else{
                            MotionToast.createToast(this,"Operacion Fallida", "Contraseña incorrecta",MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                        }
                    }

                }else{
                    MotionToast.createToast(this,"Operacion Fallida", "Código usuario incorrecto",MotionToast.TOAST_ERROR,
                        MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                }
            }
        }
    }

    private fun iniciarSesion() {
        entrar.setOnClickListener {
            if(usuario.text.toString() != ""){
                if ( contraseña.text.toString() != ""){
                    if (verificarInternet()){
                        db.collection("Usuarios").document(usuario.text.toString()).get().addOnSuccessListener { documento->
                            if (documento.exists()){
                                if (documento.data?.get("Rol").toString() == "Jefe"){
                                    if (contraseña.text.toString() == documento.data?.get("Contraseña").toString()){
                                        MotionToast.createToast(this,"Operacion Exitosa", "Operacion realizada exitosa",MotionToast.TOAST_SUCCESS,
                                            MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                                            guardarDatos()
                                            val inicio = Intent(this, VistaTodos:: class.java)
                                            startActivity(inicio)
                                            finish()
                                    }else{
                                        MotionToast.createToast(this,"Operacion Fallida", "Contraseña incorrecta",MotionToast.TOAST_ERROR,
                                            MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                                    }
                                }else{
                                    if (contraseña.text.toString() == documento.data?.get("Contraseña").toString()){
                                        MotionToast.createToast(this,"Operacion Exitosa", "Operacion realizada exitosa",MotionToast.TOAST_SUCCESS,
                                            MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                                        guardarDatos()
                                        val inicio = Intent(this, Inicio:: class.java)
                                        inicio.putExtra("nombreUsuario",documento.data?.get("Usuario").toString())
                                        startActivity(inicio)
                                        finish()
                                    }else{
                                        MotionToast.createToast(this,"Operacion Fallida", "Contraseña incorrecta",MotionToast.TOAST_ERROR,
                                            MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                                    }
                                }

                            }else{
                                MotionToast.createToast(this,"Operacion Fallida", "Código usuario incorrecto",MotionToast.TOAST_ERROR,
                                    MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                            }
                        }
                    }
                }else{
                    MotionToast.createToast(this,"Operacion incompleta", "Ingrese contraseña",MotionToast.TOAST_WARNING,
                        MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
                }
            }else{
                MotionToast.createToast(this,"Operacion incompleta", "Ingrese usuario",MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,MotionToast.LONG_DURATION,null)
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun guardarDatos() {
        val preferencias : SharedPreferences = getSharedPreferences("Credenciales",Context.MODE_PRIVATE)
        val user = usuario.text.toString()
        val contra = contraseña.text.toString()
        val editor : SharedPreferences.Editor = preferencias.edit()
        editor.putString("USER",user)
        editor.putString("CONTRASEÑA",contra)
        editor.apply()
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