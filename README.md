
<a name="title"></a>
# iRate: A Database for Managing Movie Ratings  

iRate is a social media application that encourages theater customers to rate a movie that they saw at the theater in the past week and write a short review.
This project is a portion of a an application that enables registered movie theater customers to rate a movie that they saw at the theater, and for other registered customers to vote for reviews.

# Table of contents
* [Title](#title)
* [Getting Started](#gettingStarted)
* [Prerequisites](#prerequisites)
* [Schema Diagram for the Database](#schema)
* [Tables and description](#tables)
  * [Customer](#customer)
  * [Movie](#movie)
  * [Attendance](#attendance)
  * [Review](#review)
  * [Endorsement](#endorsement)
* [Examples of commands you can run on for this Database.](#examples)
  * [DDL](#ddl)
  * [DML](#dml)
  * [DQL](#dql)
* [Program Design](#programDesign)
* [Testing strategy](#testingStrategy)
  * [Successfull tests](#successfulTest)
  * [Unsuccessful tests](#unsuccessfulTest)
* [Running Tests](#runningTests)
* [Future Scope](#future)
* [Authors](#authors)

<a name="gettingStarted"></a>
## Getting Started 

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

<a name="prerequisites"></a>
### Prerequisites 

* Derby
* Java 8
* SQL

<a name="schema"></a>
## Schema Diagram for the Database 
![GitHub Logo](https://github.ccs.neu.edu/2018FACS5200SV/project-1-sreerajathav/blob/master/SchemaDiagram.png)

<a name="tables"></a>
## Tables and description 
<a name="customer"></a>
* **Customer** is a registered customer of the theater. 
The Customer information includes a customer name, email address, the date the customer joined, and a gensym customer ID. The information is entered by the theater when the customer registers. If a customer is deleted, all of his or her reviews and endorsements are deleted 

<a name="movie"></a>
* **Movie** is a record of a movie playing at the theater. 
It includes a title, and a gensym movie ID. This information is entered by the theater for each movie it plays.

<a name="attendance"></a>
* **Attendance** is a record of a movie seen by a customer on a given date. 
It includes a movie ID, the attendance date, and the customer ID. This information is entered when the customer purchases a ticket for a show. If a movie is deleted, all of its attendances are deleted. Attendance info is used to verify attendance when creating a review.

<a name="review"></a>
* **Review** is a review of a particular movie attended by a custumer within the last week. 
The review includes the customer ID, the movie ID, the review date, a rating (0-5 stars), a short (1000 characters or less) review, and a gensym review ID. There can only be one movie review per customer, and the date of the review must be within 7 days of the most recent attendance of the movie. If a movie is deleted, all of its reviews are also deleted.

<a name="endorsement"></a>
* **Endorsement** is an endoresement of a movie review by a customer. 
A customer's current endorsement of a review for a movie must be at least one day after the customer's endorsement of a review for the same movie. The endorsement includes the review ID, the customerID of the endorser, and the endoresemnt date. A customer cannot endorse his or her own review. If a review is deleted, all endorsements are also deleted.

<a name="examples"></a>
## Examples of commands you can run on for this Database.
<a name="ddl"></a>
### DDL for creating the tables 
```
create table Customer ( CustomerID varchar(64) not null ,
 first_Name varchar(32) not null,
 last_Name varchar(32),
 email varchar(64),
 date TIMESTAMP,
 primary key (CustomerID),
  check (isEmail(email))
);
 ```
 <a name="dml"></a>
### DML for editing entries in the tables 
```
insert into Customer values( generateUuid(),
'Siddhant',
'V',
'abc@gmail.com',
'1962-09-23 03:23:34.234');
 ```
 <a name="dql"></a>
### DQL for making commonly used queries to retrieve information about the status of reviews and votes from the database. 
```
select * from Customer;
 ```
 <a name="programDesign"></a>
 ## Program Design

* **Project.java:** This file contains all the logic to create and connect to the database, drop and create all the tables and functions for the application.

* **MovieTheatre.java:** This file contains all the stored functions that have to be added to the database in order to populate the tables with data that comply with all the checks.

* **Test.java:** This file contains all the logic to insert data successfully into the tables as well as scenarios where data insertion fails due to constraint or business logic violations.

* **Display.java:** This file displays all the data stored in the database.
 
 <a name="testingStrategy"></a>
## Testing strategy 

<a name="successfulTest"></a>
### Successful insert tests 
 * We first create a set of dummy data which we use to populate the Movie and Customer Table.
 * The dummy data is stored in arrays.
 * Using the data stored in Movie and Customer we populate the data in Attendance, Review and Endorsement table.
 * The order of insertion is Customer,Movie -----> Attendence ----> Review ------> Endorsement.
 * The above order is required because a customer can write a review only after a movie is attended and the review can be endorsed only after the review is written.
 
 <a name="unsuccessfulTest"></a>
 ### Unsuccessful insert tests 
 * We have an exhaustive list of test for inserting data which violates constraints and a few of them are as follows,
   - Test to check if insertion fails if you endorse a 2nd review before 24 hours of endorsing a first review of the same.
   - Test to check if insert fails when same customer writes review for the same movie again.
   - Test to check for review without attendance.
   - Test to check if insertion with invalid email ID fails, etc.
 
<a name="runningTests"></a>
## Running the tests 
* Run Project.java and it will create all the tables.
* Run Test.java and it will populate all the tables with data.
* Run Display.java and it will display all the tables with data.

<a name="future"></a>
## Future Scope
* In the future we can add more funcionality and business logic which can help us utilize the data store in the database. We can add a feature where other costumers can vote one review of a particular movie as "helpful" each day. The writer of the top rated review of a movie written three days earlier receives a free movie ticket, and voting is closed for all reviews of the movie written three days ago. Someone who voted one or more movie reviews as "helpful" on a given day will be chosen to receive a free concession item.

* This project can provide a back-end that could be used for a future projects to create web-based front-end to the database that allows a movie theater to operate this promotional application. It can also be used as part of a back end to a mobile application for the theater.

<a name="authors"></a>
## Authors 

* **Siddhant Varyambat** - *Intial work* - [vpsiddhant](https://github.com/vpsiddhant)
* **Sreerajatha** - *Initial work* - [sreerajathav](https://github.ccs.neu.edu/sreerajathav)
