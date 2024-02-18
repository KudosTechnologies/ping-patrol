Feature: Monitors can be deleted

  Background:
    Given delete all existing monitors

  Scenario: Update a monitor
    Given create monitor with name "kudostech.ro" and default values
    When update monitor with name "kudos_updated" and type "PING" and url "https://example.com" and monitoring interval 100 and monitor timeout 200
    Then statusCode is "200"
    And updated monitor should have name "kudos_updated" and type "PING" and url "https://example.com" and monitoring interval 100 and monitor timeout 200


  Scenario: Error case: Create Monitor is not possible with invalid url
    Given create monitor with name "kudostech.ro" and default values
    When update monitor with name "kudos_updated" and type "PING" and url "http:ex." and monitoring interval 100 and monitor timeout 200
    Then statusCode is "400"
    And contains error message "Method Argument Exception" with violation field "/url" and violation message "must match \"^((https?|ftp|smtp)://)?(www.)?[a-z0-9]+\.[a-z]+(/[a-zA-Z0-9#]+/?)*$\""