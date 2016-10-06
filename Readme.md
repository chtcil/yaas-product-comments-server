# Yaas Product comments

This is a super simple service used to show small part of YaaS possibilities. 
This service is used as example for SAP Code Jam event in Munich.

### Requirements

- Java 1.8
- Maven 3+

### Running

Just clone the repository and run `mvn clean package` command.
This command will download all dependencies and create war file.

### Deploying

In this repository there is `manifest.yml` file that is used to push this application to CF. When deploying please customize `manifest.yml` to suit your needs.
