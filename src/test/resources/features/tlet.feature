Feature: Verify end to end functionality of Trade letters

  @test @stage1 @regression
  Scenario: Verify add,search,update and delete functionality of Trade letters
    Given User navigates to IAPortal
    When User clicks on "Trade Letter" URL
    When User add new Trade letter with below information
      | accountID            |                   1684 |
      | portfolioId          |                 168402 |
      | admin                | Shimura, Mitsuhiro     |
      | excludedSecType      | Equities               |
      | Include Signature    | Y                      |
      | Include Summary Page | Y                      |
      | Inactive             | N                      |
      | Name                 | Automation TLET User   |
      | city                 | Boston                 |
      | zip                  |                  02151 |
      | Email                | Y                      |
      | Fax                  | Y                      |
      | deliveryName         | Automation-Fax-User    |
      | FaxNumber            | 888-473-2963           |
      | Subject              | Automation-Fax-Subject |
      | Print                | N                      |
    Then User should be presented with the below message
      | Record has been added Trade Letter Id |
    When User search for Trade Letter with below info
      | Relationship Name | WTC-CIF Monthly    |
      | Account Id        |               1684 |
      | Portfolio Id      |             168402 |
      | Account Admin     | Shimura, Mitsuhiro |
      | Created From Date | Today              |
      | Created To Date   | Today              |
    Then Verify search results are displayed with below info
      | relationshipName | WTC-CIF Monthly    |
      | accountId        |               1684 |
      | portfolioId      |             168402 |
      | acctAdmin        | Shimura, Mitsuhiro |
    When User updates Trade Letter with below info
      | accountID   |           0012 |
      | portfolioId |         001203 |
      | acctAdmin   | Hum, Catherine |
    Then User should be presented with the below message
      | Record has been Updated |
    When User search for Trade Letter with below info
      | Relationship Name | Jackson National |
      | Account Id        |             0012 |
      | Portfolio Id      |           001203 |
      | Account Admin     | Hum, Catherine   |
      | Created From Date | Today            |
      | Created To Date   | Today            |
    Then Verify search results are displayed with below info
      | accountId       |           0012 |
      | portfolioId     |         001203 |
      | acctAdmin       | Hum, Catherine |
      | createdFromDate | Today          |
      | status          | Active         |
    When User delete a Trade Letter
    Then User should be presented with the below message
      | Record(s) have been Deleted |

  @test @stage @regression
  Scenario: Verify add,search,copy and delete functionality of Trade letters
    Given User navigates to IAPortal
    When User clicks on "Trade Letter" URL
    When User add new Trade letter with below information
      | accountID            |                   1684 |
      | portfolioId          |                 168402 |
      | admin                | Shimura, Mitsuhiro     |
      | excludedSecType      | Equities               |
      | Include Signature    | Y                      |
      | Include Summary Page | Y                      |
      | Inactive             | N                      |
      | Name                 | Automation TLET User   |
      | city                 | Boston                 |
      | zip                  |                  02151 |
      | Email                | Y                      |
      | Fax                  | Y                      |
      | deliveryName         | Automation-Fax-User    |
      | FaxNumber            | 888-473-2963           |
      | Subject              | Automation-Fax-Subject |
      | Print                | N                      |
    Then User should be presented with the below message
      | Record has been added Trade Letter Id |
    When User search for Trade Letter with below info
      | Relationship Name | WTC-CIF Monthly    |
      | Account Id        |               1684 |
      | Portfolio Id      |             168402 |
      | Account Admin     | Shimura, Mitsuhiro |
      | Created From Date | Today              |
      | Created To Date   | Today              |
    When User copy Trade Letter with below info
      | accountID   |           0012 |
      | portfolioId |         001203 |
      | acctAdmin   | Hum, Catherine |
    Then User should be presented with the below message
      | Record has been added Trade Letter Id |
    When User search for Trade Letter with below info
      | Relationship Name | Jackson National |
      | Account Id        |             0012 |
      | Portfolio Id      |           001203 |
      | Account Admin     | Hum, Catherine   |
      | Created From Date | Today            |
      | Created To Date   | Today            |
    When User delete a Trade Letter
    Then User should be presented with the below message
      | Record(s) have been Deleted |

  @test @stage @regression
  Scenario: Verify add,search and deliver functionality of Trade letters
    Given User navigates to IAPortal
    When User clicks on " Trade Letter " URL
    When User add new Trade letter with below information
      | accountID            |                   1684 |
      | portfolioId          |                 168402 |
      | admin                | Shimura, Mitsuhiro     |
      | excludedSecType      | Equities               |
      | Include Signature    | Y                      |
      | Include Summary Page | Y                      |
      | Inactive             | N                      |
      | Name                 | Automation TLET User   |
      | city                 | Boston                 |
      | zip                  |                  02151 |
      | Email                | Y                      |
      | Fax                  | Y                      |
      | deliveryName         | Automation-Fax-User    |
      | FaxNumber            | 888-473-2963           |
      | Subject              | Automation-Fax-Subject |
      | Print                | N                      |
    Then User should be presented with the below message
      | Record has been added Trade Letter Id |
    When User search for Trade Letter with below info
      | Relationship Name | WTC-CIF Monthly    |
      | Account Id        |               1684 |
      | Portfolio Id      |             168402 |
      | Account Admin     | Shimura, Mitsuhiro |
      | Created From Date | Today              |
      | Created To Date   | Today              |
    When User generate the Trade Letter from maintenance screen
    Then User should be presented with the below message
      | Trade letter(s) have been delivered. |
    When User delete a Trade Letter
    Then User should be presented with the below message
      | Record(s) have been Deleted |

  @test @stage @regression
  Scenario: Verify add,search and On Demand functionality of Trade letters
    Given User navigates to IAPortal
    When User clicks on "Trade Letter" URL
    When User add new Trade letter with below information
      | accountID            |                   1684 |
      | portfolioId          |                 168402 |
      | admin                | Shimura, Mitsuhiro     |
      | excludedSecType      | Equities               |
      | Include Signature    | Y                      |
      | Include Summary Page | Y                      |
      | Inactive             | N                      |
      | Name                 | Automation TLET User   |
      | city                 | Boston                 |
      | zip                  |                  02151 |
      | Email                | Y                      |
      | Fax                  | Y                      |
      | deliveryName         | Automation-Fax-User    |
      | FaxNumber            | 888-473-2963           |
      | Subject              | Automation-Fax-Subject |
      | Print                | N                      |
    Then User should be presented with the below message
      | Record has been added Trade Letter Id |
    When User search for Trade Letter with below info
      | Relationship Name | WTC-CIF Monthly    |
      | Account Id        |               1684 |
      | Portfolio Id      |             168402 |
      | Account Admin     | Shimura, Mitsuhiro |
      | Created From Date | Today              |
      | Created To Date   | Today              |
    When User navigate to Trade Letters-On Demand
    And User generate the Trade Letter
    Then User should be presented with the below message
      | Trade letter(s) have been delivered. |

  @test @stage @prod @smoke
  Scenario: Verify search functionality of all Trade Letter
    Given User navigates to IAPortal
    When User clicks on "Trade Letter" URL
    And User clicks on Search button
    Then Verify search result is displayed

  @test @stage @prod @smoke
  Scenario: Verify search functionality of a particular Trade Letter
    Given User navigates to IAPortal
    When User clicks on "Trade Letter" URL
    When User search for Trade Letter with below info
      | Trade Letter ID   |          2322 |
      | Relationship Name | ALPS Advisors |
      | Account Id        | 3J65          |
      | Portfolio Id      | 3J6502        |
      | Created From Date | 08/06/2009    |
      | Created To Date   | Today         |
    Then Verify search results are displayed with below info
      | relationshipName | ALPS Advisors |
      | accountId        | 3J65          |
      | portfolioId      | 3J6502        |
      | createdFromDate  | 08/06/2009    |
      | status           | Active        |

  @test @stage @prod @smoke
  Scenario: Verify deliver screen of Trade Letter
    Given User navigates to IAPortal
    When User clicks on "Trade Letter" URL
    And User clicks on Search button
    And User selects a search result
    And User clicks on Deliver Selected button
    Then User should be able to access "Deliver" screen

  @test @stage @prod @smoke
  Scenario: Verify deliver screen of Trade Letter
    Given User navigates to IAPortal
    When User clicks on "Trade Letter" URL
    And User navigate to Trade Letters-On Demand
    And User clicks on Preview in ER button
    Then User should be able to access "Enterprise Reporting" screen
