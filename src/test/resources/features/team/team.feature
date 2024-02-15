@Team
Feature: Teams management
  Admins, moderators and professors should be able to create teams, manage students inside team and delete them.

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
    And PROFESSOR users with following:
      | Family Name | First Name | Phone Number | Email             | Password |
      | Doe         | John       | +40555555555 | john.doe@mail.com | john.doe |
    And user amanda.hopkins@mail.com sends POST authentication request with amanda.hopkins password

  Scenario Outline: Positive scenario: Admin user submits POST request with new team request body, data is saved to the DB.
    When user amanda.hopkins@mail.com sends POST team creation request with <teamName> and <teamDescription>
    Then API responds with 200 HTTP code
    And new team with <teamName>, <teamDescription> is created and team head is amanda.hopkins@mail.com

    Examples:
      | teamName | teamDescription    |
      | Team 1   | Team 1 description |

  Scenario Outline: Admin user submits POST request for removing the professor user from the team.
    Given STUDENT users with following:
      | Family Name | First Name | Phone Number | Email             | Password |
      | Cary        | Jim        | 11223344     | jim.cary@mail.com | jim.cary |
    When user amanda.hopkins@mail.com sends POST team creation request with <teamName> and <teamDescription>
    And user john.doe@mail.com sends POST add student request for <userEmail> to <teamName>
    And user amanda.hopkins@mail.com sends POST remove student request for <userEmail> from <teamName>
    Then API responds with 200 HTTP code
    And user <userEmail> is removed from the team <teamName>

    Examples:
      | teamName | teamDescription    | userEmail         |
      | Team 1   | Team 1 description | jim.cary@mail.com |

#  Scenario Outline: Positive scenario: Users with adequate roles can remove the students from the teaam.
#    Given team registration request with <name> and <description>
#    And active and enabled student with <studentFamilyName>, <studentFirstName>, <studentPhoneNumber>, <studentEmail> and <studentPassword>
#    When user with the email address <username> added to the team <name>.
#    And user make a delete request for the username <username> from the team <name>
#    Then user with the username <username> is deleted from the team.
#
#    Examples:
#      | name | description | username                | studentFamilyName | studentFirstName | studentPhoneNumber | studentEmail            | studentPassword |
#      | TEST | Advanced    | bogdan.dabija@yahoo.com | Dabija            | Bogdan           | +40743068088       | bogdan.dabija@yahoo.com | bogdan123       |
