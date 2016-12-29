<p align="center">
  <img src="https://raw.githubusercontent.com/kvsjxd/YouMusix/gh-pages/images/Screenshot.png">
</p>

## Features:

* Easy to use
* Video duration limit - Approx. 1 hour
* Highest sound quality (Depends on Video)
* Download stream as MP3 with ID3 tags and Album Art
* Check MP3 stream size without downloading it
* Saves a lot of bandwidth

## Building:

1. Add all the jar files present in lib directory to class path or build path
2. Export project as runnable JAR
3. Make sure the JAR file is set as executable if running Unix-like OS

If you are using UNIX-Like OS (GNU/Linux, MacOS, BSD, etc.) make sure the JAR file is set as executable before launching it for the first time on your system.

## Possible Issues:

* Elasped Duration is known to work only when running project within IDE and does not work in exported runnable JAR.
* Downloads cannot be cancelled.
* Due to limitations imposed by server, sometimes downloading and streaming won't work. Try using another YouTube url or quit the application and relaunch it.
