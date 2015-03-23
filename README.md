# Newfad-Generator
Methodology Buzzword Generator


[![Get it on Google Play](http://www.android.com/images/brand/get_it_on_play_logo_small.png)](https://play.google.com/store/apps/details?id=com.omatt.newfadgenerator)

This project was made out of fun and is based from [keikun17's](https://github.com/keikun17) [newfad generator](https://keikun17.github.io/newfad-generator/) that's also available at [Github](https://github.com/keikun17/newfad-generator). 

Generate your own Google Analytics [Tracking ID](https://www.google.com/analytics/) and replace the ids in:

`res/xml/ecommerce_tracker.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <integer name="ga_sessionTimeout">60</integer>
    <!--  The following value should be replaced with correct property id. -->
    <string name="ga_trackingId">UA-########-#</string>
</resources>
```
`res/xml/global_tracker.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <integer name="ga_sessionTimeout">300</integer>
    <bool name="ga_autoActivityTracking">true</bool>
    
    <!-- The screen names that will appear in reports -->
    <screenName name="com.omatt.newfadgenerator.MainActivity">Newfad Generator</screenName>
    
    <!-- The following value should be replaced with correct property id. -->
    <string name="ga_trackingId">UA-########-#</string>
</resources>
```
`com/omatt/newfadgenerator/analytics/AnalyticsMedium.java`
```java
private static final String PROPERTY_ID = "UA-########-#";
```

Generate your own [Facebok APP_ID](https://developers.facebook.com/apps/) and replace the id in:

`res/values/strings.xml`
```xml
<!-- Facebook APP ID -->
<string name="app_id">Place the app_id here</string>
```

Requirements
--------
Android Studio 1.1.0

Java SDK (jdk1.7.0_51)

Android SDK Tools 24.1.2

Android SDK Build-tools 21.1.2

Minimum Target API 15; Target API 22

Dependencies
--------
'com.android.support:appcompat-v7:21.0.3'

'com.google.android.gms:play-services:6.5.87'

'com.facebook.android:facebook-android-sdk:3.23.1'

<h2>Copyright</h2>
    Copyright © 2015 Omar Matthew Reyes
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
