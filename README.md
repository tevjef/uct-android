Rutgers Course Tracker [![Build Status](https://travis-ci.org/tevjef/Rutgers-Course-Tracker.svg?branch=master)](https://travis-ci.org/tevjef/Rutgers-Course-Tracker)
======================
#### How to Build 
<pre> Import with Android Studio </pre>
<pre> gradle assembleDebug </pre>

[![Get on Google Play](https://github.com/tevjef/Rutgers-Course-Tracker/blob/master/assets/en_g_play.png)][1]

<h2>What is Rutgers Course Tracker?</h2>
In short, Rutgers Course Tracker (RCT) allows users to view courses and subscribe to their openings. Inspired by Abe Stanway's [Schedule Sniper](http://abe.is/a/sniper/) and Vaibhav Verma's python [Sniper](https://github.com/v/sniper). 
<br>

Additionally, users can view information about current and upcoming semesters. This includes prerequisites, course specific information and even professor ratings and details.
Once a section of course is tagged to be tracked, the app checks for openings at user selectable interval.
<h2>Motivations?</h2>
I started this project on January 14th 2015 out of desperation. Other "course sniping" solutions out there are focused on the New Brunswick campus, leaving me with very few options. In a few days I threw together something simple and it worked quite well. I began work on a full version and within a few weeks it was stable and presentable enough to be my first app on the Google Play Store.
<h2>How was it made?</h2>
RCT was developed in my spare time using <a href="https://developer.android.com/sdk/index.html">Android Studio 1.3</a> and <a href="https://www.jetbrains.com/idea/">Intellij 14.04</a>. It was designed to take advantage of the <strong>Model-View-Presenter (MVP)</strong> architectural pattern which enabled me to create a robust, extensible and testable code base.

I owe the speed at which I developed the application to <a href="https://github.com/tevjef/Rutgers-Course-Tracker/blob/master/app/build.gradle#L87-L146">many open source libraries</a>. Each one of them likely saved me from 3 days of headaches, debugging and testing. 3 of them in particular, <strong>RxJava</strong>, <strong>Retrofit</strong> and <strong>Dagger</strong>, greatly simplified my business code and reduced some very complex logic to child's play. I highly recommend using any of these libraries in you own applications.


Screenshots
-----------
<img width="300" src="https://github.com/tevjef/Rutgers-Course-Tracker/blob/master/assets/new_material/tracked_sections.png">
<img width="300" src="https://github.com/tevjef/Rutgers-Course-Tracker/blob/master/assets/new_material/course_info.png">
<img width="300" src="https://github.com/tevjef/Rutgers-Course-Tracker/blob/master/assets/new_material/section_info.png">

[1]: https://play.google.com/store/apps/details?id=com.tevinjeffrey.rutgersct
