# Page Analytics

**Page Analytics** it is microservice, which deliver information about page.
 All statistics are collected from google analytics and they are fetch for current date, every 15 minutes.  

###### For certain page path
* *views* - number of visits.
* *uniqueViews* - number of unique views.
* *avgTimeOnPage* - avrage time on page in seconds.
* *entraces* - number of entraces/landing on page.
* *exits* - number of exits from page.
* *date* - date of statistics are for.

## Usage

Send **GET** request to retrieve statistics for all pages.

    /page-analytis 
    

Send **GET** request with **path** and **date** params to  retrieve statistics for certain page and date.

    page-analytics?path=/extra-time/history-of-volleyball/&date=2016-01-14
    

#### Example

    GET http://{url}/page-analytics
   
##### Result
 
    [
        {
            path: "/extra-time/history-of-volleyball/",
            views: 642,
            uniqueViews: 531,
            avgTimeOnPage: 39.4,
            entrances: 233,
            exits: 211,
            date: "2015-01-14"
        },
        {
            path: "/extra-time/footbal-highligts/",
            views: 1534,
            uniqueViews: 945,
            avgTimeOnPage: 34.43,
            entrances: 548,
            exits: 859,
            date: "2015-01-14"
        },
        {
            path: "/extra-time/phone-features/",
            views: 156,
            uniqueViews: 90,
            avgTimeOnPage: 61,
            entrances: 42,
            exits: 67,
             date: "2015-01-14"
        }
    ]

## Configuration

To fetch data from google analytics you need to setup google analytics account https://developers.google.com/analytics/devguides/reporting/core/v3/.

After that you need to provide your account settings in **application.conf**.


```
ga.p12-key-file-location= "<p12-key-file-location>"
ga.service-account-email = "<service-account-email>"
ga.profile-id=  "<profile-id>"
ga.query-filters=  "<query-filters>"
```

## Tech
### Page Analytics use:
* Scala.
* Play framework.
* Akka.
* Slick.
* Guice.
