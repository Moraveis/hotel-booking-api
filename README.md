# Hotel Booking API

## Task Description

You should be able to start the example application by executing com.alten.altentest.AltenTestApplication.java, which starts a webserver on port 8080 (http://localhost:8080) and serves SwaggerUI(http://localhost:8080/swagger-ui.html) where can inspect and try existing endpoints.

The project is based on a small web service which uses the following technologies:

* Java 1.8
* Lombok
* Spring Boot 2.x (2.3.4.RELEASE)
* Database H2 (In-Memory)
* Gradle 6.x

### Requirements

Scenario:
People are now free to travel everywhere but because of the pandemic, a lot of hotels went bankrupt. Some former famous travel places are left with only one hotel.
You’ve been given the responsibility to develop a booking API for the very last hotel in Cancun.

* API will be maintained by the hotel’s IT department.
* As it’s the very last hotel, the quality of service must be 99.99 to 100% => no downtime
* For the purpose of the test, we assume the hotel has only one room available
* To give a chance to everyone to book the room, the stay can’t be longer than 3 days and can’t be reserved more than 30 days in advance.
* All reservations start at least the next day of booking,
* To simplify the use case, a “DAY’ in the hotel room starts from 00:00 to 23:59:59.
* Every end-user can check the room availability, place a reservation, cancel it or modify it.
* To simplify the API is insecure.
