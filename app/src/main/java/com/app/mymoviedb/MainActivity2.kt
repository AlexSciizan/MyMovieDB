package com.app.mymoviedb

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import java.util.concurrent.ExecutionException

class MainActivity2 : AppCompatActivity(), View.OnClickListener {
    var isLoading = false
    lateinit var buttons: Array<Button?>
    var layout: LinearLayout? = null
    var recyclerViewAdapterMovie: RecyclerviewAdapterMovie? = null
    var recyclerView: RecyclerView? = null
    var page = 1
    var recent_genre: String? = null
    var recent_id_genre: String? = null
    val API_GENRE =
        "https://api.themoviedb.org/3/genre/movie/list?api_key=5edbd3e1a85cfef0cdd0fa429f3809cd&language=en-US"
    val API_LIST_MOVIE_BY_GENRE =
        "https://api.themoviedb.org/3/discover/movie?api_key=5edbd3e1a85cfef0cdd0fa429f3809cd&include_adult=false&include_video=false&language=en-US&sort_by=popularity.desc" //+&with_genres=action&page=1
    val API_DETAILS_MOVIE =
        "https://api.themoviedb.org/3/movie/" //614479?language=en-US&api_key=5edbd3e1a85cfef0cdd0fa429f3809cd
    var genreObj: JsonObject? = null
    var list_genreObj: JsonObject? = null

    /*
    "https://api.themoviedb.org/3/movie/movie_id/lists?language=en-US&page=1"
    "https://api.themoviedb.org/3/movie/"+buttons[i].getTag()+"/lists?language=en-US&page=1"*/
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        layout = findViewById(R.id.layout)
        recyclerView = findViewById(R.id.recyclerview)
        rowListItem = ArrayList()
        try {
            genreObj = Ion.with(this)
                .load(API_GENRE)
                .setHeader("Accept", "application/json")
                .asJsonObject().get()
//           final var jml_genre = genreObj.get("genres").asJsonArray.size()
            buttons = arrayOfNulls(Ion.with(this)
                .load(API_GENRE)
                .setHeader("Accept", "application/json")
                .asJsonObject().get().get("genres").asJsonArray.size())
            for (i in buttons.indices) {
                buttons[i] = Button(this)
                buttons[i]!!.text =
                    "" + Ion.with(this)
                        .load(API_GENRE)
                        .setHeader("Accept", "application/json")
                        .asJsonObject().get().get("genres").asJsonArray[i].asJsonObject["name"].asString
                buttons[i]!!.tag =
                    "" + Ion.with(this)
                        .load(API_GENRE)
                        .setHeader("Accept", "application/json")
                        .asJsonObject().get().get("genres").asJsonArray[i].asJsonObject["id"].asInt
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(10, 10, 10, 10)
                buttons[i]!!.setPadding(40, 20, 40, 20)
                buttons[i]!!.layoutParams = params
                buttons[i]!!.setOnClickListener(this)
                buttons[i]!!.background = setRoundRect(9, 1, "#000000", "#ffffff")
                buttons[i]!!.transformationMethod = null
                layout?.addView(buttons[i])
                Log.d(
                    "genre n id", buttons[i]!!
                        .text.toString() + " " + buttons[i]!!.tag
                )
            }
            buttons[0]!!.background = setRoundRect(9, 1, "#000000", "#06786e")
            recent_genre = buttons[0]!!.text as String
            recent_id_genre = buttons[0]!!.tag as String
            list_genreObj = Ion.with(this)
                .load("$API_LIST_MOVIE_BY_GENRE&with_genres=$recent_id_genre&page=$page")
                .setHeader("Accept", "application/json")
                .asJsonObject().get() //+&with_genres=action&page=1
            for (j in 0 until list_genreObj?.getAsJsonObject()?.get("results")?.asJsonArray!!.size()) {
                rowListItem?.add(
                    ItemObject(
                        getImg(
                            this, "https://image.tmdb.org/t/p/w220_and_h330_face/"
                                    + list_genreObj!!.getAsJsonObject()["results"].asJsonArray[j].asJsonObject["backdrop_path"].asString
                        ), arrayOf(
                            list_genreObj!!.getAsJsonObject()["results"].asJsonArray[j].asJsonObject["id"].asString,
                            list_genreObj!!.getAsJsonObject()["results"].asJsonArray[j].asJsonObject["title"].asString,
                            list_genreObj!!.getAsJsonObject()["results"].asJsonArray[j].asJsonObject["release_date"].asString
                        )
                    )
                )
            }
            recyclerViewAdapterMovie = RecyclerviewAdapterMovie(rowListItem)
            recyclerView!!.setLayoutManager(GridLayoutManager(this, 2))
            recyclerView!!.setAdapter(recyclerViewAdapterMovie)
            recyclerView!!.setHasFixedSize(true)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    val linearLayoutManager = recyclerView.layoutManager as GridLayoutManager?
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowListItem!!.size - 1) {
//                        rowListItem.add(null);
//                        recyclerViewAdapterMovie.notifyItemInserted(rowListItem.size() - 1);
                        val handler = Handler()
                        handler.postDelayed({
                            try {
                                page += 1
                                list_genreObj = Ion.with(recyclerView.context)
                                    .load("$API_LIST_MOVIE_BY_GENRE&with_genres=$recent_id_genre&page=$page")
                                    .setHeader("Accept", "application/json")
                                    .asJsonObject().get() //+&with_genres=action&page=1
                                println("ini url $API_LIST_MOVIE_BY_GENRE&with_genres=$recent_id_genre&page=$page")
                                println("ini obj " + list_genreObj.toString())
                                if (list_genreObj != null) for (j in 0 until list_genreObj!!.asJsonObject["results"].asJsonArray.size()) {
                                    rowListItem!!.add(
                                        ItemObject(
                                            getImg(
                                                recyclerView.context,
                                                "https://image.tmdb.org/t/p/w220_and_h330_face/"
                                                        + list_genreObj!!.asJsonObject["results"].asJsonArray[j].asJsonObject["backdrop_path"].asString
                                            ), arrayOf(
                                                list_genreObj!!.asJsonObject["results"].asJsonArray[j].asJsonObject["id"].asString,
                                                list_genreObj!!.asJsonObject["results"].asJsonArray[j].asJsonObject["title"].asString,
                                                list_genreObj!!.asJsonObject["results"].asJsonArray[j].asJsonObject["release_date"].asString
                                            )
                                        )
                                    )
                                }
                                //                                recyclerViewAdapterMovie = new RecyclerviewAdapterMovie(rowListItem);
//                                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
//                                recyclerView.setAdapter(recyclerViewAdapterMovie);
                                recyclerViewAdapterMovie!!.notifyDataSetChanged()
                            } catch (e: ExecutionException) {
                                e.printStackTrace()
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            } catch (e: UnsupportedOperationException) {
                                e.printStackTrace()
                            }
                            isLoading = false
                        }, 2000)
                        isLoading = true
                    }
                }
            }
        })
    }

    /*api_key=5edbd3e1a85cfef0cdd0fa429f3809cd&language
    header='Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZWRiZDNlMWE4NWNmZWYwY2RkMGZhNDI5ZjM4MDljZCIsInN1YiI6IjYxNTZiNDZjMWM2MzViMDAyYTE5ZTAxNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wPCqUuAXsejf8aI5ci6vPGi5OGz1nHFwFTb_qhhvBm0' \
    header='accept: application/json'*/
    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(view: View) {
        val button = view as Button
        for (btns in buttons) {
            if (btns!!.text.toString().matches(("" + button.text).toRegex())) {
                btns.background = setRoundRect(9, 1, "#000000", "#06786e")
                try {
                    page = 1
                    rowListItem = ArrayList()
                    list_genreObj = Ion.with(this)
                        .load(API_LIST_MOVIE_BY_GENRE + "&with_genres=" + btns.tag + "&page=" + page)
                        .setHeader("Accept", "application/json")
                        .asJsonObject().get() //+&with_genres=action&page=1
                    println("ini url " + API_LIST_MOVIE_BY_GENRE + "&with_genres=" + btns.tag + "&page=" + page)
                    println("ini obj " + list_genreObj.toString())
                    for (j in 0 until list_genreObj!!.getAsJsonObject()["results"].asJsonArray.size()) {
                        rowListItem!!.add(
                            ItemObject(
                                getImg(
                                    this, "https://image.tmdb.org/t/p/w220_and_h330_face/"
                                            + list_genreObj!!.getAsJsonObject()["results"].asJsonArray[j].asJsonObject["backdrop_path"].asString
                                ), arrayOf(
                                    list_genreObj!!.getAsJsonObject()["results"].asJsonArray[j].asJsonObject["id"].asString,
                                    list_genreObj!!.getAsJsonObject()["results"].asJsonArray[j].asJsonObject["title"].asString,
                                    list_genreObj!!.getAsJsonObject()["results"].asJsonArray[j].asJsonObject["release_date"].asString
                                )
                            )
                        )
                    }
                    recyclerViewAdapterMovie = RecyclerviewAdapterMovie(rowListItem)
                    recyclerView!!.layoutManager = GridLayoutManager(this, 2)
                    recyclerView!!.adapter = recyclerViewAdapterMovie
                    recyclerViewAdapterMovie!!.notifyDataSetChanged()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } else {
                btns.background = setRoundRect(9, 1, "#000000", "#ffffff")
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(10, 10, 10, 10)
            btns.layoutParams = params
            btns.setPadding(40, 20, 40, 20)
        }
    }

    companion object {
        var rowListItem: MutableList<ItemObject>? = null
        fun setRoundRect(
            rad: Int,
            line_width: Int,
            strokeColor: String?,
            feelColor: String?
        ): GradientDrawable {
            val gd = GradientDrawable()
            gd.setColor(Color.parseColor(feelColor))
            gd.cornerRadius = rad.toFloat()
            gd.setStroke(line_width, Color.parseColor(strokeColor))
            return gd
        }

        @Throws(ExecutionException::class, InterruptedException::class)
        fun getImg(context: Context?, url: String?): Bitmap {
            return Ion.with(context)
                .load(url)
                .withBitmap()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .asBitmap().get()
        }
    }
}