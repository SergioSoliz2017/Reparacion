package com.example.factureroreparacioncelular

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_descripcion.CarnetDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.CelularDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.ContinuarDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.EstadoDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.FechaEntradaDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.FechaSalidaDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.ImeiDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.ModeloCelularDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.NombreClienteDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.PrecioDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.ReparacionDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.VerFotosDescripcion
import kotlinx.android.synthetic.main.activity_descripcion.VerObservacionesDescripcion
import www.sanju.motiontoast.MotionToast
import java.text.SimpleDateFormat
import java.util.Date


class Descripcion : AppCompatActivity() {
    val db = Firebase.firestore
    var celular: Celular = Celular()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descripcion)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val codigo : String = intent.getStringExtra("codigo").toString()
        db.collection("Celulares").document(codigo).get().addOnSuccessListener { resultado ->
            celular = crearCelular(resultado)
            ModeloCelularDescripcion.text = celular.modelo
            NombreClienteDescripcion.text = "Cliente: ${celular.cliente}"
            CarnetDescripcion.text = "CI: ${celular.ci}"
            CelularDescripcion.text = "Celular: ${celular.celular}"
            FechaEntradaDescripcion.text = "Fecha Inicial: ${celular.fechaInicio}"
            FechaSalidaDescripcion.text = "Fecha Final: ${celular.fechaFin}"
            EstadoDescripcion.text = "Estado: ${celular.estado}"
            ReparacionDescripcion.text = "Reparacion: ${celular.reparacion}"
            PrecioDescripcion.text = "Precio: ${celular.precio}"
            VerFotosDescripcion.visibility = View.VISIBLE
            VerObservacionesDescripcion.visibility = View.VISIBLE
            botonContinuar(resultado.data?.get("Estado").toString())
            ContinuarDescripcion.setOnClickListener {
                var dato = hashMapOf("" to "")
                if (ContinuarDescripcion.text == "Iniciar reparacion"){
                    dato = hashMapOf(
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
                        "Estado" to "En Proceso",
                        "FechaEntrada" to celular.fechaInicio,
                        "FechaSalida" to celular.fechaFin,
                        "Mes" to celular.mes
                    )
                }
                if (ContinuarDescripcion.text == "Reparacion finalizada"){
                    dato = hashMapOf(
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
                        "Estado" to "Finalizado",
                        "FechaEntrada" to celular.mes,
                        "FechaSalida" to celular.fechaFin,
                        "Mes" to celular.mes
                    )
                }
                if (ContinuarDescripcion.text == "Celular entregado"){
                    dato = hashMapOf(
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
                        "Estado" to "Entregado",
                        "FechaEntrada" to celular.mes,
                        "FechaSalida" to celular.fechaFin,
                        "Mes" to celular.mes
                    )
                }
                db.collection("Celulares").document(celular.codigo).update(dato as Map<String, Any>)
                MotionToast.createToast(
                    this as Activity,"Operacion Exitosa", "Tarea realizada",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,null)
                val actualizado = Intent(this, Inicio:: class.java)
                actualizado.putExtra("nombreUsuario",celular.encargado)
                startActivity(actualizado)
                finish()
            }
            VerFotosDescripcion.setOnClickListener {
                val foto = Intent(this, VerFotos:: class.java)
                foto.putExtra("codigoCelular",celular.codigo)
                startActivity(foto)
            }
            VerObservacionesDescripcion.setOnClickListener {
                val observaciones = Intent(this, Observaciones:: class.java)
                observaciones.putExtra("codigoCelular",celular.codigo)
                startActivity(observaciones)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val inicio = Intent(this, Inicio:: class.java)
        inicio.putExtra("nombreUsuario",celular.encargado)
        startActivity(inicio)
    }



    private fun crearCelular(resultado: DocumentSnapshot?): Celular {
        var celularNuevo : Celular = Celular()
        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        val fecha = dateFormat.format(Date())
        celularNuevo.modelo = resultado?.data?.get("Modelo").toString()
        celularNuevo.cliente = resultado?.data?.get("Cliente").toString()
        celularNuevo.ci = resultado?.data?.get("Ci").toString()
        celularNuevo.celular = resultado?.data?.get("Celular").toString()
        celularNuevo.fechaInicio = resultado?.data?.get("FechaEntrada").toString()
        celularNuevo.fechaFin = fecha
        celularNuevo.estado = resultado?.data?.get("Estado").toString()
        celularNuevo.reparacion = resultado?.data?.get("Reparacion").toString()
        celularNuevo.precio = resultado?.data?.get("Precio").toString()
        celularNuevo.encargado = resultado?.data?.get("Encargado").toString()
        celularNuevo.observaciones = resultado?.data?.get("Observaciones").toString()
        celularNuevo.fotoInicial = resultado?.data?.get("FotoInicial").toString()
        celularNuevo.fotoFinal = resultado?.data?.get("FotoFinal").toString()
        celularNuevo.codigo = resultado?.id.toString()
        celularNuevo.mes = resultado?.data?.get("Mes").toString()
        return celularNuevo
    }

    private fun botonContinuar(estado: String) {
        if (estado == "Activo"){
            ContinuarDescripcion.text = "Iniciar reparacion"
            ContinuarDescripcion.visibility = View.VISIBLE
        }
        if (estado == "En Proceso"){
            ContinuarDescripcion.text = "Reparacion finalizada"
            ContinuarDescripcion.visibility = View.VISIBLE
        }
        if (estado == "Finalizado"){
            ContinuarDescripcion.text = "Celular entregado"
            ContinuarDescripcion.visibility = View.VISIBLE
        }
    }
}