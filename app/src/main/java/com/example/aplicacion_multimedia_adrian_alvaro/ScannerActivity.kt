package com.example.aplicacion_multimedia_adrian_alvaro

import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.os.Handler
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion_multimedia_adrian_alvaro.Plant.Plant
import com.example.aplicacion_multimedia_adrian_alvaro.databinding.ActivityScannerBinding
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScannerBinding
    private lateinit var photoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        changeCVSize(100)
        checkPermissions()
        setupSpinner()

        binding.btnAnadir.setOnClickListener{
            val imagePath = saveImageLocally() ?: "" //Si hay error, usa un string vacío //Guarda la imagen en la carpeta de la app

            val plant = Plant(
                id = System.currentTimeMillis().toInt(),
                nombre = binding.txtNomPlanta.text.toString(),  //Nombre científico
                latin = binding.txtSubNombre.text.toString(),  //Nombre común
                clima = binding.spinnerClima.selectedItem.toString(),
                pais = binding.txtPais.text.toString(),
                edad = binding.txtEdad.text.toString().toIntOrNull() ?: 0,  //Edad
                cantidadRiego = binding.txtCantRiego.text.toString(),  //Cantidad de riego
                imageResId = 0, // No lo necesitamos si usamos rutas locales
                imageUrl = imagePath, //Guardamos la ruta local de la imagen
                extraInfo = binding.txtExtraInfo.text.toString() //Información extra
            )

            val databaseRef = FirebaseDatabase.getInstance("https://plantitas-8b08a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Plantitas")
            databaseRef.child(plant.id.toString()).setValue(plant)
                .addOnSuccessListener {
                    Toast.makeText(this, "Planta guardada correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveImageLocally(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES) // Carpeta interna de la app
        val imageFile = File(storageDir, fileName)

        contentResolver.openInputStream(photoUri)?.use { inputStream ->
            FileOutputStream(imageFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return imageFile.absolutePath // Ruta de la imagen guardada
    }


    private fun setupSpinner() {
        //Definimos un array con los climas.
        val biomas = arrayOf(
            "Desierto", "Bosque templado", "Selva tropical", "Pradera", "Estepa",
            "Mediterraneo", "Tundra", "Sabana", "Manglar", "Marisma",
            "Bosque lluvioso", "Alpino", "Taiga", "Chaparral", "Pantano"
        )
        //Creamos un ArrayAdapter usando el array de climas.
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            biomas
        )

        //Especificamos cómo se verá el item seleccionado.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Establecemos el adapter al Spinner
        binding.spinnerClima.adapter = adapter

        //Añadimos un OnItemSelectedListener para escuchar las selecciones
        binding.spinnerClima.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedClima = parent.getItemAtPosition(position).toString()
                //Toast.makeText(this@ScannerActivity, "Clima seleccionado: $selectedClima", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(android.Manifest.permission.CAMERA)
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, 100)
        } else {
            loadScanner()
        }
    }

    private fun loadScanner() {
        photoUri = createImageFileUri()
        takePictureLauncher.launch(photoUri)
    }

    private fun createImageFileUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_$timeStamp.jpg")
        return FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.ivImagen.setImageURI(photoUri)
            changeCVSizeWithDelay(500)
        } else {
            Toast.makeText(this, "Error al tomar la foto", Toast.LENGTH_SHORT).show()
        }
    }


    //region CVSIZE
    //Método para animar el cambio de tamaño del CardView
    private fun animateCVSizeChange(size: Int) {
        val layoutParams = binding.cvElementos.layoutParams
        val density = resources.displayMetrics.density
        val targetHeight = (size * density).toInt()

        val animator = ValueAnimator.ofInt(layoutParams.height, targetHeight)
        animator.duration = 500  //Duración de la animación (500 ms)
        animator.interpolator = DecelerateInterpolator()  //Interpolador para que sea suave

        animator.addUpdateListener { animation ->
            layoutParams.height = animation.animatedValue as Int
            binding.cvElementos.layoutParams = layoutParams
        }

        animator.start()  // Iniciar la animación
    }

    private fun changeCVSizeWithDelay(size: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.txtTitle.text = "Añadir nueva planta"
            animateCVSizeChange(size)
        }, 2000)

     }

    //Método para cambiar el tamaño del CardView
    private fun changeCVSize(size: Int) {
        val layoutParams = binding.cvElementos.layoutParams
        val density = resources.displayMetrics.density
        val heightInPixels = (size * density).toInt()
        layoutParams.height = heightInPixels
        binding.cvElementos.layoutParams = layoutParams
    }
    //endregion
}
