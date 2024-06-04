# gist-tool

## Prerequisites

Ensure you have the following installed:
* Java 21
* Docker
* gcloud CLI tool

Additionally, a *Pipeline* on Pipedrive must be set up.

## Building the application

To build the file, navigate to the root folder and run

```./gradlew build```

## Running the application locally

Set environment variables in ``docker-compose.yml`` file to configure communication with Github & your Pipedrive organization.

To run the application, navigate to the root folder and run

``docker compose up``

## Using the application

All requests require the `X-API-KEY` header for authentication. Value must correspond to the `GIST_TOOL_APIKEY` environment variable.

* GET /api/gist/ - Get usernames that are tracked.
* GET /api/gist/latest - Get gists that have been added in between requests.
* PUT /api/gist/scan - Scan for (and add) new gists by tracked users.
* POST /api/gist/upload/{username} - Scan user's Github gists and add them to Pipedrive

