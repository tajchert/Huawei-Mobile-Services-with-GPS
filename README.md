# Huawei-Mobile-Services-with-GPS
Sample integration of Huawei Mobile Services (HMS, Map Kit) for existing app with Google Mobile Services. This repo show how to create HMS flavour of your app and split GPS/HMS imports when using Android Architecture components (LiveData and ModelView).

## Why
To show how to integrate both HMS and GPS services side-by-side. It is done using 2 flavors, to visualize how many object that you wouldn't expect are provider-dependent (ex. `LatLng` or `BitmapDescriptor` just to name few in such simple use case).

## How to run it?
* For HMS flavor:
  * Huawei device (with HMS), otherwise map will result in error. But it will build fine.
  * `agconnect-services.json`(`/app` level) that you can obtain from `Huawei Developer Console` [link on how to do it](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-get-started).
* For GPS flavor:
  * Obtain Google Map key and put it inside `mapKeysFile.properties` on root level ex. googleMapsKey="YOUR_API_KEY_HERE".
  * Obtain `google-services.json`(`/app` level) that you can obtain from `Google Firebase Console`.
  * Device with GPS.

### HMS Map Errors
If you got error when map is showing:
* `getRemoteContext: DynamicModule load failedcom.huawei.hms.feature.dynamic.DynamicModule$LoadingException: Query huawei_module_maps unavailable, errorCode:2`
* `getRemoteContext: DynamicModule load failedcom.huawei.hms.feature.dynamic.DynamicModule$LoadingException: Query huawei_module_maps unavailable, errorCode:4`
* `getRemoteMapContext failed`
* `HmsMapKit_HmsUtil_88: Hms is :0`

That mean device is not Huawei and is not eligible for HMS Map Kit. If code is other number - check HMS documentation [here](https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/hms-map-v4-faq).

### I have other service that I'm trying to use
I'm open to pull requests. This is just sample on probably most used Google and Huawei SDK to visualize what is needed and how it affects your code (assuming basic architecture) and a good starting point.

## TODO
* Fetching current location
* Repository for providing mock data
* Automatic testing
