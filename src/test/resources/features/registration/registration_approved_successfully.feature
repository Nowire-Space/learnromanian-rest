Feature: Registration approved successfully
  Users should be able to submit POST requests with registration body that should be approved/rejected by admin.

  Scenario Outline: User submits POST request with registration body, data is saved to DB. Admin approves registration request.
    Given user with <userFamilyName> and <userLastName>
    When user submits POST registration request
    Then data is saved to DB

    Examples:
      | userFamilyName | userLastName |
      | Doe            | John         |