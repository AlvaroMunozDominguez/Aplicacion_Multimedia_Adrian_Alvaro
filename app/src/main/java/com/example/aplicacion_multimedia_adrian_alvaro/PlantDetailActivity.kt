package com.example.aplicacion_multimedia_adrian_alvaro

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion_multimedia_adrian_alvaro.Plant.Plant
import com.example.aplicacion_multimedia_adrian_alvaro.databinding.ActivityPlantDetailBinding
import com.google.firebase.database.*
import java.io.File

class PlantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantDetailBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el ID de la planta del Intent
        val plantId = intent.getIntExtra("plant_id", -1)

        if (plantId == -1) {
            Toast.makeText(this, "Error: Planta no encontrada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        //Referencia a Firebase Database
        databaseRef = FirebaseDatabase.getInstance("https://plantitas-8b08a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Plantitas").child(plantId.toString())

        //Obtener datos de la planta desde Firebase
        loadPlantDetails()
    }

    private fun loadPlantDetails() {
        databaseRef.get().addOnSuccessListener { snapshot ->
            val plant = snapshot.getValue(Plant::class.java)
            if (plant != null) {
                showPlantDetails(plant)
            } else {
                Toast.makeText(this, "Error al cargar la planta", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error de conexión con Firebase", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showPlantDetails(plant: Plant) {
        binding.txtNomPlanta.text = plant.nombre
        binding.txtSubNombre.text = plant.latin
        binding.txtClima.text = plant.clima
        binding.txtPais.text = plant.pais
        binding.txtEdad.text = plant.edad.toString()
        binding.txtCantRiego.text = plant.cantidadRiego
        binding.txtExtraInfo.text = plant.extraInfo

        //Cargar imagen desde almacenamiento local
        val imgFile = File(plant.imageUrl)
        if (imgFile.exists()) {
            binding.ivPlant.setImageURI(Uri.fromFile(imgFile))
        } else {
            binding.ivPlant.setImageResource(R.drawable.olivo) //Imagen por defecto si no se encuentra
        }
    }
}
