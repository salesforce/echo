#!/bin/sh

set -e

PATH_OPENRESTY=src/openresty-1.11.2.1/

pushd .
echo "*** running configure $@..."
cd ${PATH_OPENRESTY}
./configure $@

echo "*** running make..."
make

echo "*** running make install..."
make install

popd 

echo "*** done."

