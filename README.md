# Katapult
Import users from CSV file into Liferay portal

[![Build Status](https://travis-ci.org/slemarchand/katapult.svg?branch=master)](https://travis-ci.org/slemarchand/katapult)
[![codecov](https://codecov.io/gh/slemarchand/katapult/branch/master/graph/badge.svg)](https://codecov.io/gh/slemarchand/katapult)

## Installation

1. Download the binary distribution here: https://github.com/slemarchand/katapult/releases
2. Extract the archive where you want to install Katapult
3. Add the new created directory to your executable path

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
