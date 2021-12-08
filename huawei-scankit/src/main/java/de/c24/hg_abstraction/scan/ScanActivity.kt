package de.c24.hg_abstraction.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.ml.scan.HmsScan

class ScanActivity : AppCompatActivity() {

    companion object
    {
        //declare the key ,used to get the value returned from scankit
        val SCAN_RESULT = "scanResult"
        //scan_view_finder width & height is  300dp
        val SCAN_FRAME_SIZE = 300
    }

    //declare RemoteView instance
    private lateinit var remoteView: RemoteView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
    }

    //manage remoteView lifecycle
    override fun onStart() {
        super.onStart()
        startCamera()
        remoteView?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView?.onDestroy()
    }

    private fun startCamera(){
        //caculate viewfinder's rect,it's in the middle of the layout
        val rect = calculateRect()

        //initialize RemoteView instance, and set calling back for scanning result
        remoteView = initializeRemoteView(this,rect)

        // Add the defined RemoteView to the page layout.
        val params = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        val frameLayout = findViewById<FrameLayout>(R.id.rim1)
        frameLayout.addView(remoteView, params)
    }

    private fun calculateRect(): Rect{
        //1.get screen density to caculate viewfinder's rect
        val dm = resources.displayMetrics
        //2.get screen size
        val density = dm.density
        val mScreenWidth =dm.widthPixels
        val mScreenHeight =dm.heightPixels
        //scan_view_finder width & height is  300dp
        val scanFrameSize=(SCAN_FRAME_SIZE *density)
        //3.caculate viewfinder's rect,it's in the middle of the layout
        //set scanning area(Optional, rect can be null,
        // If not configure,default is in the center of layout)
        val rect = Rect()
        apply {
            rect.left = (mScreenWidth / 2 - scanFrameSize / 2).toInt()
            rect.right = (mScreenWidth / 2 + scanFrameSize / 2).toInt()
            rect.top = (mScreenHeight / 2 - scanFrameSize / 2).toInt()
            rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2).toInt()
        }
        return rect
    }

    private fun initializeRemoteView(activity: Activity,rect: Rect):RemoteView{
        //initialize RemoteView instance, and set calling back for scanning result
        val remoteView = RemoteView.Builder()
            .setContext(activity).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build()
        remoteView?.onCreate(null)
        remoteView?.setOnResultCallback { result ->
            if (
                result != null && result.size > 0 && result[0] != null
                && !TextUtils.isEmpty(result[0].getOriginalValue())
            ) {
                val hmsScanResult: HmsScan = result[0]
                val intent = Intent()
                intent.apply {
                    putExtra(SCAN_RESULT, hmsScanResult.getOriginalValue()) }
                setResult(Activity.RESULT_OK, intent)
            }
        }
        return remoteView
    }

    override fun onResume() {
        super.onResume()
        remoteView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        remoteView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        remoteView?.onStop()
    }



}