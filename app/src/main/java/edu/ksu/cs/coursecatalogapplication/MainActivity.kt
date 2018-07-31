package edu.ksu.cs.coursecatalogapplication

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.card_view.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManger: RecyclerView.LayoutManager

    private var searchInputs = mutableMapOf<String, Any>()

    class Course(val cardTitle: List<String>, val details: List<String>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // load sql data into cursor
        val cursor = getData("", emptyArray())

        generateCourseList(cursor)
    }

    private fun generateCourseList(cursor: Cursor) {
        val data = mutableListOf<String>()
        val details = mutableListOf<String>()
        while (cursor.moveToNext()) {
            data.add("${cursor.getString(1)}${cursor.getString(0)} ${cursor.getString(2)}")
            details.add("\nDescription: ${cursor.getString(3)}\n\n" +
                    "Hours: ${cursor.getString(4)}\n\n" +
                    "Requisites: ${cursor.getString(5)}\n\n" +
                    "KSTATE8: ${cursor.getString(6)}")
        }

        val course = Course(data, details)

        viewManger = LinearLayoutManager(this)
        viewAdapter = CourseViewAdapter(course, this)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManger
            adapter = viewAdapter
        }
    }

    fun getData(selection: String, selectionKey: Array<String>): Cursor{
        return contentResolver.query(
                CourseCatalog.Course.CONTENT_URI,
                arrayOf(
                        CourseCatalogContract.CourseListing.COLUMN_NAME_NUMBER,
                        CourseCatalogContract.CourseListing.COLUMN_NAME_PREFIX,
                        CourseCatalogContract.CourseListing.COLUMN_NAME_TITLE,

                        CourseCatalogContract.CourseListing.COLUMN_NAME_DESCRIPTION,
                        CourseCatalogContract.CourseListing.COLUMN_NAME_MAXHOURS,
                        CourseCatalogContract.CourseListing.COLUMN_NAME_REQUISITES,
                        CourseCatalogContract.CourseListing.COLUMN_NAME_KSTATE8
                ),
                selection,
                selectionKey,
                "prefix, number",
                null
        )
    }

    fun launchDetails(view: View){


    }

    fun launchSearchAndFilter(view: View){
        val linearLayout = LinearLayout(this)


        val keyWord = EditText(this)
        keyWord.setHint("Key Word")
        val coursePrefix = EditText(this)
        coursePrefix.setHint("PREFIX")
        val minCourseNumber = EditText(this)
        minCourseNumber.setHint("Min #")
        val maxCourseNumber = EditText(this)
        maxCourseNumber.setHint("Max #")

        linearLayout.addView(keyWord)
        linearLayout.addView(coursePrefix)
        linearLayout.addView(minCourseNumber)
        linearLayout.addView(maxCourseNumber)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Search/Filter")
        builder.setView(linearLayout)

        builder.setPositiveButton("Go"){ dialog, which ->
            var selection = StringBuilder()
            var selectionKey = mutableListOf<String>()

            if(keyWord.text.isNotEmpty()){
                selection.append("title LIKE '%${keyWord.text.toString().toUpperCase()}%' " +
                        "OR description LIKE '%${keyWord.text.toString().toUpperCase()}%'")
                //selectionKey.add(keyWord.text.toString())
                //selectionKey.add(keyWord.text.toString())
            }
            if(minCourseNumber.text.isNotEmpty()){
                if(selection.isNotEmpty()) selection.append(" AND ")
                selection.append("number>?")
                selectionKey.add(minCourseNumber.text.toString())
            }
            if(maxCourseNumber.text.isNotEmpty()){
                if(selection.isNotEmpty()) selection.append(" AND ")
                selection.append("number<?")
                selectionKey.add(maxCourseNumber.text.toString())
            }
            if(coursePrefix.text.isNotEmpty()){
                if(selection.isNotEmpty()) selection.append(" AND ")
                selection.append("prefix=?")
                selectionKey.add(coursePrefix.text.toString().toUpperCase())
            }

            var cursor = getData(selection.toString(), selectionKey.toTypedArray())

            generateCourseList(cursor)

            selection = StringBuilder()
            selectionKey.clear()
        }

        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.setNeutralButton("Reset"){ dialog, which ->
            val cursor = getData("", emptyArray())
            generateCourseList(cursor)
        }

        builder.show()
    }
}

class CourseViewAdapter(val dataSet : MainActivity.Course, val context: Context): RecyclerView.Adapter<CourseViewAdapter.ViewHolder>(){

    class ViewHolder(val courseCard: CardView): RecyclerView.ViewHolder(courseCard)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false) as CardView
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courseCard.relativeLayout.course_textView.text = dataSet.cardTitle[position]
        holder.courseCard.relativeLayout.course_textView.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val textView = TextView(context)
            textView.text = dataSet.details[position]
            textView.setPadding(15,15,15,15)
            builder.setTitle(dataSet.cardTitle[position])
            builder.setView(textView)
            builder.setNegativeButton("OK") { dialog, which -> dialog.cancel() }
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.cardTitle.size
    }
}
