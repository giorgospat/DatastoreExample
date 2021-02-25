package gr.enginius.datastoreexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import gr.enginius.datastoreexample.datastore.UserPreferences
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPreferences = UserPreferences(this)

        buttonSaveBookmark.setOnClickListener {
            val bookmark = editTextBookmark.text.toString().trim()
            lifecycleScope.launch {
                userPreferences.saveBookmark(bookmark)
            }
        }

        userPreferences.bookmark.asLiveData().observe(this, {
            textViewCurrentBookmark.text = it
        })

    }
    
}