# REST Assured Capstone Project

API automation framework built with REST Assured, TestNG, and Extent Reports.

## Tech Stack

- **Java 17**
- **Maven** - build and dependency management
- **REST Assured** - API testing library
- **TestNG** - test runner with priority and dependency support
- **Extent Reports** - HTML test execution reports
- **Jackson** - JSON serialization for POJO-based requests

## Project Structure

```
src/
  main/java/com/capstone/
    config/        ConfigReader       - reads environment property files
    endpoints/     Routes             - API endpoint path constants
    pojo/          User               - data model for user requests
    utils/         ApiHelper          - reusable request specifications
                   TestDataGenerator  - random test data generation
  test/java/com/capstone/
    base/          BaseTest           - TestNG setup with Extent Reports
    tests/         UserApiTest        - 5 user CRUD tests (GoRest API)
                   PostApiTest        - 3 post/comment tests (GoRest API)
                   ProductApiTest     - 3 product tests (AutomationExercise API)
  test/resources/
    qa.properties                     - QA environment config
    uat.properties                    - UAT environment config
```

## Setup

1. Install Java 17 and Maven
2. Get a free API token from https://gorest.co.in/ (register with any email)
3. Paste your token in `src/test/resources/qa.properties` and `uat.properties`:
   ```
   gorest.token=YOUR_TOKEN_HERE
   ```

## Running Tests

Run all tests (defaults to QA environment):
```
mvn test
```

Run with a specific environment:
```
mvn test -Denv=qa
mvn test -Denv=uat
```

Run a specific test class:
```
mvn test -Dtest=UserApiTest
mvn test -Dtest=PostApiTest
mvn test -Dtest=ProductApiTest
```

## Test Report

After execution, the Extent HTML report is generated at:
```
reports/ExtentReport.html
```

Open it in a browser to see execution summary, passed/failed tests, and error details.

## APIs Under Test

| API               | Base URL                          | Auth           |
|-------------------|-----------------------------------|----------------|
| GoRest Users      | https://gorest.co.in              | Bearer Token   |
| GoRest Posts      | https://gorest.co.in              | Bearer Token   |
| Product Catalog   | https://automationexercise.com    | None           |

## Test Cases (11 total)

**User API (5 tests):** Create, Get by ID, Get all, Update, Delete
**Post API (3 tests):** Get all posts, Create post for user, Comment on post
**Product API (3 tests):** Get product list, Get product details, Search product
