# Foodies
A food recipes app with information about the different dishes and the steps to prepare it. This project is part of a course material that i followed to learn Kotlin, Jetpack Components etc.

![Made With - Android](https://img.shields.io/badge/Made_With-Android-2ea44f?logo=android)
![Powered by - Kotlin](https://img.shields.io/badge/Powered_by-Kotlin-B322E9)
![Release Version](https://img.shields.io/badge/release-v1.0.0-blue)

![Project Thumbnail](assets/Web-Thumbnail.png)

Download
--------
APK can be found [here][1]

Screenshots
-----------

| Screenshot 1  | Screenshot 2  | Screenshot 3 |
| --------------- | --------------- |------------|
| <img src="assets/1.jpg" width="400"> | <img src="assets/2.jpg" width="400"> | <img src="assets/3.jpg" width="400"> |
| Screenshot 4  | Screenshot 5  | Screenshot 6 |
| <img src="assets/4.jpg" width="400"> | <img src="assets/5.jpg" width="400"> | <img src="assets/6.jpg" width="400"> |
| Screenshot 7  | Screenshot 8  | Screenshot 9 |
| <img src="assets/7.jpg" width="400"> | <img src="assets/8.jpg" width="400"> | <img src="assets/9.jpg" width="400"> |
| Screenshot 10  | Screenshot 11  | Screenshot 12 |
| <img src="assets/10.jpg" width="400"> | <img src="assets/11.jpg" width="400"> | <img src="assets/12.jpg" width="400"> |
| Screenshot 13  |
| <img src="assets/13.jpg" width="400"> |

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
| binding adapters | contains all the binding adapters (used in XML layouts) |
| data | contains the data source (remote, local and datastore preferences)   |
| di | contains all the dependency injection modules |
| ui | contains all the activities, fragments and bottomsheet |
| utils | contains utility files like `Network Listeners`, `Constants`, etc. |
| viewmodels | contains `MainViewModel` and `RecipesViewModel` |

Author
------
<b>Rajit Deb</b>

Find me on
----------
[![LinkedIn Profile](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/imrajit/)
[![Instagram](https://img.shields.io/badge/Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/rajit.deb/)

[1]: https://github.com/rajitdeb/Foodies/raw/master/foodies_final_build.apk
[2]: https://spoonacular.com/food-api/console#Dashboard
