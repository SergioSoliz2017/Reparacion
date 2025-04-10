package com.example.factureroreparacioncelular

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_configuracion.AñadirTrabajadores
import kotlinx.android.synthetic.main.fragment_configuracion.EliminarTrabajador
import kotlinx.android.synthetic.main.fragment_configuracion.EliminarTrabajadores
import kotlinx.android.synthetic.main.fragment_configuracion.TextContraseña
import kotlinx.android.synthetic.main.fragment_configuracion.btnEditContraseña
import kotlinx.android.synthetic.main.fragment_configuracion.btnEditNombre
import kotlinx.android.synthetic.main.fragment_configuracion.btnGuardarContraseña
import kotlinx.android.synthetic.main.fragment_configuracion.btnGuardarNombre
import kotlinx.android.synthetic.main.fragment_configuracion.editContraseña
import kotlinx.android.synthetic.main.fragment_configuracion.editNombre
import kotlinx.android.synthetic.main.fragment_configuracion.nombreTrabajadorEliminar
import kotlinx.android.synthetic.main.fragment_configuracion.textNombre
import www.sanju.motiontoast.MotionToast

class ConfiguracionFragment : Fragment(R.layout.fragment_configuracion) {

    val db = Firebase.firestore
    var usuario : ListTrabajadores = ListTrabajadores()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null){
            val nombreUsuario = requireArguments().getString("nombreUsuario")
            db.collection("Usuarios").document(nombreUsuario.toString()).get().addOnSuccessListener { documento ->
                usuario.contraseña = documento.data?.get("Contraseña").toString()
                usuario.nombre = documento.data?.get("Nombre").toString()
                usuario.rol = documento.data?.get("Rol").toString()
                usuario.usuario = documento.id
                TextContraseña.text = contraseñaTapada(usuario.contraseña)
                textNombre.text = usuario.nombre
                btnEditContraseña.visibility = View.VISIBLE
                btnEditNombre.visibility = View.VISIBLE
                editarContraseña(usuario.contraseña)
                editarNombre(usuario.nombre)
                guardarContraseña()
                guardarNombre()
                crearNuevoTrabajador(usuario.rol)
                eliminarTrabajador(usuario.rol)
            }
        }
    }

    private fun crearNuevoTrabajador(rol: String) {
        if (rol == "Jefe"){
            AñadirTrabajadores.visibility = View.VISIBLE
            AñadirTrabajadores.setOnClickListener {
                val añadir = Intent(this.context, AnadirTrabajador:: class.java)
                añadir.putExtra("nombreUsuario",usuario.nombre)
                startActivity(añadir)
            }
        }

    }

    private fun eliminarTrabajador(rol: String) {
        if (rol == "Jefe") {
            EliminarTrabajadores.visibility = View.VISIBLE
            EliminarTrabajadores.setOnClickListener {
                EliminarTrabajador.visibility = View.VISIBLE
                nombreTrabajadorEliminar.visibility = View.VISIBLE
            }
            EliminarTrabajador.setOnClickListener {
                db.collection("Usuarios").document(nombreTrabajadorEliminar.text.toString())
                    .delete().addOnSuccessListener {
                        MotionToast.createToast(
                            this.context as Activity,"Operacion Exitosa", "Se elimino correctamente",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,null)
                        EliminarTrabajador.visibility = View.INVISIBLE
                        nombreTrabajadorEliminar.visibility = View.INVISIBLE
                    }
            }
        }
    }

    private fun guardarContraseña() {
        btnGuardarContraseña.setOnClickListener {
            usuario.contraseña = editContraseña.text.toString()
            TextContraseña.text = usuario.contraseña
            btnEditContraseña.visibility = View.VISIBLE
            btnGuardarContraseña.visibility = View.INVISIBLE
            editContraseña.visibility = View.INVISIBLE
            TextContraseña.visibility = View.VISIBLE
            actualizarDato()
        }
    }

    private fun guardarNombre() {
        btnGuardarNombre.setOnClickListener {
            usuario.nombre = editNombre.text.toString()
            textNombre.text = usuario.nombre
            btnEditNombre.visibility = View.VISIBLE
            btnGuardarNombre.visibility = View.INVISIBLE
            editNombre.visibility = View.INVISIBLE
            textNombre.visibility = View.VISIBLE
            actualizarDato()
        }
    }

    private fun actualizarDato() {
        val dato = hashMapOf(
            "Nombre" to usuario.nombre,
            "Contraseña" to usuario.contraseña,
            "Rol" to usuario.rol,
            "Usuario" to usuario.usuario
        )
        db.collection("Usuarios").document(usuario.usuario).update(dato as Map<String, Any>)

    }

    private fun editarContraseña(contraseña: String) {
        btnEditContraseña.setOnClickListener {
            editContraseña.visibility = View.VISIBLE
            TextContraseña.visibility = View.INVISIBLE
            editContraseña.setText(contraseña)
            btnGuardarContraseña.visibility = View.VISIBLE
            btnEditContraseña.visibility = View.INVISIBLE
        }
    }

    private fun editarNombre(nombre: String) {
        btnEditNombre.setOnClickListener {
            editNombre.visibility = View.VISIBLE
            textNombre.visibility = View.INVISIBLE
            editNombre.setText(nombre)
            btnGuardarNombre.visibility = View.VISIBLE
            btnEditNombre.visibility = View.INVISIBLE
        }
    }

    private fun contraseñaTapada(contraseña: String): String {
        var contraseñaTapada = ""
        var i = 0
        while (i < contraseña.length){
            contraseñaTapada = contraseñaTapada+"*"
            i++
        }
        return contraseñaTapada
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_configuracion, container, false)
    }

}