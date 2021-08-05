package de.c24.hg_abstraction

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.ml.scan.HmsScan
import de.c24.hg_abstraction.core_scan.ScanViewCore
import de.c24.hg_abstraction.databinding.ScanViewBinding

class ScanView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ScanViewCore {

    private val binding = ScanViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var resultListener: ((String) -> Unit)? = null

    companion object
    {
        //declare RemoteView instance
        private var remoteView: RemoteView? = null
        //declare the key ,used to get the value returned from scankit
        var mScreenWidth = 0
        var mScreenHeight = 0
        //scan_view_finder width & height is  300dp
        val SCAN_FRAME_SIZE = 300

    }

     override fun startCamera(activity: Activity){
        //1.get screen density to caculate viewfinder's rect
        val dm = resources.displayMetrics
        //2.get screen size
        val density = dm.density
        mScreenWidth=dm.widthPixels
        mScreenHeight=dm.heightPixels
        var scanFrameSize=(SCAN_FRAME_SIZE*density)
        //3.caculate viewfinder's rect,it's in the middle of the layout
        //set scanning area(Optional, rect can be null,If not configure,default is in the center of layout)
        val rect = Rect()
        apply {
            rect.left = (mScreenWidth / 2 - scanFrameSize / 2).toInt()
            rect.right = (mScreenWidth / 2 + scanFrameSize / 2).toInt()
            rect.top = (mScreenHeight / 2 - scanFrameSize / 2).toInt()
            rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2).toInt()
        }
        //initialize RemoteView instance, and set calling back for scanning result
        remoteView = RemoteView.Builder().setContext(activity).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build()
        remoteView?.onCreate(null)
        remoteView?.setOnResultCallback { result ->
            if (result != null && result.size > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
                val hmsScanResult: HmsScan = result[0]
                resultListener?.invoke(hmsScanResult.originalValue)
            }
        }
        // Add the defined RemoteView to the page layout.
        val params = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        val frameLayout = findViewById<FrameLayout>(R.id.container)
        frameLayout.addView(remoteView, params)
    }

    override fun destroyView(){
        remoteView?.onDestroy()
    }
}