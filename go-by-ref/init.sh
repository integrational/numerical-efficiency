docker pull golang:latest
docker run -i --rm -v $(pwd):/proj -w /proj golang <<EOF
go version
mkdir squareroot
cd squareroot/
go mod init squareroot
touch squareroot.go
EOF
