@tag
Feature: The second feature file
  I want to use this template for my feature file

  @regression
  Scenario: The second Scenario
    Given This is given step
    When This is when step
    Then This is then step

  Scenario Outline: scenario outline
    Given I am scenario outline
    
    Examples:
    | test  | pass	|
    | user1 | pass1 |
    | user2 | pass2 |
     