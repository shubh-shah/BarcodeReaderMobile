# BarcodeReaderMobile
Transform your phone into a barcode reader. 
No need to buy a scanner.   
Get the companion desktop server [here](https://github.com/shubh-shah/BarcodeReaderDesktop).
Get a release from github or you can build it yourself

Created from the [material-showcase](https://github.com/googlesamples/mlkit) app. Thanks to the Google developers behind it.

## How to use the app

+ Install the compainion desktop app
+ Pair with the desktop app by scanning the generated QR Code.
+ Scan away and the encoded text from your scanned barcodes will "magically" appear on your desktop.

### Live Camera

It uses the camera preview as input and contains barcode detection. There's also a Settings page to
allow you to configure several options:
- Barcode detection
    - Barcode reticle width -- Size of barcode reticle width relative to the camera preview width
    - Barcode reticle height -- Size of the barcode reticle height relative to the camera preview height
    - Enable Barcode size check -- Will prompt user to "move closer" if the detected barcode is too small
    - Delay loading result -- Simulates a case where the detected barcode requires further processing before displaying the result.
