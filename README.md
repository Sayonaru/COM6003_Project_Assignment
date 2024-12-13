# COM6003_Lab_Assignment
The aim of this assignment is to develop a Hospital Management App (HMP) to rationalise hospital operations by creating a secure, efficient, and user-friendly platform for admins to manage patients, doctors, surgeries, and appointments.

A development process will be outlined in this README file involving sprints which are directly correlated to the labs provided in the sessions.


## Lab 1 - Gathering User Requirements
To begin with, I will be breaking down the core functionalities of the HMP into Epics and  into their corresponding user stories.
These methods of requirement gathering are used to outline functional requirements, with the security-focused evil user stories highlighting potential risks and vulnerabilities.

Epics will be split into the Business Requirements and Functionalities provided by the brief in order to meet all the desired requirements and explore where the security risks may lie and what core functional requirements are needed.
My method of requirement gathering is to give the key points "ID"s to allow easier review and referencing later in the project. IDs will be given to both the Epics and the User Stories, with the Acceptance Criteria, and Evil User Stories following these in a table format.


| **Epic ID** | **Epic Title**                              | **User Story ID** | **User Story**                                                                                                               | **Acceptance Criteria**                                                                                                                                                                                                                                               | **Evil User Story**                                                                                                          |  
|-------------|---------------------------------------------|-------------------|------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|  
| **1**       | **User Authentication and Role Management** | 1.1               | As a user, I want to log in securely using a username and password so that I can access my account.                          | Users must log in successfully with valid credentials. Passwords must meet security standards (e.g., 8+ characters for SHA-256 to be optimal, alphanumeric, special character). Failed login attempts are logged.                                                     | As an attacker, I want to use brute-force attacks to guess user passwords and gain unauthorised access.                      |  
|             |                                             | 1.2               | As a user, I want to reset my password securely if I forget it.                                                              | Password reset requests require email or multi-factor authentication (MFA). Temporary tokens expire after 15 minutes. New passwords must differ from the last used.                                                                                                   | As an attacker, I want to take advantage of password reset links to take control of a legitimate user's account.             |  
|             |                                             | 1.3               | As a user, I want to log out of the system to prevent unauthorised access to my session.                                     | Users are logged out immediately after clicking the logout button. Idle sessions timeout after 10 minutes of inactivity.                                                                                                                                              | As an attacker, I want to keep a user's session active after they log out so I can continue accessing sensitive information. |  
| **2**       | **Patient Management**                      | 2.1               | As an admin, I want access to view and update patient's information so that I can assess and change any details as required. | Admin dashboard can view a list of patients, being able to select them, modify their details and save the changes. Any unauthorised access attempts are logged, as well as changes to any data.                                                                       | As an attacker, I want to spoof an admin account to view and tamper with sensitive information.                              |  
|             |                                             | 2.2               | As an admin, I want to delete a patient if they are no longer required in the database safely and securely.                  | Admin can delete a user by long-pressing on the patient, with confirmation since important information can be lost easily on accident. Any unauthorised access attempts to others data are logged and deleted data creates a signature to keep track of changes made. | As an attacker, I want to delete a patient's data to cause disruption to patients receiving care.                            |  
| **3**       | **Appointment Management**                  | 3.1               | As a user, I want to view the schedules between doctors and patients.                                                        | Admin can view all of a doctor's and patient's appointment. Data is encrypted so can only be viewed in the dashboard.                                                                                                                                                 | As an attacker, I want to invade patient's privacy by viewing the patient's appointments and doctor's schedule.              |  
|             |                                             | 3.2               | As an admin, I want to manage appointments between doctors and patients.                                                     | Admin will be able to manage appointments between doctors and patients in parallel with data integrity and encryption. Changes are logged and visible to all authorised users.                                                                                        | As an attacker, I want to change a doctor's availability to cause scheduling conflicts.                                      |  
| **4**       | **Security and Audit Logging**              | 4.1               | As an admin, I want to view logs of all user activities for auditing purposes.                                               | Logs must include timestamps, user IDs, and actions performed. Logs must be protected against tampering, e.g. read-only.                                                                                                                                              | As an attacker, I want to delete or modify audit logs to cover up malicious actions.                                         |  
|             |                                             | 4.2               | As a system, I want to detect suspicious activity like failed logins or unauthorised access attempts.                        | Suspicious activities trigger alerts. Logs are kept and analysed for potential threats.                                                                                                                                                                               | As an attacker, I want to bypass security alerts to avoid detection while performing malicious activities.                   |  
| **5**       | **System Administration and Monitoring**    | 5.1               | As an admin, I want to monitor the system's performance.                                                                     | Admins can view system performance metrics securely. Performance logs are encrypted and accessible only to authorised users.                                                                                                                                          | As an attacker, I want to gain access to system performance data to identify vulnerabilities.                                |  
|             |                                             | 5.2               | As an admin, I want to configure alerts for system failures or breaches.                                                     | Alerts for system failures are configurable and sent to authorised users. Logs of alert configurations are stored.                                                                                                                                                    | As an attacker, I want to disable alerts to avoid detection of system breaches or failures.                                  |  
| **6**       | **Integration with Other Departments**      | 6.1               | As an admin, I want to integrate with other department systems to view patient test results.                                 | External system integrations must be secure. Data is encrypted during transit and is accessible only to authorised users.                                                                                                                                             | As an attacker, I want to inject malicious data through an different department integration to compromise the hospital data. |  
|             |                                             | 6.2               | As an admin, I want to access patient data from pharmacies securely.                                                         | Patient data from pharmacies is integrated securely and encrypted.                                                                                                                                                                                                    | As an attacker, I want to access external pharmacy data to gather sensitive patient information.                             |  
| **7**       | **Regulatory Compliance Features**          | 7.1               | As an admin, I want to ensure the app complies with GDPR regulations by managing data retention.                             | Personal data is stored according to GDPR requirements. Consent logs are maintained and accessible to authorised users.                                                                                                                                               | As an attacker, I want to erase consent records to evade compliance audits.                                                  |  
|             |                                             | 7.2               | As a user, I want to delete my personal data from the system when I no longer need the service.                              | Data deletion requests are securely processed, and logs are kept. Data is fully erased after deletion.                                                                                                                                                                | As an attacker, I want to recover deleted data to gather sensitive user information.                                         | 


With the use of these Epic User Stories, I can easily uncover the core functionalities and security requirements outlined in the brief while aligning with the business goals and objectives.
These stories will be continuously referenced throughout development to ensure that functional requirements are being met and security is efficient.


## Lab 2 - Threat Modelling
The report document can be found in the first section of the file directory for this project in the zip file.


## Lab 3 - User Authentication (Developing Login and Registration)
The objective of this sprint is to design the login and registration screen for the app, ensuring user authentication and setting foundation for accessing the app securely.

This sprint covers initial design of screens and addressing user story 1.1.

The relevant screens for the sprint have been developed and functional, along with necessary security features that are needed for GitHub at the moment.
The data validation has been complete. All fields must not be empty. Password must contain: 8+ characters, at least 1 character, number, and special character. The username the user chooses must not already exist in the database to avoid conflicts. The email must be in the correct format, at least 1 character before @, then a letter followed by ".com". The password gets entered into the database hashed with SHA-256 and other sensitive data like username and email are encrypted. The only thing left for 1.1 is logging failed attempts which will be completed in a later sprint.

### Testing
Testing will be conducted at the end of each sprint to ensure that everything is working efficiently and correctly. These will be broken down into static, dynamic and edge case testing.

#### Static Testing
After reviewing the code, I believe common practices are followed with naming conventions, functions, and database handling. The UI is going to be basic for this project as the main criteria is checking secure development and deployment over UI design and I want to ensure this is done to a high standard rather than spending loads of time on the UI.

Using Android Studio's Lint to inspect code and output any issues or warnings with the code provided no glaring errors with the project, just some preference changes with the hardcoded text.
![img.png](img.png)

#### Dynamic Testing & Edge Cases
Testing for running the app was conducted on my own Google Pixel 5.

Test cases have been developed to ensure all aspects of the app that have been added in this sprint are being tested with the expected and actual outcomes recorded to see if any changes are needed.

| **Test Case**                | **Description**                                                                                                                                                                                                              | **Expected Outcome**                                                                                                         | **Actual Outcome**                                                                                                                               |
|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| **Login and Registration**   | Tested the functionality. Entering valid credentials logs the user in and navigates to the placeholder. Invalid credentials displays a Toast message.                                                                        | The app should accept valid credentials and navigate to placeholder. If error occurs, display message.                       | The app ran as expected, with correct navigation upon successful login and appropriate error messaging for failed attempts.                      |
| **User Input Validation**    | If the fields are left empty, a toast message is shown. If the password doesn't meet the requirements mentioned in user story 1.1, an error is displayed. For registration, if the passwords do not match, display an error. | Empty fields and invalid passwords should display an error.                                                                  | App correctly display corresponding error messages for what is the issue.                                                                        |
| **Database Interaction**     | Test whether user data is correctly added to the database during registration. Data should not be added if the credentials do not meet requirements.                                                                         | User data should be saved in the database and be retrievable for login authentication. Data should not be stored if invalid. | User data is successfully saved and retrieved for login. Credentials are not stored if the input is not correct.                                 |
| **Feedback Messages**        | Test if the toast messages displayed provide significant information to the user for what the issue is.                                                                                                                      | Appropriate feedback should appear when users interact with the app, such as success and error messages.                     | Feedback messages display correctly based on user input, providing clear error messages such as declaring what exactly is missing from password. |


## Lab 4 - Patient Management
The objective of this sprint is to implement the core CRUD operations for managing patient records in the application. 

This sprint covers Epic 2 including both 2.1 and 2.2. 

Development involves screens for patient management, a list of all the patients currently available in the database, creating new users, and editing existing users. 
All data validation is functional, all required fields cannot remain empty, toasts and popups appear when significant changes are made and deletion is about to occur.

### Testing
This testing phase will go over validation, making sure the current app state is functioning properly and the database is storing correctly.

#### Static Testing
Common practices are still being followed throughout all areas of the code. The ui is still basic, just allowing for the navigation between screens and visibility of relevant data on the screen.

Android Studio Lint is displaying roughly the same warnings as before, just some preferences around coding that aren't anything concerning
![img_1.png](img_1.png)

#### Dynamic Testing & Edge Cases

| **Test Case**                                     | **Description**                                                                | **Expected Outcome**                                                     | **Actual Outcome**                                                |
|---------------------------------------------------|--------------------------------------------------------------------------------|--------------------------------------------------------------------------|-------------------------------------------------------------------|
| **Adding a Patient - Including Empty Fields**     | Test if the app prevents adding a patient with empty fields.                   | A Toast message should appear for missing fields.                        | The app displays a Toast for missing fields.                      |
| **Adding a Patient - Including Valid Data**       | Test if the app adds a patient when all fields are filled correctly.           | Patient data is added to the database and a success message is shown.    | Patient is added, success message displayed.                      |
| **View Patient List - Empty Database**            | Test if the app handles an empty patient database.                             | An empty list is displayed.                                              | An empty list is shown.                                           |
| **View Patient List - Patient Entries**           | Test if the app displays multiple patients correctly in the list.              | All patients are displayed with correct names.                           | Patient names are displayed correctly.                            |
| **Editing a Patient's Data - Empty Fields**       | Test if empty fields are prevented when editing a patient.                     | A Toast message should appear for missing fields.                        | The app shows a Toast message for empty fields.                   |
| **Editing a Patient's Data - Valid Data**         | Test if editing patient details updates the database correctly                 | Updated details are saved in the database and a success message appears. | The patient data is updated and a success message is shown.       |
| **Deleting a Patient - Confirmation**             | Test if the app asks for confirmation before deleting a patient.               | A confirmation dialog appears before deletion.                           | Confirmation dialog appears, patient deleted if confirmed.        |
| **Database Integrity - Editing a Patient's Data** | Test if editing a patient's details updates the database correctly.            | The database reflects the updated details after saving.                  | The database correctly updates after editing a patient's details. |

## Lab 5 - Doctor & Appointment Management
The objective of this sprint is to build similar CRUD functionality as the patient management module, but for doctors. 

This sprint covers Epic 3 including both 3.1 and 3.2.

Development contains designing similar screens to those developed in Lab 4 and similar functionality as well. 
Managing the appointments will be done using relational databases, with patientId and doctorId being the foreign keys to allow the numerous 1-many relationships. 
This will also be used to easily handle any scheduling conflicts by viewing all of a doctors appointments, making sure they don't overlap in time schedules, with the same being the case for patients.
Relational databases will also help make the appointment management between the two sides run in parallel and with data integrity since it's only changing 1 field since they are connected by id.

### Testing 
The testing will go over any validation that is required, analysing if the CRUD operations were implemented correctly, and the overall app is working as intended.

#### Static Testing
Common practices followed throughout coding. The ui, such as the list views, update and view effectively to output the necessary data to the user.

Lint is outputting the same results which are involving stuff like hard-coded text and similar stuff which is just preference in this scenario, but at a higher level and with more time I would sort out some of these minor issues.
![img_2.png](img_2.png)

#### Dynamic Testing & Edge Cases

| **Test Case**                                    | **Description**                                                     | **Expected Outcome**                                                     | **Actual Outcome**                                         |
|--------------------------------------------------|---------------------------------------------------------------------|--------------------------------------------------------------------------|------------------------------------------------------------|
| **Adding a Doctor - Including Empty Fields**     | Test if the app prevents adding a doctor with empty fields.         | A Toast message should appear for missing fields.                        | The app displays a Toast for missing fields.               |
| **Adding a Patient - Including Valid Data**      | Test if the app adds a doctor when all fields are filled correctly. | Doctor data is added to the database and a success message is shown.     | Doctor is added, success message displayed.                |
| **View Doctor List - Empty Database**            | Test if the app handles an empty doctor database.                   | An empty list is displayed.                                              | An empty list is shown.                                    |
| **View Doctor List - Doctor Entries**            | Test if the app displays multiple doctors correctly in the list.    | All doctors are displayed with correct names.                            | Doctor names are displayed correctly.                      |
| **Editing a Doctor's Data - Empty Fields**       | Test if empty fields are prevented when editing a doctor.           | A Toast message should appear for missing fields.                        | The app shows a Toast message for empty fields.            |
| **Editing a Doctor's Data - Valid Data**         | Test if editing doctor details updates the database correctly       | Updated details are saved in the database and a success message appears. | The doctor data is updated and a success message is shown. |
| **Deleting a Doctor - Confirmation**             | Test if the app asks for confirmation before deleting a doctor.     | A confirmation dialog appears before deletion.                           | Confirmation dialog appears, doctor deleted if confirmed.  |

## Conclusion & Review
Due to time constraints and errors I ran into during development, most of the epics and user stories I couldn't cover. However, most of the features from the epics were additional criteria indentified through the initial gathering of requirements. Most of the stuff ask in all briefs were met, with a few things missing from the final step such as the appointment management, and while ideas were made on how to implement unfortunately did not have enough time. Most of the Labs were completed, 1 - 4 with 5 being half done, just needing to work on the appointment side of things.

Despite the missing criteria, I believe that all criteria that was met, especially that asked in the brief were done effectively and securely. The password is efficiently hashed and other valuable user data is encrypted to prevent gathering the data by just gaining access to the database. All validation across the app works as intended, letting the user know when something has or hasn't gone through with the use of toasts. The correct data is displayed to the user, decrypted from the database. All CRUD operations are present in the app, being able to create both patients and doctors, read of the relevant data from both the list view and edit menus, updating it via the edit menu and deleting data when certain items are held down in the list. 

Continuing this project in the future, I would begin with completing the appointment section since I have a solution on how to develop it. Then would be finalising areas of the app, such as updating the UI, and ensuring that signatures and other admin tools can be easily accessed via the dashboard.