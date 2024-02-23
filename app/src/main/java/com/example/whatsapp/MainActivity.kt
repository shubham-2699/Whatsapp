package com.example.whatsapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var pick: Button

    private val uriList = ArrayList<Uri>()
    private lateinit var adapter: RecyclerAdapter

    private val readPermission = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.totalPhotos)
        recyclerView = findViewById(R.id.recyclerView_Gallery_Images)
        pick = findViewById(R.id.pick)

        adapter = RecyclerAdapter(uriList)
        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 4)
        recyclerView.adapter = adapter

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                readPermission
            )
        }

        pick.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount

                for (i in 0 until count) {
                    uriList.add(data.clipData!!.getItemAt(i).uri)
                }
                adapter.notifyDataSetChanged()
                textView.text = "Photos (${uriList.size})"
            } else if (data?.data != null) {
                val imageURL = data.data!!.path
                uriList.add(Uri.parse(imageURL))
                adapter.notifyDataSetChanged()
                textView.text = "Photos (${uriList.size})"
            }
        }
    }
}
