# Service Proxy Provider example

This example is composed of 2 parts, a Vert.x part (the server) and 2 clients a web client and a nodejs client.

To run it first start the vert.x component and then either interact with a [browser](http://localhost:8080) or
run the node client:

> node index.js

From the web client you can:

* change the document name using: http://localhost:8080?document_name=my-name
* introduce a reply error using: http://localhost:8080?document_name=bad
