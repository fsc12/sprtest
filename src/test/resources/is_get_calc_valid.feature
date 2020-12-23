Feature: the version can be retrieved

  Scenario: client makes call to GET /calc
    When the client calls /calc
    Then the client receives status code of 200
