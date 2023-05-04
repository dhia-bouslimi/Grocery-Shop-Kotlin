package tn.yassin.discovery.Utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.shop.Data.Fournisseur
import com.example.shop.Data.Promotion
import com.example.shop.Network.UserApi
import com.example.shop.Network.retrofit
import com.example.shop.R
import com.example.shop.Utils.CustomToast
import com.example.shop.Views.Fragement.PromotionFragment
import com.google.android.material.internal.ContextUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*

class CustomDialogs(val talk: talk? = null ) {
    private lateinit var DialogNameNeedy: TextInputLayout
    private lateinit var DialogLocationNeedy: TextInputEditText
    private lateinit var DialogSecteurNeedy: TextInputLayout
    private lateinit var DialogPhoneNeedy: TextInputLayout

    private lateinit var EditNameNeedy: TextInputLayout
    private lateinit var EditLocationNeedy: TextInputLayout
    private lateinit var EditBloodNeedy: TextInputLayout
    private lateinit var EditPhoneNeedy: TextInputLayout


    private lateinit var DialogPrix: TextInputLayout
    private lateinit var Dialogduaration: TextInputLayout
    private lateinit var Dialogproduct: TextInputLayout



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
        DetailsBlood?.text="Secteur: "+numTel
        DetailsFullName?.text="Name: "+fullName
        DetailsHospitalLocation?.text="Location: "+adresse
        DetailsPhoneNeedy?.text="Phone: "+secteur
        /////
        val DetailsCallNeedy = view.findViewById<TextView>(R.id.DetailsCallNeedy) as? TextView
        val DetailsDeleteNeedy = view.findViewById<TextView>(R.id.DetailsDeleteNeedy) as? TextView
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
        DetailsDeleteNeedy?.setOnClickListener{
            dialog.cancel()
            dialog.dismiss()
            val factory = LayoutInflater.from(context)
           // val view: View = factory.inflate(R.layout.optionsharepopup, null)
            val msg = CustomDialogs()
           // msg.ShareToHelp(context, view)

        }

    }






    private fun gettextwathcerAddPromo() {
        DialogPrix?.editText?.addTextChangedListener(PrixTextWatcher)
        Dialogproduct?.editText?.addTextChangedListener(ProductTextWatcher)
        Dialogduaration?.editText?.addTextChangedListener(DuarationTextWatcher)

    }
    private val PrixTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) { validatePrix() }
    }
    private val ProductTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) { validateProduct() }
    }
    private val DuarationTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) { validateDuration() }
    }



    private fun validatePrix(): Boolean {
        if (DialogPrix?.editText?.text!!.isEmpty()) {
            DialogPrix.setError("Must Not be Empty !")
            return false
        } else {
            DialogPrix.setError(null)
            return true
        }
        return true
    }
    private fun validateProduct(): Boolean {
        if (Dialogproduct?.editText?.text!!.isEmpty()) {
            Dialogproduct.setError("Must Not be Empty !")
            return false
        } else {
            Dialogproduct.setError(null)
            return true
        }
        return true
    }
    private fun validateDuration(): Boolean {
        if (Dialogduaration?.editText?.text!!.isEmpty()) {
            Dialogduaration.setError("Must Not be Empty !")
            return false
        } else {
            Dialogduaration.setError(null)
            return true
        }
        return true
    }








    private fun gettextwathcerAddNeedy() {
        DialogNameNeedy?.editText?.addTextChangedListener(NameTextWatcher)
        DialogLocationNeedy.addTextChangedListener(LocationTextWatcher)
        DialogPhoneNeedy?.editText?.addTextChangedListener(PhoneTextWatcher)
        DialogSecteurNeedy?.editText?.addTextChangedListener(SecteurTextWatcher)

    }
    private val SecteurTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) { validateNeedyName() }
    }
    private val NameTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) { validateNeedyName() }
    }
    private val LocationTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) { validateNeedyLocation() }
    }
    private val PhoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) { validateNeedyPhone() }
    }
    private fun validateNeedyLocation(): Boolean {
        if (DialogLocationNeedy.text!!.isEmpty()) {
            DialogLocationNeedy.setError("Must Not be Empty !")
            return false
        } else {
            DialogLocationNeedy.setError(null)
            return true
        }
        return true
    }
    private fun validateNeedyName(): Boolean {
        if (DialogNameNeedy?.editText?.text!!.isEmpty()) {
            DialogNameNeedy.setError("Must Not be Empty !")
            return false
        } else {
            DialogNameNeedy.setError(null)
            return true
        }
        return true
    }
    private fun validateNeedySecteur(): Boolean {
        if (DialogSecteurNeedy?.editText?.text!!.isEmpty()) {
            DialogSecteurNeedy.setError("Must Not be Empty !")
            return false
        } else {
            DialogSecteurNeedy.setError(null)
            return true
        }
        return true
    }
    private fun validateNeedyPhone(): Boolean {
        if (DialogPhoneNeedy.editText?.text!!.isEmpty()) {
            DialogPhoneNeedy.setError("Must Not be Empty!")
            return false
        }  else if (DialogPhoneNeedy.editText?.text!!.length < 8) {
            DialogPhoneNeedy.setError("Missing "+(8-DialogPhoneNeedy.editText?.text!!.length)+" numbers")
            return false
        }
        else if (!(DialogPhoneNeedy.editText?.text!!.toString().isPhoneValid())) {
            DialogPhoneNeedy.setError("Malformed Phone Number!")
            return false
        }
        else {
            DialogPhoneNeedy.setError(null)
            return true
        }
        return true
    }

    private fun String.isPhoneValid(): Boolean {
        return !TextUtils.isEmpty(this) && Patterns.PHONE.matcher(this).matches()
    }



    fun ShowDialogAddNeedy(context: Context?, view: View) {
        val dialog = Dialog(context!!)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //Make it TRANSPARENT
        dialog.window!!.getAttributes().windowAnimations = R.style.DialogAnimation; //Set Animation
        dialog.show()



        val BtnSaveAddNeedy = view.findViewById<Button>(R.id.BtnSaveAddNeedy) as? Button
        DialogNameNeedy = view.findViewById(R.id.DialogNameNeedy)
        DialogLocationNeedy = view.findViewById(R.id.DialogLocationNeedy)
        DialogSecteurNeedy = view.findViewById(R.id.DialogSecteurNeedy)
        DialogPhoneNeedy = view.findViewById(R.id.DialogPhoneNeedy)




        /////
        val preferences : SharedPreferences = context.getSharedPreferences("azz", Context.MODE_PRIVATE)
        val MyID = preferences.getString("id", null)
        //
        ////



        ///////
        gettextwathcerAddNeedy()
        BtnSaveAddNeedy?.setOnClickListener(object : View.OnClickListener {
            @SuppressLint("RestrictedApi")
            override fun onClick(view: View?) {
                if (!validateNeedySecteur()
                    or  !validateNeedyName() or !validateNeedyLocation() or !validateNeedyPhone()
                ) {
                    CustomToast(context, "Something is empty!", "RED").show()
                    return
                }
                if(
                    validateNeedyName()&&validateNeedyLocation()&&validateNeedyPhone()
                )
                {

                    val retrofi: Retrofit = retrofit.getInstance()
                    val service: UserApi = retrofi.create(UserApi::class.java)
                    val Needy = Fournisseur(
                        "",
                        DialogNameNeedy?.editText?.text.toString(),
                        DialogLocationNeedy?.text.toString(),
                        DialogSecteurNeedy?.editText?.text.toString(),
                        DialogPhoneNeedy?.editText?.text.toString()
                    )



                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            // Do the POST request and get response
                            val response = service.AddFournisseur(Needy)
                            withContext(Dispatchers.Main) {
                                if (response!!.isSuccessful) {
                                    CustomToast(context, "Added Successfully!","GREEN").show()
                                    talk!!.senddata(response.body()!!)
                                    dialog.dismiss()
                                    println("success")


                                } else {
                                    Log.e("RETROFIT_ERROR", response.code().toString())
                                    println("Message :" + response.errorBody()?.string())
                                    CustomToast(context, "Something Went Wrong!", "RED").show()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ContextUtils.getActivity(context)?.runOnUiThread(java.lang.Runnable {
                                dialog.dismiss()
                                CustomToast(context, "Sorry, Our Server Is Down!", "RED").show()
                            })
                        }

                    }
                }

            }
        })

    }





    fun ShowDialogAddPromotion(context: Context?, view: View) {
        val dialog = Dialog(context!!)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //Make it TRANSPARENT
        dialog.window!!.getAttributes().windowAnimations = R.style.DialogAnimation; //Set Animation
        dialog.show()



        val BtnSaveAddPromo = view.findViewById<Button>(R.id.BtnSaveAddPromotion) as? Button
        DialogPrix = view.findViewById(R.id.DialogPrix)
        Dialogduaration = view.findViewById(R.id.Dialogduaration)
        Dialogproduct = view.findViewById(R.id.Dialogproduct)




        /////
        val preferences : SharedPreferences = context.getSharedPreferences("promo", Context.MODE_PRIVATE)
        val MyID = preferences.getString("id", null)
        //
        ////



        ///////
        gettextwathcerAddPromo()
        BtnSaveAddPromo?.setOnClickListener(object : View.OnClickListener {
            @SuppressLint("RestrictedApi")
            override fun onClick(view: View?) {
                if (!validatePrix()
                    or  !validateProduct() or !validateDuration()
                ) {
                    CustomToast(context, "Something is empty!", "RED").show()
                    return
                }
                if(
                    validatePrix()&&validateProduct()&&validateDuration()
                )
                {

                    val retrofi: Retrofit = retrofit.getInstance()
                    val service: UserApi = retrofi.create(UserApi::class.java)
                    val Promo = Promotion(
                        "",
                        DialogPrix?.editText?.text.toString(),
                        Dialogproduct?.editText?.text.toString(),
                        Dialogduaration?.editText?.text.toString()
                    )



                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            // Do the POST request and get response
                            val response = service.AddPromotion(Promo)
                            withContext(Dispatchers.Main) {
                                if (response!!.isSuccessful) {
                                    CustomToast(context, "Added Successfully!","GREEN").show()
                                    talk!!.senddata(response.body()!!)
                                    dialog.dismiss()
                                    println("success")


                                } else {
                                    Log.e("RETROFIT_ERROR", response.code().toString())
                                    println("Message :" + response.errorBody()?.string())
                                    CustomToast(context, "Something Went Wrong!", "RED").show()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ContextUtils.getActivity(context)?.runOnUiThread(java.lang.Runnable {
                                dialog.dismiss()
                                CustomToast(context, "Sorry, Our Server Is Down!", "RED").show()
                            })
                        }

                    }
                }

            }
        })

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
    fun senddata(n: Promotion)
}




