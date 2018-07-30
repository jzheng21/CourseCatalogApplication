package edu.ksu.cs.coursecatalogapplication

import android.content.Context
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.card_view.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManger: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // load sql data into cursor
        val cursor = getData("", emptyArray())

        val data = mutableListOf<String>()
        while(cursor.moveToNext()){
            data.add("${cursor.getString(1)}${cursor.getString(0)} ${cursor.getString(2)}")
        }

        viewManger = LinearLayoutManager(this)
        viewAdapter = CourseViewAdapter(data,this)

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
                        CourseCatalogContract.CourseListing.COLUMN_NAME_DESCRIPTION
                ),
                selection,
                selectionKey,
                "prefix, number",
                null
        )
    }
}

class CourseViewAdapter(val dataSet : List<String>, val context: Context): RecyclerView.Adapter<CourseViewAdapter.ViewHolder>(){

    class ViewHolder(val courseCard: CardView): RecyclerView.ViewHolder(courseCard)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false) as CardView
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courseCard.relativeLayout.course_textView.text = dataSet[position]
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
