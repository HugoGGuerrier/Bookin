# Bookin : The open librery project

## Dependencies

Before building and running, you must install all these tools

* npm : [The node package manager](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
* JDK 11 : The java developpment kit 11
* maven : [The java building tool](https://maven.apache.org/install.html)

## How to build Bookin

### Auto build

* If you're using Unix then you're a true programmer and a have superior mind, just run `autobuild.sh` to automatically build the Bookin app in a jar.
* If you're using macOS, you can try to run the `autobuild.sh` script. If it fails, build the app manually.
* If you're using Windows then build manually you peasant (or run `autobuild.bat` like the peasant you are).

### Manual build

* (1) Run `npm install` inside the `./frontend` directory
* (2) Run `npm run build` inside the `./frontend` directory
* (3) Copy all files and folders inside `./frontend/dist` directory
* (4) Paste them in the `./backend/src/main/ressources/static` directory
* (5) Run `mvn clean install spring-boot:repackage` inside the `./backend` directory
* The runnable JAR is in `./backend/src/target` with a name like `bookin_back-x.x.x.jar`

### How to deploy and run Bookin

## The book downloader

The book downloader is a Java application that can download books from the Guttenberg project API and create a book database for Bookin. For now it only support Guttenberg project but other sources may appear in the future.
To use it, go into the project file and read the README.md.

