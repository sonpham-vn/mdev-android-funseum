package com.example.funseum.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.funseum.R
import com.example.funseum.model.FrameModel
import com.example.funseum.model.UserFrameData
import kotlinx.android.synthetic.main.activity_gallery.*


class GalleryActivity : AppCompatActivity() {
    private val frameModel = FrameModel(this)
    var frameList = UserFrameData ()
    lateinit var topImg : Bitmap
    lateinit var bottomImg : Bitmap
    lateinit var mixImg : Bitmap

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var linearLayout: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        linearLayout = findViewById(R.id.linear1)
        val layoutInflater = LayoutInflater.from(this)

        //load frame list
        frameModel.getEvent {
            frameList = it
            txtpoint.text = "Your current points: " + frameList.Point!!.toString()
            for (i in frameList.Frame!!.indices)
            {
                val view: View = layoutInflater.inflate(
                    R.layout.activity_gallery_frame,
                    linearLayout,
                    false
                )
                val frameImageView =  view.findViewById<ImageView>(R.id.iv)
                frameImageView.setImageResource(getResource(frameList.Frame!![i].frameFile!!)!!)
                val tv = view.findViewById<TextView>(R.id.tv)
                if (frameList.Frame!![i].Active!!) {
                    tv.text = frameList.Frame!![i].frameName
                } else {
                    tv.text = "\uD83D\uDD12" + frameList.Frame!![i].frameName
                }

                linearLayout?.addView(view)
                frameImageView.setOnClickListener {

                    if (frameList.Frame!![i].Active!!) {
                        topImg = BitmapFactory.decodeResource(
                            resources,
                            getResource(frameList.Frame!![i].frameFile!!)!!
                        )
                        mixImg = mergeBitmap(topImg, bottomImg)!!
                        imageView.setImageBitmap(mixImg)
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("This frame is locked")
                        builder.setMessage("Do you want to use 50 points to unlock it?")

                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            frameList.Frame!![i].Active = true
                            frameModel.unlockFrame(this, frameList.Frame!!) {
                                frameModel.getEvent {
                                    frameList = it
                                    tv.text = frameList.Frame!![i].frameName
                                    txtpoint.text = "Your current points: " + frameList.Point!!.toString()

                                }
                            }
                        }

                        builder.setNegativeButton(android.R.string.no) { dialog, which ->

                        }
                        builder.show()
                    }


                }

            }
        }

        buttonLoadPicture.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }





    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            imageView.invalidate()
            val drawable = imageView.drawable
            bottomImg = drawable.toBitmap()

        }
    }

    fun Context.getResource(name: String): Int? {
        val resID = this.resources.getIdentifier(name, "drawable", this.packageName)
        return resID
    }

    fun mergeBitmap(top: Bitmap, bottom: Bitmap?): Bitmap? {

        lateinit var top_resize : Bitmap
        top_resize = Bitmap.createScaledBitmap(top,((top.width)*0.75).toInt(),((top.height)*0.75).toInt(), false)

        val bitmap1Width: Int = bottom!!.getWidth()
        val bitmap1Height: Int = bottom!!.getHeight()
        val bitmap2Width: Int = top_resize.getWidth()
        val bitmap2Height: Int = top_resize.getHeight()

        val marginLeft = (bitmap1Width * 0.5 - bitmap2Width * 0.5).toFloat()
        val marginTop = (bitmap1Height * 0.5 - bitmap2Height * 0.5).toFloat()

        val finalBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bottom!!.getConfig())
        val canvas = Canvas(finalBitmap)
        canvas.drawBitmap(bottom, Matrix(), null)
        canvas.drawBitmap(top_resize, marginLeft, marginTop, null)
        return finalBitmap
    }


}






