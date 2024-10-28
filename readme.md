how to build image:

docker build -t megogo .

how to run server:

docker run -p 8080:8080 -e RATE_LIMIT=2 megogo

swagger docs are available at:

http://localhost:8080/docs/
