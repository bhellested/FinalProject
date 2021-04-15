FROM openjdk:11

COPY ./ ./
RUN apt-get update && apt-get install -y libxrender1 libxtst6 libxi6

CMD ["java", "-jar", "projectFrame.jar","projectFrame"]

#docker build -t hellestedfinalproject .
#docker run -ti --rm -e DISPLAY=$DISPLAY -e GOOGLE_APPLICATION_CREDENTIALS=./cred/cred.json hellestedfinalproject //still need to bind local directories