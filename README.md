# Final Project For CS1660
(JTables were used to display results)
Link to demo: https://youtu.be/ZIjDlEAwqjo

To build, first clone this repo and add your json credentials into the directory.
Next execute:
  docker build -t hellestedfinalproject .
  
GUI Passthrough was achieved following this tutorial:
https://dev.to/darksmile92/run-gui-app-in-linux-docker-container-on-windows-host-4kde

Once those steps are in place, run: (replace credential path with your own)
docker run -ti --rm -e DISPLAY=$DISPLAY -e GOOGLE_APPLICATION_CREDENTIALS=./credential/path.json hellestedfinalproject

If you would like to run from files on your local system either use bind mounts described below or copy the files into the data directory.
https://docs.docker.com/storage/bind-mounts/

The invertedIndex.java file is present in this library but the JAR executed lives on my cloud storage.


invertedIndex.java is the hadoop job, the jar can be found on my cloud storage bucket.

projectFrame.java contains my GUI, however the included projectFrame.jar is all you need to run
This jar packages all needed GCP imports.

If planning on running yourself, you will need to move invertedIndex.jar into a bucket and update the bucket references in projectFrame.java

