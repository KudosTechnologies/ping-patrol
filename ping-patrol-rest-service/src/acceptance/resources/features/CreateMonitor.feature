Feature: Monitors can be created

  Background:
    Given delete all existing monitors

  Scenario: Create new monitor
    Given the system has no existing monitor with name "kudostech.ro"
    When I create a monitor with name "kudostech.ro" of type "HTTPS" and url "http://kudostech.ro"
    Then statusCode is "201"
    And the system should have a monitor with name "kudostech.ro"

  Scenario: Error case: Create Monitor is not possible with invalid name
    When I create a monitor with name "" of type "HTTPS" and url "http://kudostech.ro"
    Then statusCode is "400"
    And contains error message "Method Argument Exception" with violation field "/name" and violation message "must not be blank"

  Scenario: Error case: Create Monitor is not possible with invalid url
    When I create a monitor with name "kudostech.ro" of type "HTTPS" and url "http:ex."
    Then statusCode is "400"
    And contains error message "Method Argument Exception" with violation field "/url" and violation message "must match \"^((https?|ftp|smtp)://)?(www.)?[a-z0-9]+\.[a-z]+(/[a-zA-Z0-9#]+/?)*$\""