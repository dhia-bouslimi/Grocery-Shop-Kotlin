package com.example.shop.Utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.preference.PreferenceManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.Data.Produit
import com.example.shop.R

import java.io.ByteArrayOutputStream


class RecommendedAdapter(var context: Context,val limit: Int) :
    RecyclerView.Adapter<RecommendedViewHolder>() {

    var dataList = mutableListOf<Produit>()
    var filteredPostsList= ArrayList<Produit>()
    val ReadyFunction = ReadyFunction()


    internal fun setDataList(PostsArrayList: ArrayList<Produit>) {
        this.dataList = PostsArrayList
        this.filteredPostsList = PostsArrayList
        notifyDataSetChanged()
    }
    internal fun addDataList(data: Produit) {
        this.dataList.add(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recommended, parent, false)
        return RecommendedViewHolder(view)

    }


    //override fun getItemCount() = filteredPostsList.size


    ////////////////////////////////////

    override fun getItemCount(): Int {
        return if (filteredPostsList.size > limit) {
            limit
        } else {
            filteredPostsList.size
        }
    }
    ////////////////////////////////////

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        var data = filteredPostsList[position]

        val ImagePlace = (filteredPostsList[position].image)
       // println("Imageeeee ==>>>>> "+ImagePlace)
        val Type = filteredPostsList[position].type
        val Quantite = filteredPostsList[position].quantite
        val Prix = filteredPostsList[position].prix
        //
        Glide
            .with(context)
            .load(ImagePlace)
            .fitCenter()
            .into(holder.Image);
        //holder.PicRecomm.setImageResource(ImagePlace!!)
        holder.Type.text = Type
        holder.Quantite.text = Quantite
        holder.Prix.text = Prix
        holder.itemView.setBackgroundColor(Color.parseColor("#FAFAFA"));
        //animation Items RecyclerView
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation_fall_down)
        //holder.itemView.setVisibility(View.VISIBLE)
        holder.itemView.startAnimation(animation)
        //
        holder.itemView.setOnClickListener {
            val preferences : SharedPreferences = context.getSharedPreferences("product", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("_id", data.getID())
            editor.putString("type", data.getType())
            editor.putString("quantite", data.getQuantite())
            editor.putString("prix", data.getPrix())
            editor.putString("image", data.getPhoto())
            editor.apply()  //Save Data
          //  println("Ratteeeeeeeeeeeee "+data.id)
            ///
          //  PopUpDetails(holder.itemView)
            val factory = LayoutInflater.from(context)
            val view: View = factory.inflate(R.layout.update_product, null)
            val msg = CustomDialogs()
            msg.ShowDialogUpdateProduct(context, view)

            //ScreenShot
            val fileOutputStream = ByteArrayOutputStream()
            ReadyFunction.ScreenShot(holder.itemView)!!.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            val compressImage: ByteArray = fileOutputStream.toByteArray()
            val sEncodedImage: String = Base64.encodeToString(compressImage, Base64.DEFAULT)
            //println("sEncodedImage ===>>>>>> "+sEncodedImage)
            val preferencess = PreferenceManager.getDefaultSharedPreferences(context)
            val editorr = preferencess.edit()
            editorr.putString("ScreenShotAdmin", sEncodedImage)
            editorr.apply()  //Save Data
        }

    }
  /*  fun PopUpDetails(view: View) {
        PopUpDetailsPosts(view.context)
    }*/

   /* fun PopUpDetailsPosts(context: Context) {
/*        val factoryyy = ViewModelProvider.NewInstanceFactory()
        val viewModel: FavorisViewModel = factoryyy.create(FavorisViewModel::class.java)*/
        val androidFactory =
            ViewModelProvider.AndroidViewModelFactory(context.applicationContext as Application)
        val viewModell: FavorisViewModel = androidFactory.create(FavorisViewModel::class.java)
        //////////////////
        val Jooobbb = GlobalScope.launch(Dispatchers.Default) {
            viewModell.VerifFavoriteCoroutineScope(context)
            // delay the coroutine by 1sec
                delay(1000)
        }
        //////////////////
        runBlocking {
            Jooobbb.join()
            println("Blooocck")
            //update the UI
            val factory = LayoutInflater.from(context)
            val view: View = factory.inflate(R.layout.detailsposts, null)
            val msg = DialogFavoris()
            msg.ShowDetailsPost(context, view)
        }
        //////////////////
    }*/





}



