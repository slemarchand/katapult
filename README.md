# Katapult
Import users from CSV file into Liferay portal

[![Build Status](https://travis-ci.org/slemarchand/katapult.svg?branch=master)](https://travis-ci.org/slemarchand/katapult)

## Installation

Download the JAR here: https://github.com/slemarchand/katapult/releases/download/0.0.1-SNAPSHOT/katapult-0.0.1-SNAPSHOT.jar.

## Usage
```
Usage: katapult [options] <csv-file>
  Options:
  * -s, --server
      The server base URL
  * -c, --companyId
      The company ID
  * -u, --user
      The user
  * -p, --password
      The password
    -k, --insecure
      (TLS) By default, every SSL connection katapult makes is verified to be 
      secure. This option allows katapult to proceed and operate even for 
      server connections otherwise considered insecure
      Default: false
```
