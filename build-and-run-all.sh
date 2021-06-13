#!/bin/bash

for d in */ ; do
    echo "$d:"
    pushd "$d" > /dev/null
    ./build-and-run.sh --no-cache
    popd > /dev/null
done

./combine-execution-times.sh
