package com.example.aplicacion_multimedia_adrian_alvaro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacion_multimedia_adrian_alvaro.Plant.Plant
import com.example.aplicacion_multimedia_adrian_alvaro.Plant.PlantAdapter
import com.example.aplicacion_multimedia_adrian_alvaro.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var plants: List<Plant>? = null
    private lateinit var adapter: PlantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manejar los márgenes para UI moderna
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar RecyclerView con PlantAdapter
        setupRecyclerView()

        // Botón para ir a ScannerActivity
        binding.btnScanner.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openPlantDetail(plant: Plant) {
        val intent = Intent(this, PlantDetailActivity::class.java).apply {
            putExtra("plant_id", plant.id) //Solo pasamos el ID, cogemos el resto de info de la BD.
        }
        startActivity(intent)
    }


    private fun setupRecyclerView() {
        binding.rvPlants.layoutManager = LinearLayoutManager(this)
        adapter = PlantAdapter(emptyList()) { plant -> openPlantDetail(plant) }
        binding.rvPlants.adapter = adapter  //Se inicializa vacío

        val databaseRef = FirebaseDatabase.getInstance("https://plantitas-8b08a-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Plantitas")

        //Escuchar cambios en tiempo real en Firebase
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val plantList = mutableListOf<Plant>()

                snapshot.children.forEach { data ->
                    val plant = data.getValue(Plant::class.java)
                    if (plant != null) {
                        plantList.add(plant)
                        Log.d("FIREBASE", "Planta agregada: ${plant.nombre}") //Debugging
                    }
                }

                Log.d("FIREBASE", "Total de plantas cargadas: ${plantList.size}") //Debugging
                adapter.updateList(plantList)  //Actualiza la lista sin recargar el RecyclerView completo.
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FIREBASE", "Error al obtener datos: ${error.message}")
            }
        })
    }

}
