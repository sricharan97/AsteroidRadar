# Asteroid Radar


## Getting Started

Asteroid Radar is an app to view the asteroids detected by NASA that pass near Earth, you can view all the detected asteroids in a period of time, their data (Size, velocity, distance to Earth) and if they are potentially hazardous.

The app consists of two screens: A Main screen with a list of all the detected asteroids and a Details screen that is going to display the data of that asteroid once it´s selected in the Main screen list. The main screen will also show the NASA image of the day to make the app more striking.

## Built With

To build this project you are going to use the NASA NeoWS (Near Earth Object Web Service) API, which you can find here.
https://api.nasa.gov/

Please use your own API KEY to have the app working for you


### API KEY

Please replace the value "PROVIDE YOUR API KEY HERE" of the variable API_KEY inside the Constants.kt file to have the NASA API service working for you once you download the app.

### Screenshots

![Screenshot 1](starter/screenshots/screen_1.png)
![Screenshot 2](starter/screenshots/screen_2.png)
![Screenshot 3](starter/screenshots/screen_3.png)
![Screenshot 4](starter/screenshots/screen_4.png)


## Project Instructions

The most important dependencies we are using are:
- Retrofit to download the data from the Internet.
- Moshi to convert the JSON data we are downloading to usable data in form of custom classes.
- Picasso to download and cache images.
- RecyclerView to display the asteroids in a list.

Following components from the Jetpack library were also used:
- ViewModel
- Room
- LiveData
- Data Binding
- Navigation

## Skills Learned

  1. Network calls using **Retrofit**
  2. Data persistence and offline caching to Database using **Room**
  3. Displaying a list of items efficiently using a **Recycler view**
  4. Multithreading using **Coroutines**
  5. Binding complex types using **BindingAdapters**
  6. Displaying images from Web efficiently using **Picasso**
  7. Building for everyone using **Accessibility** features






