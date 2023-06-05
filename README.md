# GitHub Repo Listing API

The GitHub Repo Listing API is a Java application that allows to retrieve and list GitHub repositories for a user. It uses the GitHub API to fetch the repositories and provides paginated results .

## Features

- Fetch and list GitHub repositories for a user.
- Paginate through the repositories.
- Filter out forked repositories.
- Retrieve branches for each repository.

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