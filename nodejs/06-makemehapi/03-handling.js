var Hapi = require("hapi");
var Path = require("path");

var server = new Hapi.Server();
var portn = Number(process.argv[2]);

server.connection({
   host: 'localhost',
   port: Number(portn || 8080)
});

server.route({
   path: '/',
   method:'GET',
   handler: {
     file: Path.join("03-handling", "index.html")
   }
});

server.start();
