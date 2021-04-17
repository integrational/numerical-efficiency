FROM golang:latest AS builder
# compile and package app
WORKDIR /app
COPY squareroot .
RUN go mod tidy
RUN go build -o app

FROM debian:buster-slim
WORKDIR /app
COPY --from=builder /app/app .
CMD ["./app"]
