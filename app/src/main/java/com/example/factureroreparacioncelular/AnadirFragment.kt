package com.example.factureroreparacioncelular

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_anadir.carnet
import kotlinx.android.synthetic.main.fragment_anadir.celularCliente
import kotlinx.android.synthetic.main.fragment_anadir.cliente
import kotlinx.android.synthetic.main.fragment_anadir.modelo
import kotlinx.android.synthetic.main.fragment_anadir.observaciones
import kotlinx.android.synthetic.main.fragment_anadir.precio
import kotlinx.android.synthetic.main.fragment_anadir.reparacion
import kotlinx.android.synthetic.main.fragment_anadir.siguienteAñadir
import www.sanju.motiontoast.MotionToast


class AnadirFragment : Fragment() {

    var encargado = ""
    val sacarFotoFragment :SacarFotoFragment = SacarFotoFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null){
            encargado = requireArguments().getString("nombreUsuario").toString()
            siguienteAñadir.setOnClickListener {
                if (esValido()) {
                    var celular: Celular = Celular()
                    celular.cliente = cliente.text.toString()
                    celular.ci = carnet.text.toString()
                    celular.celular = celularCliente.text.toString()
                    celular.reparacion = reparacion.text.toString()
                    celular.encargado = encargado
                    celular.modelo = modelo.text.toString()
                    celular.precio = precio.text.toString()
                    celular.observaciones = observaciones.text.toString()
                    celular.estado = "Activo"
                    abrirFragment(sacarFotoFragment, celular)
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anadir, container, false)
    }
    private fun abrirFragment(fragment: Fragment ,celular: Celular) {
        val bundle = Bundle()
        bundle.putSerializable("celular",celular)
        fragment.arguments = bundle
        val transaccion = activity?.supportFragmentManager?.beginTransaction()
        transaccion?.setReorderingAllowed(true)
        transaccion?.replace(R.id.fragmentAñadir,fragment)
        transaccion?.commit()
    }

    private fun esValido(): Boolean {
            var valido = true
            if (cliente.text.toString() == ""){
                valido = false
                MotionToast.createToast( this.context as Activity,"Operacion incompleta", "Ingrese nombre del cliente", MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
            }
            if (carnet.text.toString() == ""){
                valido = false
                MotionToast.createToast( this.context as Activity,"Operacion incompleta", "Ingrese carnet del cliente", MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
            }
            if (celularCliente.text.toString() == ""){
                valido = false
                MotionToast.createToast( this.context as Activity,"Operacion incompleta", "Ingrese numero del cliente", MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
            }
            if (reparacion.text.toString() == ""){
                valido = false
                MotionToast.createToast( this.context as Activity,"Operacion incompleta", "Ingrese tipo de reparacion", MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
            }
            if (modelo.text.toString() == ""){
                valido = false
                MotionToast.createToast( this.context as Activity,"Operacion incompleta", "Ingrese modelo del celular", MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
            }
            if (precio.text.toString() == ""){
                valido = false
                MotionToast.createToast( this.context as Activity,"Operacion incompleta", "Ingrese precio", MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
            }
            if (observaciones.text.toString() == ""){
                valido = false
                MotionToast.createToast(
                    this.context as Activity,"Operacion incompleta", "Ingrese observaciones principales", MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,null)
            }
            return valido
        }
}