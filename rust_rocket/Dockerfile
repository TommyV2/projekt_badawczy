FROM rust:1.68.1

ENV ROCKET_ADDRESS=0.0.0.0
ENV ROCKET_PORT=6666

ENV POSTGRES_HOST=db
ENV POSTGRES_PORT=5432
ENV POSTGRES_DB=exampledb
ENV POSTGRES_USER=docker
ENV POSTGRES_PASSWORD=docker

ENV DATABASE_URL=postgres://docker:docker@db:5432/exampledb

ENV ROCKET_DATABASES='{exampledb={url="postgres://docker:docker@db:5432/exampledb"}}'


WORKDIR /app
COPY . .

RUN rustup default nightly
RUN cargo build

CMD ["cargo", "run"]