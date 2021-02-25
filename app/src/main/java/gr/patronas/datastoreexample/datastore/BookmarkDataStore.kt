package gr.patronas.datastoreexample.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import gr.patronas.datastoreexample.Bookmark
import gr.patronas.datastoreexample.datastore.model.BookmarkModel
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class BookmarkDataStore(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Bookmark>

    init {
        dataStore = applicationContext.createDataStore(
            fileName = "bookmark.pb",
            serializer = BookmarkSerializer
        )
    }

    val bookmark = dataStore.data
        .map { bookmarkSchema ->
            bookmarkSchema
        }

    suspend fun saveBookmark(bookmark: BookmarkModel) {
        dataStore.updateData { currentBookmark ->
            currentBookmark.toBuilder()
                .setName(bookmark.name)
                .setNotes(bookmark.notes)
                .build()
        }
    }

    object BookmarkSerializer : Serializer<Bookmark> {
        override fun readFrom(input: InputStream): Bookmark {
            try {
                return Bookmark.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }
        override fun writeTo(
            t: Bookmark,
            output: OutputStream
        ) = t.writeTo(output)
        override val defaultValue: Bookmark
            get() = Bookmark.getDefaultInstance()
    }

}