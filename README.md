# gist-tool

## Prerequisites

Ensure you have the following installed:
* Java 21
* Docker

## Building the application

To build the file, navigate to the root folder and run

```./gradlew build```

## Running the application

Set environment variables in ``docker-compose.yml`` file to configure communication with Github & your Pipedrive organization.

To run the application, navigate to the root folder and run

``docker compose up``

## Using the application

* GET /api/gist/ - Get account names that have been scanned.
* POST /api/gist/upload/{username} - Scan user's Github gists and add them to Pipedrive

