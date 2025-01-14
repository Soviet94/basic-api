### How to Run Mock Employee API (Server module)

Start **Server** Spring Boot application.
`./gradlew server:bootRun`

Each invocation of **Server** application triggers a new list of mock employee data. While live testing, you'll want to keep 
this server running if you require consistent data. Additionally, the web server will randomly choose when to rate
limit requests, so keep this mind when designing/implementing the actual Employee API.

_Note_: Console logs each mock employee upon startup.

### Code Formatting

This project utilizes Gradle plugin [Diffplug Spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) to enforce format
and style guidelines with every build. 

To resolve any errors, you must run **spotlessApply** task.
`./gradlew spotlessApply`

