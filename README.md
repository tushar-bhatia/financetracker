# Finance Tracker 💰

This project is a lightweight full-stack web application designed to help users track their income and expenses. It's composed of a **Spring Boot backend** and an **Angular frontend**. The application allows users to manage transactions, categorize them, and view a monthly financial summary.

## Backend

The backend is a **Spring Boot** application built with **JDK 21**. [cite\_start]It uses an in-memory or embedded database, so you don't need to set up an external database[cite: 20]. The backend provides a series of RESTful API endpoints for managing financial transactions.

### API Endpoints

The core API endpoints for this application are:

#### Transaction APIs (`/financetracker/transaction/api/v1`)

  * **`GET /getAll`**: Retrieves all transactions with optional filtering.
  * **`GET /get/{id}`**: Fetches a specific transaction by its ID.
  * **`POST /add`**: Creates a new transaction.
  * **`PUT /edit`**: Updates an existing transaction.
  * **`DELETE /delete/{id}`**: Deletes a transaction by its ID.
  * **`POST /summary`**: Generates a monthly summary, including total income, total expenses, and balance.
  * **`POST /download`**: Downloads an Excel report of the monthly summary.

#### Category APIs (`/financetracker/category/api/v1`)

  * **`GET /getAll`**: Retrieves all available categories.
  * **`GET /get/{id}`**: Fetches a specific category by its ID.
  * **`POST /add`**: Creates a new category.
  * **`PUT /edit`**: Updates an existing category.
  * **`DELETE /delete/{id}`**: Deletes a category by its ID.

-----

## Frontend

The frontend is built using **Angular**, with Node version 22.18.0 and npm version 10.9.3. [cite\_start]It provides a clean and usable dashboard for managing and viewing transactions[cite: 27, 32].

### Key Features

  * [cite\_start]**Transaction Management**: Add, edit, and delete transactions[cite: 26].
  * [cite\_start]**Dashboard**: A dashboard for any selected month showing total income, total expenses, and net balance[cite: 27, 28, 29, 30].
  * [cite\_start]**Category Breakdown**: A simple category breakdown using a bar chart or table[cite: 31].
  * [cite\_start]**CSV Export**: Ability to export transactions to a CSV file (optional)[cite: 35].

-----

## How to Run the Project Locally

Follow these steps to set up and run the full-stack application on your local machine.

### Prerequisites

  * **Backend**: Java Development Kit (JDK) 21 and Maven.
  * **Frontend**: Node.js v22.18.0 and npm v10.9.3.

### Step 1: Run the Backend

1.  Open a terminal and navigate to the `backend` directory.
2.  Run the following command to start the Spring Boot application:
    ```bash
    ./mvnw spring-boot:run
    ```
3.  Wait for the backend to start up successfully.

### Step 2: Run the Frontend

1.  Open a new terminal and navigate to the `frontend` directory.
2.  Install the project dependencies if you haven't already:
    ```bash
    npm install
    ```
3.  Start the Angular development server:
    ```bash
    npm start
    ```
4.  The frontend will be accessible in your browser at `http://localhost:4200`.

### Visual Studio Code Launch Configurations

This project includes launch configurations for Visual Studio Code. You can run and debug both the backend and frontend directly from the IDE. Simply open the **Run and Debug** view, select the desired configuration, and click the play button.

This README file provides a clear overview and instructions to help someone else understand and run your project.