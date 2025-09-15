Feature: Note Management
  As a user
  I want to manage my notes
  So that I can keep track of my thoughts

  Scenario: Create a new note successfully
    Given I have a note with title "Shopping List" and content "Buy milk and bread"
    When I create the note
    Then the note should be saved successfully
    And the note should have an ID

  Scenario: Create note with empty title fails
    Given I have a note with empty title
    When I try to create the note
    Then I should get an error message

  Scenario: Update an existing note
    Given I have an existing note with ID 1
    When I update the note title to "Updated Shopping List"
    Then the note should be updated successfully

  Scenario: Delete an existing note
    Given I have an existing note with ID 1
    When I delete the note
    Then the note should be deleted successfully