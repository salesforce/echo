#!/bin/bash

cp -r ../conf echo_0.1-1/opt/echo/
dpkg-deb --build echo_*
