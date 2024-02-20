@Security
Feature: Authentication
  This feature file is created in order to test authentication security configuration

  Background: Saved to the DB user
    Given following roles:
      | Id | Role      |
      | 1  | ADMIN     |
      | 2  | MODERATOR |
      | 3  | PROFESSOR |
      | 4  | STUDENT   |
    And STUDENT users with following:
      | Family Name | First Name | Phone Number | Email             | Password |
      | Doe         | John       | +40555555555 | john.doe@mail.com | john.doe |

  Scenario: Authentication positive scenario: User is trying to log in using correct password and access the endpoint he is allowed to
    When user john.doe@mail.com sends POST authentication request with john.doe password
    And  user john.doe@mail.com sends GET profile request with saved token
    Then API responds with 200 HTTP code

  Scenario: Authentication negative scenario: User is trying to log in using correct password and access the endpoint he is not allowed to
    When user john.doe@mail.com sends POST authentication request with john.doe password
    And  user john.doe@mail.com sends GET all profiles request with saved token
    Then API responds with 403 HTTP code

  Scenario: Authentication negative scenario: User is trying to log in using incorrect password
    When user john.doe@mail.com sends POST authentication request with doe.doe password
    Then API responds with 403 HTTP code
