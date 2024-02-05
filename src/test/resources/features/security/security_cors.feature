Feature: Cross-origin resource sharing
  This feature file is created in order to test Spring Security and CORS configuration

  Scenario: CORS positive scenario: OPTIONS request is sent to check if API endpoint accepts requests from the origin it is configured for
    Given user johndoe@mail.com is trying to access /account/ endpoint from the origin, API is configured for
    When browser sends OPTIONS request with GET HTTP method for ACPM header
    Then API responds with 200 HTTP code

  Scenario: CORS negative scenario: OPTIONS request is sent to check if API endpoint accepts requests from the wrong origin
    Given user johndoe@mail.com is trying to access /account/ endpoint from the wrong origin
    When browser sends OPTIONS request with GET HTTP method for ACPM header
    Then API responds with 403 HTTP code