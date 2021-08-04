package de.c24.hg_abstraction

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import de.c24.hg_abstraction.databinding.ScanViewBinding
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.activity_scan.view.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ScanViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScanner: BarcodeScanner
    var resultListener: ((String) -> Unit)? = null

    init {
        initBarCodeScanner()
        startCamera()
    }

    private fun startCamera(activity: Activity) {
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(context))

    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {

        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraProvider.unbindAll()

        preview.setSurfaceProvider(previewview.surfaceProvider)

        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, setupAnalyzer())
    }

    private fun initBarCodeScanner() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
            .build()

        barcodeScanner = BarcodeScanning.getClient(options)
    }

    private fun setupAnalyzer(): ImageAnalysis {
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setImageQueueDepth(30)
            .build()

        val qrCodeAnalyzer = YourImageAnalyzer(barcodeScanner) { qrCodes ->
            qrCodes.firstOrNull()?.rawValue?.let { qrToken ->
                cameraExecutor?.shutdown()
                cameraProviderFuture?.get()?.unbindAll()
                resultListener?.invoke(qrToken)

            }
        }

        cameraExecutor?.let {
            imageAnalysis.setAnalyzer(it, qrCodeAnalyzer)
        }

        return imageAnalysis
    }

     fun destroyView() {
        cameraExecutor.shutdown()
     }

    fun setResultListener(){

    }

    private class YourImageAnalyzer(
        barcodeScanner: BarcodeScanner,
        private val onQrCodesDetected: (qrCodes: List<Barcode>) -> Unit
    ) : ImageAnalysis.Analyzer {
        val barCodeScanner = barcodeScanner

        override fun analyze(imageProxy: ImageProxy) {
            imageProxy.image?.let { inputImage ->
                val processingImage = InputImage.fromMediaImage(
                    inputImage,
                    imageProxy.imageInfo.rotationDegrees
                )

                barCodeScanner.process(processingImage)
                    .addOnSuccessListener {
                        imageProxy.close()
                        onQrCodesDetected.invoke(it)
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                    }
            }
        }
    }


}