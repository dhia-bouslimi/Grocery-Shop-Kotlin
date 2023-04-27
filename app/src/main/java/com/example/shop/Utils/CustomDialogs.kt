package tn.yassin.discovery.Utils

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import com.example.shop.Data.Fournisseur
import com.example.shop.Network.FournisseurResponse
import com.example.shop.R

class CustomDialogs {
    var mMediaPlayer: MediaPlayer? = null
    fun SoundNotification(context: Context?) {
        mMediaPlayer = MediaPlayer.create(context, R.raw.soundialog)
        mMediaPlayer!!.start()
    }
    fun ShowDialogNoConnection(context: Context?, view: View) {
        val dialog = Dialog(context!!)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //Make it TRANSPARENT
        dialog.window!!.getAttributes().windowAnimations = R.style.DialogAnimation; //Set Animation
        dialog.show()
        SoundNotification(context)
        val btnigotit = view.findViewById<Button>(R.id.BtnGotIt) as? Button
        btnigotit?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                dialog.dismiss()
            }
        })
    }


    fun ShowDetailsNeedy(context: Context?, view: View) {
        val dialog = Dialog(context!!)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //Make it TRANSPARENT
        dialog.window!!.getAttributes().windowAnimations = R.style.DialogAnimation; //Set Animation
        dialog.getWindow()?.getAttributes()?.gravity = Gravity.BOTTOM;
        dialog.show()
       // val sound = PlayMusic()
        //sound.SoundNotification(context!!)

        /////
        val sharedPreference : SharedPreferences = context.getSharedPreferences("azz", Context.MODE_PRIVATE)

        val id = sharedPreference.getString("id", null)
        val fullName = sharedPreference.getString("fullName", null)
        val adresse = sharedPreference.getString("adresse", null)
        val secteur = sharedPreference.getString("secteur", null)
        val numTel = sharedPreference.getString("numTel", null)




        /////
        val DetailsBlood = view.findViewById<TextView>(R.id.DetailsBlood) as? TextView
        val DetailsFullName = view.findViewById<TextView>(R.id.DetailsFullName) as? TextView
        val DetailsHospitalLocation = view.findViewById<TextView>(R.id.DetailsHospitalLocation) as? TextView
        val DetailsPhoneNeedy = view.findViewById<TextView>(R.id.DetailsPhoneNeedy) as? TextView
        /////
        DetailsBlood?.text="Secteur: "+secteur
        DetailsFullName?.text="Name: "+fullName
        DetailsHospitalLocation?.text="Location: "+adresse
        DetailsPhoneNeedy?.text="Phone: "+numTel
        /////
        val DetailsCallNeedy = view.findViewById<TextView>(R.id.DetailsCallNeedy) as? TextView
        val DetailsShareNeedy = view.findViewById<TextView>(R.id.DetailsShareNeedy) as? TextView
        val CloseDetailsNeed = view.findViewById<ImageView>(R.id.CloseDetailsNeed) as? ImageView
        /////


        /////
        CloseDetailsNeed?.setOnClickListener{
            dialog.dismiss()
        }
        DetailsCallNeedy?.setOnClickListener{
            try {
                val intent = Intent(Intent.ACTION_DIAL)
               intent.data = Uri.parse("tel:$numTel")
                context.startActivity(intent)
            }catch (e: Exception) {
                println(e.printStackTrace())
            }
        }
        DetailsShareNeedy?.setOnClickListener{
            dialog.cancel()
            dialog.dismiss()
            val factory = LayoutInflater.from(context)
           // val view: View = factory.inflate(R.layout.optionsharepopup, null)
            val msg = CustomDialogs()
           // msg.ShareToHelp(context, view)

        }

    }



    /////////
  /*  fun ShowDialogInformations(context: Context?, view: View) {
        val dialog = Dialog(context!!)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //Make it TRANSPARENT
        dialog.window!!.getAttributes().windowAnimations = R.style.DialogAnimation; //Set Animation
        dialog.show()
        SoundNotification(context)
        val btnigotit = view.findViewById<Button>(R.id.BtnOkay) as? Button
        btnigotit?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                dialog.dismiss()
                //
                val sharedPref = context.getSharedPreferences("PrayerTimes", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putBoolean("FirstTimeRun", false)
                editor.apply()
                //
            }
        })
    }*/
}
interface talk {
    fun senddata(n: Fournisseur)
}
