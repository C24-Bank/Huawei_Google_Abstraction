package de.c24.hg_abstraction.scan

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.ml.scan.HmsScan
import de.c24.hg_abstraction.core_scan.ScanViewCore
import de.c24.hg_abstraction.scan.databinding.ScanViewBinding

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

    override var resultListener: ((String) -> Unit)? = null

    companion object
    {
        //declare RemoteView instance
        private var remoteView: RemoteView? = null
        //scan_view_finder width & height is  300dp
        val SCAN_FRAME_SIZE = 300

    }

     override fun startCamera(activity: Activity){
         //caculate viewfinder's rect,it's in the middle of the layout
         val rect = calculateRect()

         //initialize RemoteView instance, and set calling back for scanning result
         remoteView = initializeRemoteView(activity,rect)

        // Add the defined RemoteView to the page layout.
        val params = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
        val frameLayout = findViewById<FrameLayout>(R.id.container)
        frameLayout.addView(remoteView, params)
         remoteView?.onStart()
    }

    private fun calculateRect(): Rect{
        //1.get screen density to caculate viewfinder's rect
        val dm = resources.displayMetrics
        //2.get screen size
        val density = dm.density
        val mScreenWidth=dm.widthPixels
        val mScreenHeight=dm.heightPixels
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
                result != null && result.size > 0 && result[0] != null &&
                !TextUtils.isEmpty(result[0].getOriginalValue())
            ) {
                val hmsScanResult: HmsScan = result[0]
                resultListener?.invoke(hmsScanResult.originalValue)
            }
        }
        return remoteView
    }
    
    override fun destroyView(){
        remoteView?.onDestroy()
    }
}