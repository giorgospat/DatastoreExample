package gr.patronas.datastoreexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import gr.patronas.datastoreexample.datastore.BookmarkDataStore
import gr.patronas.datastoreexample.datastore.model.BookmarkModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var bookmarkDataStore: BookmarkDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookmarkDataStore = BookmarkDataStore(this)

        buttonSaveBookmark.setOnClickListener {
            val bookmarkName = editTextBookmarkName.text.toString().trim()
            val bookmarkNotes = editTextBookmarkNotes.text.toString().trim()

            lifecycleScope.launch {
                bookmarkDataStore.saveBookmark(
                    BookmarkModel(
                        name = bookmarkName,
                        notes = bookmarkNotes
                    )
                )
            }
        }

        bookmarkDataStore.bookmark.asLiveData().observe(this, {
            textViewCurrentBookmarkName.text = it.name
            textViewCurrentBookmarkNotes.text = it.notes
        })

    }

}