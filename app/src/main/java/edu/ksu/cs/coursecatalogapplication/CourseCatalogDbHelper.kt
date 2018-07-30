package edu.ksu.cs.coursecatalogapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object CourseCatalogContract{
    object CourseListing: BaseColumns{
        const val TABLE_NAME = "courses"
        const val COLUMN_NAME_UGRAD = "ugrad"
        const val COLUMN_NAME_GRAD = "grad"
        const val COLUMN_NAME_PREFIX = "prefix"
        const val COLUMN_NAME_NUMBER = "number"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_MINHOURS = "minHours"
        const val COLUMN_NAME_MAXHOURS = "maxHours"
        const val COLUMN_NAME_VARIABLEHOURS = "variableHours"
        const val COLUMN_NAME_REQUISITES = "requisites"
        const val COLUMN_NAME_SEMESTERS = "semesters"
        const val COLUMN_NAME_UGE = "uge"
        const val COLUMN_NAME_KSTATE9 = "kstate8"
    }
}

class CourseCatalogDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    private val SQL_CREATE_ENTRIES = context.resources.openRawResource(R.raw.create_course_table)
            .bufferedReader().use { it.readText() }

    private val SQL_DELETE_ENTRIES = context.resources.openRawResource(R.raw.destroy_course_data)
            .bufferedReader().use { it.readText() }

    private val SQL_SEED_ENTRIES = context.resources.openRawResource(R.raw.seed_course_data)
            .bufferedReader().use { it.readText() }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
        db?.execSQL(SQL_SEED_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        db?.execSQL(SQL_CREATE_ENTRIES)
        db?.execSQL(SQL_SEED_ENTRIES)
    }

    companion object {
        const val DATABASE_NAME = "course_catalog"
        const val DATABASE_VERSION = 2
    }
}