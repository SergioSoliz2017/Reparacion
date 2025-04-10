package com.example.factureroreparacioncelular

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.SEND_SMS
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.fragment_sacar_foto.FotoCelular
import kotlinx.android.synthetic.main.fragment_sacar_foto.siguienteAñadir2
import kotlinx.android.synthetic.main.fragment_sacar_foto.tomarFoto
import www.sanju.motiontoast.MotionToast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date


class SacarFotoFragment : Fragment() {

    val db = Firebase.firestore
    private val STORAGE_CODE = 1001
    private var pfdEnviado = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var photoUri : Uri
    var celular : Celular = Celular()
    @SuppressLint("NewApi", "SimpleDateFormat")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        celular = requireArguments().getSerializable("celular") as Celular

        tomarFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                it.resolveActivity(activity?.packageManager!!).also { components ->
                    createPhotoFile()
                    photoUri  = FileProvider.getUriForFile(this.context as Activity,"com.example.factureroreparacioncelular.fileprovider",file)
                    it.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                }
            }
            abrirCamara.launch(intent)
        }
        siguienteAñadir2.setOnClickListener {
            if(!pfdEnviado){
                val codigo = generarCodigo(celular.cliente, celular.modelo)
                storageReference = FirebaseStorage.getInstance().getReference("Celulares/$codigo+Inicial")
                storageReference.putFile(photoUri).addOnSuccessListener {snapshot->
                    val uriTask : Task<Uri> = snapshot.getStorage().getDownloadUrl()
                    uriTask.addOnSuccessListener {uri->
                        celular.fotoInicial = uri.toString()
                        celular.fotoFinal = ""
                        val dateFormat = SimpleDateFormat("dd MMM yyyy")
                        val fecha = dateFormat.format(Date())
                        val dateFormatMes = SimpleDateFormat("MMM yyyy")
                        val mes = dateFormatMes.format(Date())
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
                            "FechaEntrada" to fecha,
                            "FechaSalida" to "",
                            "Mes" to mes
                        )
                        db.collection("Celulares").document(codigo).set(dato).addOnSuccessListener {
                            MotionToast.createToast(
                                this.context as Activity, "Operacion Exitosa", "Registro Exitoso",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION, null
                            )
                            if (Environment.isExternalStorageManager()) {
                                crearPDF(codigo,celular.encargado)
                            } else {

                                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                intent.data = Uri.parse("package:" + requireActivity().packageName)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }else{
                val guardar = Intent(this.context, Inicio::class.java)
                guardar.putExtra("nombreUsuario",celular.encargado)
                startActivity(guardar)
                activity?.finish()
            }

        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun crearPDF(codigo: String, encargado: String) {
        val pageSize = Rectangle(800f, 600f) //ancho y alto
        val documento = Document(pageSize)
        val mFileName = codigo

        val dir = Environment.getExternalStorageDirectory()
        val mFilePath = File.createTempFile("DOC_$mFileName",".pdf",dir)

        try {
            PdfWriter.getInstance(documento,FileOutputStream(mFilePath))
            documento.open()
            val titulo = Paragraph("SOPORTE TÉCNICO EXPECIALIZADO \n\n\n\n",FontFactory.getFont("arial",15f,
                Font.BOLD,
                BaseColor.BLACK
            ))
            val direccion = Paragraph("DIRECCION: AV. AYACUCHO ESQUINA CALAMA ACERA SUD OESTE TIENDA SMART CELL 2",FontFactory.getFont("arial",8f,
                BaseColor.BLACK
            ))
            val miTelefono = Paragraph("TELEFONO: 71435536",FontFactory.getFont("arial",8f,
                BaseColor.BLACK
            ))
            val dateFormat = SimpleDateFormat("d MMM yyyy, HH:mm:ss")
            val date = dateFormat.format(Date())
            val fecha = Paragraph(date,FontFactory.getFont("arial",8f,
                BaseColor.BLACK
            ))

            val bitmap = BitmapFactory.decodeResource(resources,R.drawable.banner)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imagen: Image = Image.getInstance(stream.toByteArray())
            imagen.scaleToFit(150F, 150f)
            titulo.alignment = Element.ALIGN_CENTER
            val columnWidth = floatArrayOf(250f, 250f,250f)
            val tablaEncabezado = PdfPTable(columnWidth)
            val cellImagen = PdfPCell(imagen)
            val cellTitulo = PdfPCell(titulo)
            val cellTree = PdfPCell()
            cellTitulo.horizontalAlignment = Element.ALIGN_CENTER
            cellTree.addElement(direccion)
            cellTree.addElement(miTelefono)
            cellTree.addElement(fecha)
            cellImagen.border = Rectangle.NO_BORDER
            cellTitulo.border = Rectangle.NO_BORDER
            cellTree.border = Rectangle.NO_BORDER
            tablaEncabezado.addCell(cellImagen)
            tablaEncabezado.addCell(cellTitulo)
            tablaEncabezado.addCell(cellTree)
            documento.add(tablaEncabezado)
            val espacio = Paragraph("\n")
            documento.add(espacio)
            val nombre = Paragraph("Nombre: "+celular.cliente,FontFactory.getFont("arial",10f,
                BaseColor.BLACK
            ))
            val ci = Paragraph("Carnet identidad: "+celular.ci,FontFactory.getFont("arial",10f,
                BaseColor.BLACK
            ))
            val telefono = Paragraph("Celular: "+celular.celular,FontFactory.getFont("arial",10f,
                BaseColor.BLACK
            ))
            val tablaDatos =  PdfPTable (3)
            val cellNombre = PdfPCell(nombre)
            val cellCi = PdfPCell(ci)
            val cellCelular = PdfPCell(telefono)
            cellNombre.border = Rectangle.NO_BORDER
            cellCi.border = Rectangle.NO_BORDER
            cellCelular.border = Rectangle.NO_BORDER
            tablaDatos.addCell(cellNombre)
            tablaDatos.addCell(cellCi)
            tablaDatos.addCell(cellCelular)
            documento.add(tablaDatos)
            documento.add(espacio)
            val tabla =PdfPTable(4)
            tabla.addCell("Modelo Celular")
            tabla.addCell("Reparacion")
            tabla.addCell("Observaciones")
            tabla.addCell("Precio")
            tabla.addCell(celular.modelo)
            tabla.addCell(celular.reparacion)
            tabla.addCell(celular.observaciones)
            tabla.addCell(celular.precio + " Bs.")
            documento.add(tabla)
            documento.add(espacio)
            val agradecimiento = Paragraph("¡GRACIAS POR SU PREFERENCIA!",FontFactory.getFont("arial",15f,
                Font.BOLD,
                BaseColor.BLACK
            ))
            val nota = Paragraph("NOTA: Tener en cuenta lo siguiente: Conservar y presentar comprobante para retirar el teléfono celular.",FontFactory.getFont("arial",8f,
                Font.BOLD,
                BaseColor.BLACK
            ))
            val importante1 = Paragraph("IMPORTANTE:  Por 30 dias calendario se podrá reclamar el dispositivo sin perder derecho alguno, pasado ese tiempo se dispondría de el sin previo aviso declarándose como abandonado para cubrir cualquier gasto generado o no cubierto por su parte.",FontFactory.getFont("arial",8f,
                Font.BOLD,
                BaseColor.BLACK
            ))
            val importante2 = Paragraph("IMPORTANTE: Garantía de 30 dias para pantallas   (originales) solo por falla del repuesto, no debe presentar rayones, golpes, presión ni humedad.",FontFactory.getFont("arial",8f,
                Font.BOLD,
                BaseColor.BLACK
            ))
            val importante3 = Paragraph("IMPORTANTE: No se aceptaran reclamos por fallas que no pudieren ser detectadas en equipos que ingresen apagados, sin batería, sin táctil, sin imagen, mojados, con rastro de humedad o cualquier otro estado que impida probar o diagnosticar el equipo correctamente.",FontFactory.getFont("arial",8f,
                Font.BOLD,
                BaseColor.BLACK
            ))
            val importante4 = Paragraph("IMPORTANTE: Los equipos que son llevados a otros servicios técnicos no contarán con garantia.",FontFactory.getFont("arial",8f,
                Font.BOLD,
                BaseColor.BLACK
            ))
            agradecimiento.alignment = Element.ALIGN_CENTER
            documento.add(agradecimiento)
            documento.add(espacio)
            documento.add(nota)
            documento.add(importante1)
            documento.add(importante2)
            documento.add(importante3)
            documento.add(importante4)
            documento.close()

            if (ContextCompat.checkSelfPermission(requireActivity().applicationContext,SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.SEND_SMS),1)
            }else{
                var uriDocument : Uri  = FileProvider.getUriForFile(this.context as Activity,"com.example.factureroreparacioncelular.fileprovider",mFilePath)
                val sendIntent : Intent = Intent()
                sendIntent.setAction(Intent.ACTION_SEND)
                sendIntent.setType("application/*")
                sendIntent.setPackage("com.whatsapp")

                if (mFilePath != null){
                    var numTelefono = "591"+ celular.celular
                    sendIntent.putExtra(Intent.EXTRA_STREAM,uriDocument)
                    sendIntent.putExtra("jid", numTelefono + "@s.whatsapp.net");
                    try {
                        startActivity(sendIntent)
                        siguienteAñadir2.text = "Terminar"
                        pfdEnviado = true
                    }catch (e:Exception){
                        Log.e("cual es",e.toString())
                    }
                }
            }
        }catch (e:Exception){
            Log.e("nodaaaa",e.toString())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    crearPDF(celular.codigo,celular.encargado)
                }else{
                    Toast.makeText(this.context, "no se pudol", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkPermision(): Boolean {
        val permiso1 = ContextCompat.checkSelfPermission(requireActivity().applicationContext,WRITE_EXTERNAL_STORAGE)
        val permiso2 = ContextCompat.checkSelfPermission(requireActivity().applicationContext,READ_EXTERNAL_STORAGE)
        return permiso1 == PackageManager.PERMISSION_GRANTED && permiso2 == PackageManager.PERMISSION_GRANTED
    }

    private lateinit var file : File

    private fun createPhotoFile(){
        val dir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        file = File.createTempFile("IMG_${System.currentTimeMillis()}_",".jpg",dir)
    }

    var bitmap: Bitmap? = null

    val abrirCamara = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            bitmap = BitmapFactory.decodeFile(file.toString())
            FotoCelular.setImageBitmap(bitmap)
            FotoCelular.rotation = 90F
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sacar_foto, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun generarCodigo(cliente: String?, modelo: String?): String {
        val año: Int = LocalDateTime.now().year
        val mes: Int = LocalDateTime.now().monthValue
        val dia: Int = LocalDateTime.now().dayOfMonth
        val fecha = "$año$mes$dia"
        val sinCliente: String = cliente!!.replace(" ", "").toUpperCase();
        val sinModelo: String = modelo!!.replace(" ", "").toUpperCase();
        return "$fecha$sinCliente$sinModelo"
    }

    lateinit var storageReference : StorageReference
}