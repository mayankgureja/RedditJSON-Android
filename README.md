RedditJSON-Android
==================

An Android 4.2 app that extracts JSON data from Reddit.com and displays it in a ListView. Long-click opens the URL in a browser.

How To Run
----------

The RedditJSON.apk is located inside the /bin folder. Simply sideload it onto your Android phone or emulator and you're good to go.

Coding Environment
------------------

Written, built and tested on Eclipse-ADT (Android Development Tools). See http://developer.android.com/tools/sdk/eclipse-adt.html

Description
-----------

RedditJSON is an Android 4.2 app that extracts the JSON data present on Reddit pages and displays the various links extracted in a ListView. Long-cliking any of these links opens the article in a browser. This version only displays the Reddit homepage.

Reddit JSON data can be extracted from any page on reddit.com by adding a "/.json" to the end of the link. For example, to get JSON data for reddit.com, type in reddit.com/.json. For reddit.com/r/politics, it would be reddit.com/r/politics/.json . Another great thing about Reddit is that there is no authentication requirement to extract JSON data, such as the hated/loved OAuth protocol.

The application itself contains just 1 activity, "MainActivity" and all processing is done here. Since this is an Android 4.2 application, any networking has to be done outside the main thread. The reason for this requirement is that network calls are blocking and hence can hit performance unpredictably, and it has been in effect since Android 4.0. The requirement is fulfilled using an AsnycTask method, that outsources  any tasks requiring network access to an asynchronous process. This process can further drive the application after it is finished.

Since the time taken to GET information from a web resource can vary wildly, a ProgressDialog was eventually added to give the user some feedback as to what is happening. Instead of waiting on a blank page for random periods of time, the user is shown a "Loading..." message until the application is ready.

Screenshots
-----------

Screenshots of the application in action are available in the /screenshots folder.

Dependencies/Requirements
-------------------------

Jackson Java JSON Processor. See http://jackson.codehaus.org/
* jackson-annotations-2.1.1.jar
* jackson-core-2.1.1.jar
* jackson-databind-2.1.1.jar

Everything else is part of the standard libraries.