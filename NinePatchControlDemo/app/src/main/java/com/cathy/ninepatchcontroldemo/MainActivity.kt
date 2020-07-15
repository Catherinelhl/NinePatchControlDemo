package com.cathy.ninepatchcontroldemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cathy.ninepatchcontroldemo.utils.glide.GlideUtil
import com.cathy.ninepatchcontroldemo.utils.glide.LRUCacheManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_content.text = "asdfaksjdfbkajsdbfkjabsfdkjasbdf"
        startShow()
    }

    private var count = 1
    private val timer by lazy { Timer() }
    private val timerTask by lazy {
        timerTask {
            loadBitmapByGlide(if (count < 10) "0$count.png" else "$count.png")
            if (count == 10) {
                count = 0
            }
            count++
        }
    }

    private fun startShow() {
        timer.run {
            schedule(timerTask, 500, 2000)
        }
    }

    private var animationDrawableLeft: AnimationDrawable? = null

    private fun loadBitmapByGlide(fileName: String) {
        GlideUtil().loadAsNinePatchGif(
            this,
            "file:///android_asset/$fileName",
            tv_content,
            6
        ) { drawable -> animationDrawableLeft = drawable }


        //Second one
//        val drawable = NinePatchBitmapFactory.createNinePatchDrawable(this.resources,bitmap)
//        tv_content.background = drawable
//////
////or add multiple patches
//        val builder = NinePatchBuilder(resources, bitmap)
//        builder.addXRegion(30, 2).addXRegion(50, 1).addYRegion(20, 4)
//        val chunk = builder.buildChunk()
//        val ninepatch = builder.buildNinePatch()
//        val drawable = builder.build()
//        tv_content.background = drawable

//Here if you don't want ninepatch and only want chunk use
//        val builder = NinePatchBuilder(width, height)
//        val chunk: ByteArray =
//            builder.addXCenteredRegion(1).addYCenteredRegion(1).buildChunk()

//        byte[] chunk = bitmap.getNinePatchChunk();
//              if (NinePatch.isNinePatchChunk(chunk)) {
//                      NinePatchDrawable patchy = new NinePatchDrawable(view.getResources(), bitmap, chunk, new Rect(), null);
//                      view.setBackground(patchy);
//                   }

    }

    private fun loadBitmapByDrawable(fileName: String, Y: Int) {
        var inputStream: InputStream = assets.open(fileName)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

        var builder = NinePatchBuilder(resources, bitmap)
        val drawable =
            builder.addXCenteredRegion(1).addYCenteredRegion(1, Y).build()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask.cancel()
        timer.cancel()
        animationDrawableLeft?.let { it.stop() }
        LRUCacheManager.getInstance().clearAll()

    }

}
