package com.example.shop.Views.Fragement
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.shop.R
import com.example.shop.Utils.*
import com.example.shop.ViewModel.UserViewModel
import com.example.shop.Views.Activity.Login
import com.google.android.material.tabs.TabLayout

import java.io.*
import java.util.*


class ProfileFragment : Fragment() {
    ///
    private lateinit var MySharedPref: SharedPreferences
    private lateinit var MyName: TextView
    private lateinit var txtBio:TextView
    private lateinit var MyToolbar: Toolbar
    private lateinit var btnEditProfile : Button
    //
    private lateinit var userAvatar: ImageView
    private lateinit var tabLayoutProfile : TabLayout
    private lateinit var viewpagerProfile : ViewPager2
    //
    private var imgUri: Uri? = null
    var viewModell = UserViewModel()
    private val STORAGE_PERMISSION_CODE = 111
    private val IMAGE_GALLERY_REQUEST_CODE: Int = 2001
    //
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView();
        SetToolbar()
        SetUserData()

    }

    fun initView() {
        MySharedPref =requireContext().getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        MyName = requireView().findViewById(R.id.txtUser)
        txtBio = requireView().findViewById(R.id.txtBio)
        userAvatar = requireView().findViewById(R.id.userAvatar)
        btnEditProfile = requireView().findViewById(R.id.btnEditProfile)
        btnEditProfile.setBackgroundResource(R.drawable.btn_dark); // Set Button Style @Null on the xml
        //
       // tabLayoutProfile = requireView().findViewById(R.id.tabLayoutProfile)
        viewpagerProfile = requireView().findViewById(R.id.view_pageProfile)
    }

    fun SetUserData() {
        val nameUser = MySharedPref.getString(NAMEUSER, null)
        val avatarUser = MySharedPref.getString(AVATARUSER, null)


        var NameUserMajuscule =
            nameUser?.substring(0, 1)?.toUpperCase() + nameUser?.substring(1)?.toLowerCase();
        MyName.text = NameUserMajuscule
        //
        val emailUser = MySharedPref.getString(EMAILUSER, null)
        txtBio.text = emailUser
        //txtBio.text = Html.fromHtml("<a href='stackoverflow.com'>Go StackOverFlow!</a>")
        println("Lien de la photo : http://192.168.1.12:2500/uploads/$avatarUser")
        println("avatarUser: $avatarUser")
        val ImagelINKAvatar = (avatarUser)



        //

        //  Set Verified Icon
        //MyName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0);
        //MyName.setCompoundDrawablePadding(0);
        //
        if (avatarUser != null && nameUser != null && ImagelINKAvatar != null) {
            if (!ImagelINKAvatar.isNullOrEmpty()) {
                Glide
                    .with(requireContext())
                    .load(ImagelINKAvatar)
                    .fitCenter()
                    .error(R.drawable.avatar)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d(TAG, "onLoadFailed")
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d(TAG, "OnResourceReady")
                            //do something when picture already loaded
/*                        MySharedPref.edit().apply{
                            putString(AVATARUSER, ImagelINKAvatar)
                        }.apply()*/
                            return false
                        }
                    }).into(userAvatar);
            } else {
                print("uiiiiiiiiii")
                userAvatar.setImageResource(R.drawable.avatar)
            }
        }

    }



    fun SetToolbar()
    {
        MyToolbar = requireView().findViewById(R.id.toolbarProfile)
        (activity as AppCompatActivity).setSupportActionBar(MyToolbar)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        //
        MySharedPref =
            requireContext().getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> Logout()
        }
        return super.onOptionsItemSelected(item)
    }

    fun Logout() {

            println("Logout CLicked !!")
            // Reste du code...
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmation!")
            builder.setMessage("Are you sure do want to logout?")
            builder.setPositiveButton("Yes"){ dialogInterface, which ->
                //
                MySharedPref.edit().apply {
                    putString(RememberEmail, "")
                    putString(RememberPassword, "")
                }.apply()
                activity?.let{
                    val intent = Intent (it, Login::class.java)
                    it.startActivity(intent)
                    it.finish()
                }
            }
            builder.setNegativeButton("No"){dialogInterface, which ->
                dialogInterface.dismiss()
            }
            builder.create().show()



    }



}
