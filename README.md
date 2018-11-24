# Offer service REST API

## What is it ?
An offer service REST API developed in java the project is built as [Maven](https://maven.apache.org/) project, using 
[SpringBoot](https://spring.io/projects/spring-boot) and allows a user to :

* Create/Open an offer 
* Get a list of the offers currently open
* Cancel an offer

An offer is a proposal to sell a specific product or
service under specific conditions. As a merchant I offer goods for
sale. I want to create an offer so that I can share it with my
customers.
All my offers have shopper friendly descriptions. I price all my offers
up front in a defined currency.
An offer is time-bounded, with the length of time and is valid for
defined as part of the offer and should expire automatically. Offers may
also be explicitly cancelled before they expire.

## Running
ApplicationMain is the service's entry point, if you ran its main method with your IDE, the server should start on 
``http://localhost:8080``.

The service can also be built by running the ```mvn install``` command in the root repository, it will generate a standalone artifact suitable
 for running independently. When executed, it will start in the 
 same way as above.

## API Endpoints 

### URL: `/offers`

* **Method:** `GET` 
  
* **Success Response:**

    **Code:** 200 <br />
    **Content:** JSON representation of the offers <br />
    ```JSON
    { 
      "id": 1,
      "currentDate": "2018-11-21T20:47:40.603Z",
      "description": "A cheap offer",
      "expirationDate": "2018-11-22T20:47:40.603Z",
      "price": 10.5,
      "status": "OPEN"
    },
    {
      "id": 2,
      "currentDate": "2018-11-21T20:47:40.603Z",
      "description": "An expensive offer",
      "expirationDate": "2018-11-22T20:47:40.603Z",
      "price": 1589.58,
      "status": "OPEN"
    },
    {
      "id": 3,
      "currentDate": "2018-11-21T20:47:40.604Z",
      "description": "A closed offer",
      "expirationDate": "2018-11-20T20:47:40.604Z",          
      "price": 185.58,
      "status": "CLOSE"  
    }
    ```
* **Sample Call:**

```bash 
curl 'http://localhost:8080/offers' -H 'Accept: application/JSON' 
```

### URL: `/offers/{id}`

* **Method:** `GET` 
  
* **URL Params**

    **Required:**
 
   `id=[integer]`
  
* **Success Response:**

  * **Code:** 200 <br />
    **Content:** JSON representation of the specific offer <br />
    ```JSON
    { 
      "id": 1,
      "currentDate": "2018-11-21T20:47:40.603Z",
      "description": "A cheap offer",
      "expirationDate": "2018-11-22T20:47:40.603Z",
      "price": 10.5,
      "status": "OPEN"
    }
    ```
    
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** 
    ```JSON
    {
        "timestamp": "2018-11-22T13:12:17.966Z",
        "message": "OfferNotFoundException{offerId=1}",
        "details": "uri=/offers/1"
    }
    ```
   
* **Sample Call:**

```bash 
curl 'http://localhost:8080/offers/1' -H 'Accept: application/JSON' 
```
### URL: `/offers`

* **Method:** `POST` 
  
* **Data Params:** A JSON representation of the object to insert like below
```JSON
    { 
      "description": "A cheap offer",
      "expirationDate": "2018-11-22T20:47:40.603Z",
      "price": 10.5,
      "status": "OPEN"
    }
```
    
* **Success Response:**
  
  * **Code:** 201 <br />
  ```
  Location: http://localhost:8080/offers/1
  Content-Length: 0
  Date: Wed, 21 Nov 2018 22:36:24 GMT 
  ```
 
* **Error Response:**

 * **Code:** 400 BAD REQUEST <br />
      **Content:** 
      ```JSON
      {
          "timestamp": "2018-11-22T23:55:43.354Z",
          "message": "OfferBadFormatException{offerId=null} - Expiration date must be after current date",
          "details": "uri=/offers/"
      }
      ```
      
  * **Code:** 400 BAD REQUEST <br />
      **Content:** 
      ```JSON
      {
          "timestamp": "2018-11-21T22:32:06.692Z",
          "message": "OfferBadFormatException{offerId=null} - Expiration date must be provided for offer",
          "details": "uri=/offers"
      }
      ```  
   * **Code:** 400 BAD REQUEST <br />
        **Content:** 
        ```JSON
        {
            "timestamp": "2018-11-21T22:32:06.692Z",
            "message": "OfferBadFormatException{offerId=null} - Price must be provided for offer",
            "details": "uri=/offers"
        }
        ```    
   
   * **Code:** 400 BAD REQUEST <br />
        **Content:** 
        ```JSON
        {
            "timestamp": "2018-11-23T00:10:31.544Z",
            "message": "OfferBadFormatException{offerId=null} - Status of the offer must be OPEN",
            "details": "uri=/offers/"
        }
        ```    
      
* **Sample Call:**
```bash
curl -X POST 'http://localhost:8080/offers' -H 'content-type: application/json' --data $'{\n        "description": "A cheap offer",\n        "expirationDate": "2018-11-22T20:47:40.603Z",\n        "price": 10.5,\n        "status": "OPEN"\n}'
```

### URL: `/offers/{id}`

* **Method:** `PATCH` 

* **URL Params**

    **Required:**
 
   `id=[integer]`
  
  
* **Data Params:** A representation of the status to update like below
```JSON
    "CLOSE"
```
    
* **Success Response:**
  
  * **Code:** 201 <br />
  ```
  Location: http://localhost:8080/offers/1
  Content-Length: 0
  Date: Wed, 21 Nov 2018 22:36:24 GMT 
  ```
 
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** 
    ```JSON
    {
        "timestamp": "2018-11-21T22:54:45.377Z",
        "message": "OfferPatchException{offerId=1} - You can't cancel a non existing offer",
        "details": "uri=/offers/1"
    }
    ```

   * **Code:** 400 BAD REQUEST <br />
        **Content:** 
        ```JSON
        {
            "timestamp": "2018-11-21T22:32:06.692Z",
            "message": "OfferBadFormatException{offerId=1} - You can't cancel an offer already closed",
            "details": "uri=/offers/1"
        }
        ```    
   * **Code:** 400 BAD REQUEST <br />
       **Content:** 
       ```JSON
       {
             "timestamp": "2018-11-21T22:32:06.692Z",
             "message": "OfferBadFormatException{offerId=1} -  You can only cancel an existing offer",
             "details": "uri=/offers/1"
       }
       ```    
     
* **Sample Call:**
```bash
curl -X PATCH 'http://localhost:8080/offers/1' -H 'content-type: application/json' --data $' "CLOSE"'
```
### Unit Tests
Unit tests are under ```test/com/rest/offerservice``` folder on your IDE