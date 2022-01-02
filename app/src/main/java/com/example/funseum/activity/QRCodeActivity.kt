package com.example.funseum.activity

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.funseum.R
import com.example.funseum.model.QRData
import com.example.funseum.model.QRModel
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_qrcode.*
import java.io.IOException


class QRCodeActivity : AppCompatActivity() {
    private lateinit var mQrResultLauncher : ActivityResultLauncher<Intent>
    var mediaPlayer: MediaPlayer? = null
    var isPlay = false
    var qrData = QRData ()

    override fun onCreate(savedInstanceState: Bundle?) {
        val QRModel = QRModel ()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        btnpause.visibility = 0

        // Alternative to "onActivityResult", because that is "deprecated"
        mQrResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK) {
                val result = IntentIntegrator.parseActivityResult(it.resultCode, it.data)

                if(result.contents != null) {
                    val decodedQR = result.contents
                    // Do something with the contents (this is usually a URL)
                    QRModel.qrcall(this, decodedQR) {
                        qrData = it
                        btnpause.visibility = 1
                        txtqrcode.text = "QR record done! Your Point has increased \n Debug: $decodedQR"
                    }
                }
            }
        }

        btnpause.setOnClickListener() {
            if (isPlay) {
                mediaPlayer!!.stop()
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
                btnpause.text = "▶"
            }
            else
            {
                playAudio(qrData.songUrl!!)
                btnpause.text = "￭"
            }
            isPlay = !isPlay

        }

        // Starts scanner on Create of Overlay (you can also call this function using a button click)
        startScanner()
    }


    // Start the QR Scanner
    private fun startScanner() {
        val scanner = IntentIntegrator(this)
        // QR Code Format
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        // Set Text Prompt at Bottom of QR code Scanner Activity
        scanner.setPrompt("")
        // Start Scanner (don't use initiateScan() unless if you want to use OnActivityResult)
        mQrResultLauncher.launch(scanner.createScanIntent())
    }
    // Start the audio player
    private fun playAudio(audioLink: String) {

        // initializing media player
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try {
            mediaPlayer!!.setDataSource(audioLink)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        if (isPlay) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
        }
        super.onBackPressed()
    }
}