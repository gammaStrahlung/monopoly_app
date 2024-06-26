# Android Monopoly App

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gammaStrahlung_monopoly_app&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=gammaStrahlung_monopoly_app)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=gammaStrahlung_monopoly_app&metric=bugs)](https://sonarcloud.io/summary/new_code?id=gammaStrahlung_monopoly_app)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=gammaStrahlung_monopoly_app&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=gammaStrahlung_monopoly_app)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=gammaStrahlung_monopoly_app&metric=coverage)](https://sonarcloud.io/summary/new_code?id=gammaStrahlung_monopoly_app)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=gammaStrahlung_monopoly_app&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=gammaStrahlung_monopoly_app)


## Description

This project is an Android application that simulates the classic board game Monopoly. Designed to provide a seamless and interactive gaming experience, the app features multiplayer functionality and a user-friendly interface.

### Features
- **Multiplayer Gameplay:** Support for 2-8 players in a single session.

## Getting started

### Prerequisites

A Java JDK 17 installation is needed to run this application.
Newer versions should also work but this has not been tested and is not guaranteed to work.
This project uses the [Gradle](https://gradle.org/) Build Tool which provides the `gradlew` wrapper, which automatically downloads the required Gradle version.

A running [Monopoly Server](https://github.com/gammaStrahlung/monopoly_server). You can also use the `se2-demo` Android flavor to use the provided server.

### Building and Running:

* **Clone the Repository:**
   ```sh
   git clone https://github.com/gammaStrahlung/monopoly_app
   ```
* In Android studio:
   * **Open and Build Project:**
      - Open the project in Android Studio.
      - Build the project by navigating to `Build -> Rebuild Project`.
   * **Run the App:**
      - Connect an Android device or use the Android emulator.
      - Run the app by pressing `Run -> Run 'app'`.
* On the command line:
   * Execute the following command to build the app:
      ```sh
      ./gradlew build
      ```
   * To install it to a running emulator or a connected device use:
      ```sh
      ./gradlew installDebug
      ```

## Development

This project uses [OkHTTP](https://square.github.io/okhttp/) for the WebSocket connection to the server.

Code quality checking and coverage analysis is provided by [SonarCloud](https://sonarcloud.io/project/overview?id=gammaStrahlung_monopoly_app).

### Testing

Tests can be run locally using the following command:
```sh
./gradlew test
```
A detailed code coverage analysis report generated by [JaCoCo](https://www.jacoco.org/) can be viewed in the `app/build/reports/jacoco/jacocoTestReport/html` folder after executing this command.
