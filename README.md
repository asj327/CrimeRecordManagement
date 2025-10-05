# Initial Setup

This branch contains the initial setup for collaborating or for user distribution

It does the following
<ul>
  <li>Creates a database required for the application to work if not already present</li>
  <li>Securely connects to MySQL in the local device using username and password</li>
  <li>Can create a admin user in the database for testing</li>
  <li>Uses Maven for management, thereby avoiding manual library downloads, which are necessary for proper function</li>
</ul>

NOTE : this does reqiure a working MySQL in the local device to function

# Login Backend

The src/main/java contains code files for all of the above functions as well as Login backend

The libraries used are jbcrypt.java for secure password storage in the database and user authentication and mysql-connector-java for java connection to the database.

This uses a simple UI for testing simplicty which must be changed later to connect to the actual frontend.
The User Authenticator checks the database for the entered credentials .
It uses the hashed password for checking hence making it secure.


## Further Updates Required
<ul>
  <li>Update the database records table for initial setup</li>
  <li>Update Main after connection and testing</li>
  <li>Connect to frontend</li>
  <li>Ensure logout page works by correctly updating active property in the database</li>
</ul>


# Database 
<img width="353" height="240" alt="Screenshot 2025-10-05 140504" src="https://github.com/user-attachments/assets/076809cb-2f7d-4b6d-87d7-0a01afb9775b" />

<img width="1067" height="408" alt="Screenshot 2025-10-05 140337" src="https://github.com/user-attachments/assets/d03f92b8-e4ae-4adf-8770-50429ee7b93f" />

<img width="1596" height="214" alt="Screenshot 2025-10-05 140400" src="https://github.com/user-attachments/assets/0ffd8603-bc9d-4053-8871-d3f6c6ca4e98" />

