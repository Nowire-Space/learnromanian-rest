Feature: Profile
  Profile Controller contains all the endpoints which are responsible for CRUD operations on users profile.

  Scenario Outline: User logs in and submits GET request trying to pull his profile data based on the username.
    Given active and enabled user with following <userFamilyName>, <userFirstName>, <phoneNumber>, <email>, <password>
    When user proceeds with log in with <email> and <password>
    And user submits GET profile request for the <userProfile>
    Then user's data is pulled

    Examples:
      | userFamilyName | userFirstName | phoneNumber  | email             | password | userProfile |
      | Doe            | John          | +40555555555 | john.doe@mail.com | john.doe |john.doe@mail.com             |

