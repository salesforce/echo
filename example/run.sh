#!/bin/bash

echo "posting..."
ID=$(curl -H 'Host: 127.0.0.1:8786' -X POST 'http://127.0.0.1/' --data "$(uuidgen)")
echo "get..."
curl -H 'Host: 127.0.0.1:8786' -X GET "http://127.0.0.1/$ID"

