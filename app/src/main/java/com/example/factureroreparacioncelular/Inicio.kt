package com.example.factureroreparacioncelular

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_inicio.anadir
import kotlinx.android.synthetic.main.activity_inicio.bottomNavigationView
import kotlinx.android.synthetic.main.activity_inicio.buscar

import www.sanju.motiontoast.MotionToast

class Inicio : AppCompatActivity() {

    var user = ""
    val inicioFragment :InicioFragment = InicioFragment()
    val configuracionFragment : ConfiguracionFragment = ConfiguracionFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        user = intent.getStringExtra("nombreUsuario").toString()
        verificarInternet()
        abrirFragment (inicioFragment)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.inicio -> {
                    if(verificarInternet()){
                        abrirFragment (inicioFragment)
                    }
                    true
                }
                R.id.configuracion -> {
                    if(verificarInternet()){
                        abrirFragment (configuracionFragment)
                    }
                    true
                }

                else -> false
            }
        }

        anadir.setOnClickListener {
            if(verificarInternet()){
                val añadir = Intent(this, Anadir:: class.java)
                añadir.putExtra("nombreUsuario",user)
                startActivity(añadir)
                finish()
            }
        }
        buscar.setOnClickListener {
            val buscar = Intent(this, Buscar:: class.java)
            startActivity(buscar)
        }
    }

    private fun abrirFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("nombreUsuario", user)
        fragment.arguments = bundle
        val transaccion = supportFragmentManager.beginTransaction()
        transaccion.replace(R.id.containerView,fragment)
        transaccion.commit()
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
