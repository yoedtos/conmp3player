# ConMP3Player
A console mp3 player made with following API

 - Jaudiotagger
 - Javazoom JL
 - Log4j

### Note 
 - Maybe you need to install manually the JARs into your local Maven repository.
 To do so, run the follow command

`mvn install:install-file -Dfile=extras/tritonus_share-0.3.6.jar -DgroupId=org.tritonus -DartifactId=tritonus-share -Dversion=0.3.6 -Dpackaging=jar`

`mvn install:install-file -Dfile=extras/mp3spi1.9.5.jar -DgroupId=javazoom -DartifactId=mp3spi -Dversion=1.9.5 -Dpackaging=jar`

`mvn install:install-file -Dfile=extras/jaudiotagger-2.2.3.jar -DgroupId=org.jaudiotagger -DartifactId=jaudiotagger -Dversion=2.2.3 -Dpackaging=jar`
