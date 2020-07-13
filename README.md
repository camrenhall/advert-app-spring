# advert-app-spring

**advert-app-spring** is a *very* rudimentary foundation for an advertising application using the Java Spring framework.

Without going into too much detail, the idea is that the application, whether accessed on mobile or web, serves as a brokerage between Businesses (generally small-businesses with limited advertising campaign money) who want to advertise their businesses and the User.

Users are able to "sign up" to promote campaigns that they are personally interested in, with the idea that advertising temporarily similar to a "brand-ambassador" promotes real-person advertising and the power of recommendation over conventional advertisements.

The codebase has two branches. The master branch has security issues interwoven into the foundations. The following security issues are present (these are described further and where to find them in security-issues.md):

* SQL Injection
* XSS Injection
* Simple CSRF disable
* Sending of sensitive user information through HTML GET methods

## Installation

As of right now, this build functions by running in the cloud through travis-ci. 

## Usage

When deployed on travis-ci, it will run 

Running the tests will fail in the security-issues branch, meaning that the security issues exist. 

Running the tests in the master branch will not fail, meaning that the security issues have been fixed.


## License
All rights reserved to Camren Hall. The only people authorized to access this project are those invited to collaborate with explicitly on Github.