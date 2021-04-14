FROM openjdk:11

COPY ./ ./
RUN apt-get update && apt-get install -y libxrender1 libxtst6 libxi6
#RUN apt-get update && apt-get install -y install libX11-devel.x86_64
#RUN apt-get update && apt-get install -y install libXext.x86_64
#RUN apt-get update && apt-get install -y install libXrender.x86_64
#RUN apt-get update && apt-get install -y install libXtst.x86_64

#CMD ["java", "dockerHW"]
CMD ["java", "-jar", "projectFrame.jar","projectFrame"]
#CMD ["java", "-jar", "projectFrame.jar"]


#192.168.0.175
#docker build -t dockerhw .
#docker run -ti --rm -e DISPLAY=$DISPLAY dockerhw

#docker run -ti --rm -e DISPLAY=$DISPLAY -e GOOGLE_APPLICATION_CREDENTIALS=C:\Users\Belle\Downloads\finalprojectbrianhellested-5341e4d0baf2.json hellestedfinalproject  

# docker run -ti --rm -e DISPLAY=$DISPLAY hellestedfinalproject

#docker build -t hellestedfinalproject .
#docker run -ti --rm -e DISPLAY=$DISPLAY -e GOOGLE_APPLICATION_CREDENTIALS=./cred/cred.json hellestedfinalproject //still need to bind local directories