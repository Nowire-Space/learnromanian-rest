@Registration
Feature: Registration
  Users should be able to submit POST requests with registration body that should be approved/rejected by admin and validate provided email

  Background: Saved to the DB user
    Given following roles:
      | Id | Role      |
      | 1  | ADMIN     |
      | 2  | MODERATOR |
      | 3  | PROFESSOR |
      | 4  | STUDENT   |
    And ADMIN users with following:
      | Family Name | First Name | Phone Number | Email                   | Password       |
      | Hopkins     | Amanda     | +40111111111 | amanda.hopkins@mail.com | amanda.hopkins |
    And user amanda.hopkins@mail.com sends POST authentication request with amanda.hopkins password

  Scenario Outline: Positive Scenario: User submits POST request with registration body, data is saved to DB. User validates email. Admin approves registration request.
    Given user registration request with following: <userFamilyName>, <userFirstName>, <phoneNumber>, <email> and <password>
    When user <email> sends POST registration request
    And user <email> sends POST validation request
    And user amanda.hopkins@mail.com sends POST enable request with STUDENT role
    Then API responds with <responseCode> HTTP code
    And user's data is saved to db and user's account is enabled

    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email             | password | responseCode |
      | Doe            | John          | +40555555555 | john.doe@mail.com | john.doe | 300          |

  Scenario Outline: Positive Scenario: User submits POST request with registration body, data is saved to DB. Admin rejects registration request.
    Given user registration request with following: <userFamilyName>, <userFirstName>, <phoneNumber>, <email> and <password>
    When user <email> sends POST registration request
    And user amanda.hopkins@mail.com sends POST reject request
    Then API responds with <responseCode> HTTP code
    And user's data is erased from DB

    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email             | password | responseCode |
      | Doe            | John          | +40555555555 | john.doe@mail.com | john.doe | 200          |

  Scenario Outline: Negative Scenario: User submits POST request with registration body, data is saved to DB. Admin is trying to approve registration request for not enabled account.
    Given user registration request with following: <userFamilyName>, <userFirstName>, <phoneNumber>, <email> and <password>
    When user <email> sends POST registration request
    And user amanda.hopkins@mail.com sends POST enable request with STUDENT role
    Then API responds with <responseCode> HTTP code

    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email             | password | responseCode |
      | Doe            | John          | +40555555555 | john.doe@mail.com | john.doe | 400          |

  Scenario Outline: Negative Scenario: User submits POST request without email, error message is returned.
    Given user registration request with following: <userFamilyName>, <userFirstName>, <phoneNumber>, <email> and <password>
    When user <email> sends POST registration request
    Then API responds with <responseCode> HTTP code

    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email     | password | responseCode |
      | Doe            | John          | +40555555555 | something | john.doe | 400          |
