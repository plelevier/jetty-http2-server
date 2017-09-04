jetty-http2-server
==================

Embedded Jetty server with HTTP and HTTP/2, with the following resources :
- A catch all resource `*` that handle (almost) all the `GET` and `POST requests`;
- A Server Sent Event (SSE) resource `/sse/watch` that allows streaming of the catch all logs as SSE events

How to Run
----------

```bash
mvn package
```

In order to run the examples, you need
[Jetty's ALPN boot JAR](http://unrestful.io/2015/10/09/alpn-java.html).

Start the server:

```bash
java -Xbootclasspath/p:<path-to-alpn-boot-VERSION.jar> -jar target/jetty-http2-server.jar
```

Test using Web Browser
----------------------

Point your browser to [https://localhost:8443](https://localhost:8443)


Watch logs using a SSE client
-----------------------------

Run the command below. When you'll make request on the server, you should see the events occur

```bash
curl -v -X GET http://localhost:8080/sse/watch -H "Content-Type: text/event-stream"
```


Watch logs using the UI
-----------------------

Point your browser to [https://localhost:8080/watch](https://localhost:8080/watch). When you'll make request on the server, you should see the events occur


Test using `h2c`
----------------

Download and install the latest `h2c` release from [github.com/fstab/h2c](https://github.com/fstab/h2c/releases).

```bash
h2c get https://localhost:8443
```
