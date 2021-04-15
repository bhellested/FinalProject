# Final Project For CS1660

To build, first clone this repo and add your json credentials into the directory.
Next execute:
  docker build -t hellestedfinalproject .
  
GUI Passthrough was achieved following this tutorial:
https://dev.to/darksmile92/run-gui-app-in-linux-docker-container-on-windows-host-4kde

Once those steps are in place, run: (replace credential path with your own)
docker run -ti --rm -e DISPLAY=$DISPLAY -e GOOGLE_APPLICATION_CREDENTIALS=./credential/path.json hellestedfinalproject

The invertedIndex.java file is present in this library but the JAR executed lives on my cloud storage.

projectFrame.java contains my GUI, however the included projectFrame.jar is all you need to run
This jar packages all needed GCP imports.

