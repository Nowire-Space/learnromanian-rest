@Security
Feature: Security
Users with defined roles enabled by ADMIN are allowed to authenticate and get users profile.

    Scenario Outline: Positive scenario: Admin approves registration request. Users can authenticate and get the profiles based on roles.
      Given validated user with following <userFamilyName>, <userFirstName>, <phoneNumber>, <email> and <password>
      And active and enabled admin user with <adminFamilyName>, <adminFirstName>, <adminPhoneNumber>, <adminEmail> and <adminPassword>
      And admin is logging with <adminEmail> and <adminPassword>
      And admin enable the user account
      Then users can access the user profile <email>


      Examples:
        | userFamilyName | userFirstName | phoneNumber  | email             | password | adminFamilyName | adminFirstName | adminPhoneNumber | adminEmail              | adminPassword  |
        | Doe            | John          | +40555555555 | john.doe@mail.com | john.doe | Hopkins         | Amanda         | +40111111111     | amanda.hopkins@mail.com | amanda.hopkins |