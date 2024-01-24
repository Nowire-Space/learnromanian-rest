@Security
Feature: Security
  This feature file is created in order to test Spring Security and CORS configuration

  Background: Saved to the DB user
    Given following roles
      | id | role      |
      | 1  | ADMIN     |
      | 2  | MODERATOR |
      | 3  | PROFESSOR |
      | 4  | STUDENT   |
    Given user with following
      | Doe | John | +40555555555 | john.doe@mail.com | john.doe |

  Scenario: Authentication positive scenario: User is trying to log in using correct password
    When user john.doe@mail.com sends POST authentication request with john.doe password
    Then API responds with 200 HTTP code

  Scenario: Authentication negative scenario: User is trying to log in using incorrect password
    When user john.doe@mail.com sends POST authentication request with doe.doe password
    Then API responds with 403 HTTP code

  Scenario: CORS positive scenario: OPTIONS request is sent to check if API endpoint accepts requests from the origin it is configured for
    Given user johndoe@mail.com is trying to access /account/ endpoint from the origin, API is configured for
    When browser sends OPTIONS request with GET HTTP method for ACPM header
    Then API responds with 200 HTTP code

  Scenario: CORS negative scenario: OPTIONS request is sent to check if API endpoint accepts requests from the wrong origin
    Given user johndoe@mail.com is trying to access /account/ endpoint from the wrong origin
    When browser sends OPTIONS request with GET HTTP method for ACPM header
    Then API responds with 403 HTTP code