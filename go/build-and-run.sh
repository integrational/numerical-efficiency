img="square-root-"$(basename $PWD)

echo Building
docker build --pull --progress plain -t $img . 2>&1 | tee output_build

echo Running
docker run -it --rm --name $img $img           2>&1 | tee output_run
