#!/bin/bash

for d in */ ; do
    echo "$d:"
    pushd "$d" > /dev/null
    ./build-and-run.sh
    popd > /dev/null
done
