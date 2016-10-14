#!/bin/bash

rm -rf echo_0.1-1/opt/echo/conf
rm -rf echo_0.1-1/opt/echo/lib/lualib

cp -r ../conf echo_0.1-1/opt/echo/conf
cp -r ../lua/lib echo_0.1-1/opt/echo/lib/lualib

dpkg-deb --build echo_*
