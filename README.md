# GitHub Repo Listing API

The GitHub Repo Listing API is a Java application that allows to retrieve and list GitHub repositories for a user. It uses the GitHub API to fetch the repositories and provides paginated results .

## Features

- Fetch and list GitHub repositories for a user.
- Paginate through the repositories.
- Filter out forked repositories.
- Retrieve branches for each repository with last commits including SHA.

## Technologies Used

- Java 17
- Spring Boot 3
- Swagger
- GitHub API

## Setup

1. Clone or download the repository to your local machine.
   ```shell
   git clone https://github.com/magresh/github-repo-listing-api.git
   ```

2. Navigate to the project directory:
   ```shell
   cd github-repo-listing-api
   ```

3. Build the application using Gradle:
   ```shell
   ./gradlew build
   ```
4. Run the application:
   ```shell
   ./gradlew bootRun
   ```
   The application will start running on [localhost:8080](http://localhost:8080)


5. Access the API documentation at [localhost:8080/doc](http://localhost:8080/doc)


6. If you have a GitHub token and want to increase the API rate limit, add your token to the `application.properties` file. This can be done by modifying the `github.token` property.

## Usage

1. Make a GET request to the [localhost:8080/api/v1/github/user/{username}](http://localhost:8080/api/v1/github/user/) endpoint to retrieve the GitHub repositories for a specific user.

2. The API response will contain paginated repositories, including information such as the repository name, owner, and branches.

3. You can specify the page number and size as query parameters to navigate through the repositories.

## Testing

The GitHub Repo Listing API project includes a set of tests to ensure its functionality and behaviour. The tests cover various components, services, and API endpoints.

To run the tests, follow these steps:

1. Open a terminal or command prompt.

2. Navigate to the project directory.

3. Execute the following command to run the tests using Gradle:

   ```shell
   ./gradlew test
   ```
    This command will run all the tests and display the results in the terminal.

5. Review the test results to verify that all tests pass successfully. Any failed tests will be reported along with relevant error messages.
## Response

Here's a sample response for the request `GET /api/v1/github/user/MagresH?page=1&size=2`:

```json
{
  "content": [
    {
      "name": "CourseJ",
      "owner": {
        "login": "MagresH"
      },
      "branches": [
        {
          "name": "dev",
          "commit": {
            "sha": "7df4c4969cf11e1fecb64e381985a8f35f6558ab"
          }
        },
        {
          "name": "master",
          "commit": {
            "sha": "76a3ae6ca1f59792fd31c913e25c3b302d32bd7b"
          }
        }
      ]
    },
    {
      "name": "github-repo-listing-api",
      "owner": {
        "login": "MagresH"
      },
      "branches": [
        {
          "name": "dev",
          "commit": {
            "sha": "7d139a2a19cd27a4b3b5ace2854859af266320e8"
          }
        },
        {
          "name": "master",
          "commit": {
            "sha": "50a872d9e9363fabf54929d7159730c33ef23541"
          }
        }
      ]
    }
  ],
  "pageable": {
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 2,
    "pageNumber": 1,
    "pageSize": 2,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 6,
  "totalPages": 3,
  "size": 2,
  "number": 1,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": false,
  "numberOfElements": 2,
  "empty": false
}
