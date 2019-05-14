#!/bin/bash

# Delete release key
rm -f signing/app-release.jks

# Delete signing properties
rm -f signing/signing.properties

# Delete Play Store service account
rm -f signing/play-account.json
