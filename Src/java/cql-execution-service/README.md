# CQL Execution Service

###Usage:
- Clone repository
- gradle build
- gradle appStart
- Start the CQL Editor (follow instructions from here: https://github.com/c-schuler/Cql_Editor)
- This service is still in development mode and cross domain origin requests have not been implemented yet.
  - Turn off your web browser security features that prohibit cross domain origin requests
    - For Chrome: open the browser from the command line:
      - Open terminal in same directory as chrome.exe
      - Type the following command:
        - chrome.exe --disable-web-security --user-data-dir=<user specified directory>
      - Navigate to localhost:3000
      - Begin writing and running queries!
