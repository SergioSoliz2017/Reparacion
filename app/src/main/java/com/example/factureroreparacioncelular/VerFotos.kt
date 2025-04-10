package com.example.factureroreparacioncelular

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ver_fotos.FotoFinal
import kotlinx.android.synthetic.main.activity_ver_fotos.FotoInicial
import kotlinx.android.synthetic.main.activity_ver_fotos.ImagenDeFotos
import www.sanju.motiontoast.MotionToast
import java.io.File

class VerFotos : AppCompatActivity() {
    var codigoCelular = ""
    val db = Firebase.firestore
    lateinit var document : DocumentSnapshot
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_fotos)
        window.statusBarColor = Color.parseColor("#000000")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        codigoCelular = intent.getStringExtra("codigoCelular").toString()
        db.collection("Celulares").document(codigoCelular).get().addOnSuccessListener {
            document = it
                val fotoInicial = document.data?.get("FotoInicial").toString()
                val fotoFinal = document.data?.get("FotoFinal").toString()
                if (fotoFinal == "") {FotoFinal.text="Tomar Foto"}else{FotoFinal.text="Foto Final"}
                FotoInicial.setOnClickListener {
                    Picasso.with(this).load(fotoInicial).into(ImagenDeFotos)
                }
                FotoFinal.setOnClickListener {
                    if (FotoFinal.text == "Tomar Foto"){
                        if (document.data?.get("Estado") == "En Proceso"){
                            sacarFoto()
                        }else{
                            MotionToast.createToast(
                                this, "Operacion Fallida", "Error no se puede realizar la foto",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION, null
                            )
                        }
                    }else{
                        FotoFinal.text = "Foto Final"
                        Picasso.with(this).load(fotoFinal).into(ImagenDeFotos)
                    }
                }
        }
    }

    lateinit var storageReference : StorageReference

    @SuppressLint("SimpleDateFormat")
    private fun subirFoto(document: DocumentSnapshot) {
        val celular : Celular = CrearCelular(document)
        storageReference = FirebaseStorage.getInstance().getReference("Celulares/$codigoCelular+Final")
        storageReference.putFile(photoUri).addOnSuccessListener {snapshot->
            val uriTask : Task<Uri> = snapshot.getStorage().getDownloadUrl()
            uriTask.addOnSuccessListener {uri->
                celular.fotoFinal = uri.toString()
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
                    )
                    Picasso.with(this).load(celular.fotoFinal).into(ImagenDeFotos)
                }
            }
        }
    }

    private fun CrearCelular(document: DocumentSnapshot): Celular {
        val celular: Celular = Celular()
        celular.cliente = document.data?.get("Cliente").toString()
        celular.ci = document.data?.get("Ci").toString()
        celular.celular = document.data?.get("Celular").toString()
        celular.reparacion = document.data?.get("Reparacion").toString()
        celular.encargado = document.data?.get("Encargado").toString()
        celular.modelo = document.data?.get("Modelo").toString()
        celular.precio = document.data?.get("Precio").toString()
        celular.observaciones = document.data?.get("Observaciones").toString()
        celular.estado = document.data?.get("Estado").toString()
        celular.fotoInicial = document.data?.get("FotoInicial").toString()
        celular.fechaInicio = document.data?.get("FechaEntrada").toString()
        celular.fotoFinal = document.data?.get("FechaSalida").toString()
        celular.mes = document.data?.get("Mes").toString()
        return celular
    }

    private fun sacarFoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            it.resolveActivity(packageManager!!).also { components ->
                createPhotoFile()
                photoUri  = FileProvider.getUriForFile(this,"com.example.factureroreparacioncelular.fileprovider",file)
                it.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
            }
        }
        abrirCamara.launch(intent)
    }

    private lateinit var file : File

    lateinit var photoUri : Uri

    var bitmap: Bitmap? = null

    private fun createPhotoFile(){
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        file = File.createTempFile("IMG_${System.currentTimeMillis()}_",".jpg",dir)
    }

    val abrirCamara = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            bitmap = BitmapFactory.decodeFile(file.toString())
            ImagenDeFotos.setImageBitmap(bitmap)
            ImagenDeFotos.rotation = 90F
            FotoFinal.text = "Foto Final"
            subirFoto(document)
        }
    }
}