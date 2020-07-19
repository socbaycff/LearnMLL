package com.example.learnmll

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var labeler: FirebaseVisionImageLabeler

    companion object {
        val RC_CHOOSE = 4 
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()

    }

    fun choose(view: View?) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(
                    Intent.createChooser(intent, "Chon ung dung"),
                    MainActivity.RC_CHOOSE
            )

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_CHOOSE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data
                    imageView.setImageURI(uri)
                    uri?.let {
                        val visionImage = FirebaseVisionImage.fromFilePath(this, it)
                        labeler.processImage(visionImage).addOnSuccessListener {
                            label.text = "${it[0].text} ${it[0].confidence*100}%"
                        }
                    }

                }
            }

        }
    }
}
