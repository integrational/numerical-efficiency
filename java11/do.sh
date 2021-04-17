img="square-root-"$(basename $PWD)
docker build -t $img .
docker run -it --rm --name $img $img | tee output
