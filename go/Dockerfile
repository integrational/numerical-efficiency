FROM golang:latest AS builder
# show compiler version
RUN go version
# compile and package app
WORKDIR /app
COPY squareroot .
RUN go mod tidy
RUN go build -o app

FROM scratch
WORKDIR /app
COPY --from=builder /app/app .
CMD ["./app"]
