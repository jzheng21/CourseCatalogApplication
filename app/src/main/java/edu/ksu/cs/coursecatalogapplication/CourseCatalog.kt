package edu.ksu.cs.coursecatalogapplication

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class CourseCatalog : ContentProvider() {

    lateinit var nCourses: Array<CourseListing>

    private lateinit var mDbHelper: CourseCatalogDbHelper

    // CourseCatalog Constants
    companion object {
        val AUTHORITY = "edu.ksu.cs.coursecatalog.provider"
        val BASE_URL = Uri.parse("content://$AUTHORITY")
    }

    object Course{
        const val PATH = "courses"
        const val PATH_FOR_ID = "courses/#"

        var CONTENT_URI = BASE_URL.buildUpon().appendEncodedPath(PATH).build()
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        uriMatcher.addURI(AUTHORITY, Course.PATH, 1)
        uriMatcher.addURI(AUTHORITY, Course.PATH_FOR_ID, 2)
    }

    override fun onCreate(): Boolean {
        mDbHelper = CourseCatalogDbHelper(this.context)
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO("Implement this to handle requests for the MIME type of the data" +
                "at the given URI")
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val db = mDbHelper.readableDatabase

        when (uriMatcher.match(uri)) {
            1 -> { // All Courses
                return db.query(
                        CourseCatalogContract.CourseListing.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                )
            }
            2 -> { // Specific course
            }
        }
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
