Feature: The third test
@scenario
  Scenario: The first Scenario
    Given This is given step
    When This is when step
    Then This is then step
    @scenariooutline
Scenario Outline: Login successfully
       Given I am in the Entry page
       Then The entry page title is <title>
       
   Examples:
    | title | 
    |  index   |  
    |  indexas   | 