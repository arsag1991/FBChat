package com.example.fbchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fbchat.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
        lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Write a message to the database
        val database = Firebase.database("https://project-8399933669361248125-default-rtdb.europe-west1.firebasedatabase.app")
        val myRef = database.getReference("message")
        binding.bSend.setOnClickListener {
            myRef.setValue(binding.edMessage.text.toString())
        }
        onChangeListener(myRef)

    }
    private fun onChangeListener(dRef: DatabaseReference){
        dRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.tvView.append("Atsamaz: ${snapshot.value.toString()}")
                binding.tvView.append("\n")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}