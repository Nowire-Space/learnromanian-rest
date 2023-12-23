@Team
Feature: Team
  Users added to the team.

  Scenario Outline: Positive scenario: Users with adequate roles can add students in the teams created !
    Given team registration request with <name> and <description>
    And active and enabled user with <userFamilyName>, <userFirstName>, <phoneNumber>, <email> and <password>
    And active and enabled student with <studentFamilyName>, <studentFirstName>, <studentPhoneNumber>, <studentEmail> and <studentPassword>
    When user submit POST team request for team creation !
    Then student <studentEmail> is added to the team <name>.



    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email             | password | username          | name | description | studentFamilyName| studentFirstName  |studentPhoneNumber|studentEmail           |studentPassword|studentUser               |
      | Doe            | John          | +40555555555 | john.doe@mail.com | john.doe | john.doe@mail.com | Test | Advanced    | Dabija           |Bogdan             |+40743068088      |bogdan.dabija@yahoo.com|bogdan123      |bogdan.dabija@yahoo.com|
