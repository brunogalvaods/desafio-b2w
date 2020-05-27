FROM java:8
MAINTAINER Bruno Galvao
VOLUME /tmp
ADD target/desafio-0.0.1-SNAPSHOT.jar desafio.jar
RUN bash -c 'touch /desafio.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/desafio.jar"]
