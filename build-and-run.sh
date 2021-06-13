#!/bin/bash

# to be executed from within the implementation directory for each programming language

buildargs="$*" # all command-line args (like --no-cache) are passed to docker build

img="square-root-"$(basename $PWD)

echo Building
docker build --pull --progress plain $buildargs -t $img . 2>&1 | tee output_build

echo Running
docker run -it --rm --name $img $img                      2>&1 | tee output_run
