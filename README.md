# oop-project-rental

This was an object oriented programming project in the fourth semester of university.

The brief was to create a library style application with a functional login system (with two types of user accounts) that has different types of objects to loan. Please see the wiki for more information on the project and some reflection.

## Requirements

The requirements I adapted from the generic ones were as follows:

- The system must be able to handle multiple types of vehicles, namely motorcycles, cars, vans and trucks.
- The system must show the current number of vehicles and the current number of each type of vehicle available to be rented.
- The system must be able to mark vehicles as currently rented/loaned and show the start and end dates of the current rental contract.
- The system must be able to mark vehicles as having been returned and put them back into the list of available vehicles.
- The system should be able to print information about a single vehicle to a file including the details about the vehicle, whether it is available and the start and end dates of the current rental period.
- The system must allow the user to search for an item by any of its details.
- The system must have a functional login system with two different types of account, standard and administrator.
- The application must have the ability to register a new account through the graphical user interface.
- The system should allow administrators to review and approve or deny newly registered user accounts.

## Feedback

As part of the submission I received some feedback on the project which I will explain in the following.

-	It would have been better to auto-refresh the table after approving an account.

    This feedback stemmed from a testing plan that didn't comprehensively cover all of the functionality of the program. The tables for users do auto-refresh when an account is approved using the Observer pattern. The table is an observer and the main array of accounts is set up as a subject. I hadn't tested what happened if I created an account with the same name as an already created account which resulted in 2 accounts with the same name. I approved one account and the name remained because there were 2 accounts pending with the same name.

-	A way to view only available cars would be useful to have.

    I had planned to implement views for available vehicles but ran out of time towards the end.

-	The MainWindow class contains most of your code, and performs multiple functions. Following the principles of object oriented programming, it would have been better to split its functionalities into several classes.

    Abstraction
