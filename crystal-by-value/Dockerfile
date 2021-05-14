FROM crystallang/crystal:latest-alpine AS builder
# show compiler version
RUN crystal version
# compile and package app
WORKDIR /app
COPY . .
RUN crystal build -o app *.cr --release --static

FROM scratch
WORKDIR /app
COPY --from=builder /app/app .
CMD ["./app"]
