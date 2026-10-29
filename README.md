# GoRest API Automation Framework

Rest Assured automation framework for testing the GoRest public API and AutomationExercise product API.

## Tech Stack

- **Java 11** - Programming language
- **Maven** - Build and dependency management
- **Rest Assured** - API testing library
- **TestNG** - Test framework with priorities and dependencies
- **Extent Reports** - HTML test reporting
- **Jackson** - JSON serialization/deserialization

## Project Structure

```
rest-assured-capstone/
+-- pom.xml                              # Maven config with all dependencies
+-- testng.xml                           # TestNG suite definition
+-- src/
|   +-- main/java/com/capstone/
|   |   +-- config/
|   |   |   +-- ConfigReader.java        # Reads environment property files
|   |   +-- endpoints/
|   |   |   +-- Routes.java              # API endpoint path constants
|   |   +-- pojo/
|   |   |   +-- User.java               # User data model for serialization
|   |   +-- utils/
|   |       +-- ApiHelper.java           # Builds reusable request specifications
|   |       +-- TestDataGenerator.java   # Generates random test data
|   +-- test/
|       +-- java/com/capstone/
|       |   +-- base/
|       |   |   +-- BaseTest.java        # Extent Reports setup + REST Assured logging
|       |   +-- tests/
|       |       +-- UserApiTest.java     # 5 user CRUD tests (GoRest)
|       |       +-- PostApiTest.java     # 3 post/comment tests (GoRest)
|       |       +-- ProductApiTest.java  # 3 product tests (AutomationExercise)
|       +-- resources/
|           +-- qa.properties            # QA environment config
|           +-- uat.properties           # UAT environment config
+-- reports/                             # Generated test reports (after execution)
```

## Prerequisites

1. **Java 11+** installed (`java -version` to verify)
2. **Maven** installed (`mvn -version` to verify)
3. **GoRest API Token** - Get one free at https://gorest.co.in/consumer/login

## Setup Instructions

### 1. Get Your API Token

1. Go to https://gorest.co.in/consumer/login
2. Sign in with GitHub, Google, or Microsoft
3. Copy the access token from your dashboard

### 2. Configure the Token

Open `src/test/resources/qa.properties` and replace the placeholder:

```properties
gorest.token=YOUR_ACTUAL_TOKEN_HERE
```

Do the same for `uat.properties` if you plan to run against UAT.

### 3. Open in IntelliJ IDEA

1. Open IntelliJ IDEA
2. Click **File > Open**
3. Navigate to `C:\Users\sushmitha.saravanan\rest-assured-capstone` and select the folder
4. IntelliJ will detect the `pom.xml` and ask "Trust and Open Maven Project?" - click **Trust Project**
5. Wait for Maven to download all dependencies (progress bar at bottom-right)
6. If dependencies don't auto-download: right-click `pom.xml` > **Maven** > **Reload Project**

### 4. Install TestNG Plugin (if not already installed)

1. Go to **File > Settings > Plugins** (or **IntelliJ IDEA > Preferences > Plugins** on Mac)
2. Search for "TestNG"
3. Install the TestNG plugin and restart IntelliJ if prompted

### 5. Set the JDK

If IntelliJ doesn't detect your JDK:
1. Go to **File > Project Structure > Project**
2. Set Project SDK to your Java 11+ JDK
3. Set Project language level to "11"

## Running Tests

### From IntelliJ

**Run entire suite:**
- Right-click on `testng.xml` in the project explorer > **Run 'testng.xml'**

**Run a single test class:**
- Open any test class (e.g. `UserApiTest.java`)
- Click the green play button next to the class name
- Select **Run 'UserApiTest'**

**Run a single test method:**
- Click the green play button next to any `@Test` method
- Select **Run 'testMethodName'**

### From Command Line (Maven)

Run with QA environment (default):
```bash
mvn test
```

Run with specific environment:
```bash
mvn test -Denv=qa
mvn test -Denv=uat
```

## Test Cases (11 Total)

### User API Tests (UserApiTest.java) - 5 Tests
| # | Test | HTTP Method | Endpoint | Validations |
|---|------|-------------|----------|-------------|
| 1 | Create User | POST | /public/v2/users | Status 201, all fields match, response time |
| 2 | Get User by ID | GET | /public/v2/users/{id} | Status 200, correct ID, fields present |
| 3 | Get All Users | GET | /public/v2/users | Status 200, non-empty list, response time |
| 4 | Update User | PUT | /public/v2/users/{id} | Status 200, updated name/status match |
| 5 | Delete User | DELETE | /public/v2/users/{id} | Status 204, response time |

### Post API Tests (PostApiTest.java) - 3 Tests
| # | Test | HTTP Method | Endpoint | Validations |
|---|------|-------------|----------|-------------|
| 1 | Get All Posts | GET | /public/v2/posts | Status 200, non-empty list, response time |
| 2 | Create Post | POST | /public/v2/users/{id}/posts | Status 201, title/body/user_id match |
| 3 | Comment on Post | POST | /public/v2/posts/{id}/comments | Status 201, name/email/body match |

### Product API Tests (ProductApiTest.java) - 3 Tests
| # | Test | HTTP Method | Endpoint | Validations |
|---|------|-------------|----------|-------------|
| 1 | Get Product List | GET | /api/productsList | Response code 200, list not empty |
| 2 | Get Product Details | GET | /api/productsList | Product ID, name, price, brand present |
| 3 | Search Product | POST | /api/searchProduct | Response code 200, results returned |

## Test Reports

After running tests, the Extent Report HTML file is generated at:

```
reports/ExtentReport.html
```

Open this file in any browser to see:
- Execution summary (total, passed, failed, skipped)
- Individual test results with pass/fail status
- Failure details with stack traces
- Environment info (QA/UAT)

## Environment Switching

The framework reads configuration from property files based on the `-Denv` system property:

- `-Denv=qa` loads `qa.properties` (default)
- `-Denv=uat` loads `uat.properties`

Each property file contains:
- `gorest.baseUrl` - GoRest API base URL
- `gorest.token` - Bearer token for GoRest authentication
- `automationexercise.baseUrl` - AutomationExercise API base URL
- `response.timeout` - Max acceptable response time in milliseconds

## Framework Design

### How the pieces fit together

```
testng.xml  -->  TestNG runs test classes
                      |
                 BaseTest (parent class)
                 - Sets up Extent Reports
                 - Enables REST Assured request/response logging
                 - Creates/flushes report per suite
                      |
              Test Classes (UserApiTest, PostApiTest, ProductApiTest)
              - Use ApiHelper to get pre-configured request specs
              - Use Routes for endpoint paths
              - Use TestDataGenerator for random test data
              - Use User POJO for type-safe request bodies
                      |
              ConfigReader (loads qa.properties or uat.properties)
              - Provides base URLs, tokens, timeouts
              - Selected by -Denv system property
```

### Key Classes

- **ConfigReader** - Loads the correct property file from classpath based on `-Denv`. Provides typed accessors for base URLs, auth tokens, and timeouts.
- **Routes** - Constants for all API endpoint paths. Centralizes path management so changes propagate everywhere.
- **ApiHelper** - Builds reusable `RequestSpecification` objects with base URI, content type, and auth headers pre-configured.
- **TestDataGenerator** - Generates unique names, emails, and User objects to avoid test data collisions.
- **User (POJO)** - Java class that Jackson serializes to JSON for create/update requests.
- **BaseTest** - Parent class for all tests. Sets up Extent Reports with `@BeforeSuite`/`@AfterSuite` and logs each test result with `@BeforeMethod`/`@AfterMethod`.
