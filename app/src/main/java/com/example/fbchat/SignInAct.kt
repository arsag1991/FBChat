package com.example.fbchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.fbchat.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.credentials.IdToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInAct : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val accaunt = task.getResult(ApiException::class.java)
                if (accaunt != null){firebaseAuthWithGoogle(accaunt.idToken!!)}
            }catch (a: ApiException){Toast.makeText(this, "API exception", Toast.LENGTH_SHORT).show()}
        }
        binding.bSignin.setOnClickListener {
            signInWithGoogle()
        }
    }
    private fun getClient (): GoogleSignInClient{
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }
    private fun signInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }
    private fun firebaseAuthWithGoogle (idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful)
            {Toast.makeText(this, "Authentication is successful", Toast.LENGTH_SHORT).show()}
            else
            {Toast.makeText(this, "Authentication error", Toast.LENGTH_SHORT).show()}


        }
    }
}