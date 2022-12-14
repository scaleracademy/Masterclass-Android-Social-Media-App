package tech.arnav.chirpy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tech.arnav.chirpy.databinding.ActivityMainBinding
import tech.arnav.chirpy.models.Post

class MainActivity : AppCompatActivity() {
    lateinit var _binding: ActivityMainBinding
    val auth = Firebase.auth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        _binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        val signInLauncher =
            registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
                if (res.resultCode == RESULT_OK) {
                    Toast.makeText(this, "User signed in", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }

        if (auth.currentUser == null) {
            Toast.makeText(this, "Not logged in, starting login flow", Toast.LENGTH_SHORT).show()
            signInLauncher.launch(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(listOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.PhoneBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                    .build()
            )
        } else {
            val postsAdapter = PostsAdapter()
            _binding.rvPostList.layoutManager = LinearLayoutManager(this)
            _binding.rvPostList.adapter = postsAdapter

            val query = db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val posts = snapshot.documents.map { it.toObject(Post::class.java) }
                        posts.forEach { it?.let {
                            Log.d("POST", """
                                ${it.message}
                                ${it.uid}
                            """.trimIndent())
                        }}
                        postsAdapter.submitList(posts)
                    }
                }

            _binding.btnSend.setOnClickListener {
                val message = _binding.etMessage.text.toString()

                val post = Post(message, auth.currentUser!!.uid, System.currentTimeMillis())

                db.collection("posts")
                    .add(post)
                    .addOnSuccessListener { docRef ->
                        Toast.makeText(this, "Post Saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener() { e ->
                        Toast.makeText(this, "Failed to save post", Toast.LENGTH_SHORT).show()
                    }
            }


        }




    }
}