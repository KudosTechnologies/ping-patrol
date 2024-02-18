Feature: Monitors can be deleted

  Background:
    Given delete all existing monitors

  Scenario: Patch a monitor
    Given create monitor with name "kudostech.ro" and default values
    When patch monitor with name "kudos_updated" and type "PING"
    Then statusCode is "200"
    And updated monitor should have name "kudos_updated" and type "PING" and url "https://www.google.com" and monitoring interval 60 and monitor timeout 100

  Scenario: Error case: Patch Monitor is not possible with invalid name
    Given create monitor with name "kudostech.ro" and default values
    When update monitor with name "" and type "PING" and url "https://www.google.com" and monitoring interval 100 and monitor timeout 200
    Then statusCode is "400"
    And contains error message "Method Argument Exception" with violation field "/name" and violation message "must not be blank"