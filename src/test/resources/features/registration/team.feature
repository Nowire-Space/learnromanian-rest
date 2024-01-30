@Team
Feature: Teams management
  Admins, moderators and professors should be able to create teams, manage students inside team and delete them.

  Scenario Outline: Positive scenario: Admin user submits POST request with new team request body, data is saved to the DB.
    Given active team admin and professor users with <adminFamilyName>, <adminFirstName>, <adminPhoneNumber>, <adminEmail>, <adminPassword>, <professorFamilyName>, <professorFirstName>, <professorPhoneNumber>, <professorEmail> and <professorPassword>
    When team admin proceeds with log in with <adminEmail> and <adminPassword>
    And admin user submits POST team request for team creation with <teamName> and <teamDescription>
    Then new team with <teamName>, <teamDescription> is created and team head is <professorEmail>

    Examples:
      | adminFamilyName | adminFirstName | adminPhoneNumber | adminEmail              | adminPassword  | teamName | teamDescription    | professorFamilyName | professorFirstName | professorPhoneNumber | professorEmail    | professorPassword |
      | Hopkins         | Amanda         | 40111111111      | amanda.hopkins@mail.com | amanda.hopkins | Team 1   | Team 1 description | Doe                 | John               | +40555555555         | john.doe@mail.com | john.doe          |

  Scenario Outline: Admin user submits POST request for removing the professor user from the team.
    Given active team admin and professor users with <adminFamilyName>, <adminFirstName>, <adminPhoneNumber>, <adminEmail>, <adminPassword>, <professorFamilyName>, <professorFirstName>, <professorPhoneNumber>, <professorEmail> and <professorPassword>
    And active team user with <userFamilyName>, <userFirstName>, <userPhoneNumber>, <userEmail>, <userPassword>
    When team admin proceeds with log in with <adminEmail> and <adminPassword>
    And admin user submits POST team request for team creation with <teamName> and <teamDescription>
    And student <userEmail> is added to the team <teamName>.
    When admin user submits POST team request for removing the user <userEmail> from the team <teamName>
    Then user <userEmail> is removed from the team <teamName>

    Examples:
      | adminFamilyName | adminFirstName | adminPhoneNumber | adminEmail              | adminPassword  | teamName | teamDescription    | professorFamilyName | professorFirstName | professorPhoneNumber | professorEmail    | professorPassword | userFamilyName | userFirstName | userPhoneNumber | userEmail         | userPassword | studentEmail      |  |
      | Hopkins         | Amanda         | 40111111111      | amanda.hopkins@mail.com | amanda.hopkins | Team 1   | Team 1 description | Doe                 | John               | +40555555555         | john.doe@mail.com | john.doe          | Cary           | Jim           | 11223344        | jim.cary@mail.com |              | jim.cary@mail.com |  |

  Scenario Outline: Positive scenario: Users with adequate roles can remove the students from the teaam.
    Given team registration request with <name> and <description>
    And active and enabled student with <studentFamilyName>, <studentFirstName>, <studentPhoneNumber>, <studentEmail> and <studentPassword>
    When user with the email address <username> added to the team <name>.
    And user make a delete request for the username <username> from the team <name>
    Then user with the username <username> is deleted from the team.

    Examples:
      | name | description | username                | studentFamilyName | studentFirstName | studentPhoneNumber | studentEmail            | studentPassword |
      | TEST | Advanced    | bogdan.dabija@yahoo.com | Dabija            | Bogdan           | +40743068088       | bogdan.dabija@yahoo.com | bogdan123       |
