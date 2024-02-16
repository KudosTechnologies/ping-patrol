Feature: Monitors can be created
  
  Scenario: Create new monitor
    Given the system has no existing monitor with name "kudostech.ro"
    When I create a monitor with name "kudostech.ro" of type "HTTPS" and url "http://kudostech.ro"
    Then the system should have a monitor with name "kudostech.ro"