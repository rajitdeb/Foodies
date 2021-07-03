# Foodies
A food recipes app with information about the different dishes and the steps to prepare it

![Made With - Android](https://img.shields.io/badge/Made_With-Android-2ea44f?logo=android)
![Powered by - Kotlin](https://img.shields.io/badge/Powered_by-Kotlin-B322E9)
![Release Version](https://img.shields.io/badge/release-v1.0.0-blue)

![Project Thumbnail](assets/Web-Thumbnail.png)

Screenshots
-----------
![](https://i.postimg.cc/ydcSPCYd/0.png)
![](https://i.postimg.cc/bNXmpq26/1.jpg)
![](https://i.postimg.cc/MpHMLQKg/2.jpg)
![](https://i.postimg.cc/W3GhZmXq/3.jpg)
![](https://i.postimg.cc/JnMR9f5t/4.jpg)
![](https://i.postimg.cc/T1HG5pMW/5.jpg)
![](https://i.postimg.cc/CKV0q3tJ/6.jpg)
![](https://i.postimg.cc/MHXJwqMZ/7.jpg)
![](https://i.postimg.cc/KvkhschJ/8.jpg)
![](https://i.postimg.cc/SNBb00yq/9.jpg)
![](https://i.postimg.cc/zfksRVTX/10.jpg)
![](https://i.postimg.cc/nzc6Kp1K/11.jpg)
![](https://i.postimg.cc/jdJB48y1/12.jpg)
![](https://i.postimg.cc/mgYq0tXN/13.jpg)

Download
--------
APK can be found [here][1]

Libraries Used
--------------
![Image Loading - Glide](https://img.shields.io/badge/Image_Loading-Glide-brightgreen)
![Image Loading - COIL](https://img.shields.io/badge/Image_Loading-COIL-blue)
![Caching - ROOM](https://img.shields.io/badge/Caching-ROOM-green)
![Preferences - DataStore](https://img.shields.io/badge/Preferences-DataStore-important)
![Network - Retrofit2](https://img.shields.io/badge/Network-Retrofit2-ff69b4)
![Dependency Injection - Hilt](https://img.shields.io/badge/Dependency_Injection-Hilt-critical)
![And - Many More](https://img.shields.io/badge/And-Many_More-blueviolet)


API Details
-----------

Register here for getting the [api key][2]

Where to paste the API Key?
--------------------------

Go to ` Constants.kt ` file inside ` Utils ` package and find `API_KEY` variable and paste your API key.

```
class Constants {

    companion object {
    
        ...

        const val API_KEY = "YOUR_API_KEY"
        
    }
}   
```

Project Structure
----------------

| Package Name   | What is it used for  |
| :------------ | :------------ |
| adapters | contains all the adapters used in recycler views |
| bindingadapters | contains all the binding adapters (used in XML layouts) |
| data | contains the data source (remote, local and datastore preferences)   |
| di | contains all the dependency injection modules |
| ui | contains all the activities, fragments and bottomsheet |
| utils | contains utility files like `Network Listeners`, `Constants`, etc. |
| viewmodels | contains `MainViewModel` and `RecipesViewModel` |

Author
------
Rajit Deb

Find me on
----------
[![LinkedIn Profile](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/imrajit/)
[![Instagram](https://img.shields.io/badge/Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/rajit.deb/)

[1]: https://github.com/rajitdeb/Foodies/raw/master/foodies_latest_build.apk
[2]: https://spoonacular.com/food-api/console#Dashboard
