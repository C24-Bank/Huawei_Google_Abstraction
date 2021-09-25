package de.c24.hg_abstraction.scan

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage

class YourImageAnalyzer(
    barcodeScanner: BarcodeScanner,
    private val onQrCodesDetected: (qrCodes: List<Barcode>) -> Unit
) : ImageAnalysis.Analyzer {
    val barCodeScanner = barcodeScanner

    @androidx.camera.core.ExperimentalGetImage
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