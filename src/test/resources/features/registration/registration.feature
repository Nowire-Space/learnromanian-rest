@Registration
Feature: Registration
  Users should be able to submit POST requests with registration body that should be approved/rejected by admin.

  Scenario Outline: User submits POST request with registration body, data is saved to DB. Admin approves registration request.
    Given user with following <userFamilyName>, <userFirstName>, <phoneNumber>, <email>, <password> and <passwordCheck>
    And active admin user with <adminFamilyName>, <adminFirstName>, <adminPhoneNumber>, <adminEmail> and <adminPassword>
    When user submits POST registration request
    And admin proceeds with log in with <adminEmail> and <adminPassword>
    And admin approves registration request
    Then user's data is saved to db and user's account is enabled

    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email             | password | passwordCheck | adminFamilyName | adminFirstName | adminPhoneNumber | adminEmail              | adminPassword  |
      | Doe            | John          | +40555555555 | john.doe@mail.com | john.doe | john.doe      | Hopkins         | Amanda         | +40111111111     | amanda.hopkins@mail.com | amanda.hopkins |

  Scenario Outline: User submits POST request with registration body, data is saved to DB. Admin rejects registration request.
    Given user with following <userFamilyName>, <userFirstName>, <phoneNumber>, <email>, <password> and <passwordCheck>
    And active admin user with <adminFamilyName>, <adminFirstName>, <adminPhoneNumber>, <adminEmail> and <adminPassword>
    When user submits POST registration request
    And admin proceeds with log in with <adminEmail> and <adminPassword>
    And admin rejects registration request
    Then user's data is erased from DB

    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email             | password | passwordCheck | adminFamilyName | adminFirstName | adminPhoneNumber | adminEmail              | adminPassword  |
      | Doe            | John          | +40555555555 | john.doe@mail.com | john.doe | john.doe      | Hopkins         | Amanda        | +40111111111     | amanda.hopkins@mail.com | amanda.hopkins |

  Scenario Outline: User submits POST request with registration body without email, error message is returned.
    Given user with following <userFamilyName>, <userFirstName>, <phoneNumber>, <email>, <password> and <passwordCheck>
    When user submits POST registration request without email
    Then <errorCode> is returned

    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email | password | passwordCheck | errorCode |
      | Doe            | John          | +40555555555 |       | john.doe | john.doe      | 400       |
