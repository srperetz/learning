var Hapi = require("hapi");

var server = new Hapi.Server();
var portn = Number(process.argv[2]);

server.connection({
   host: 'localhost',
   port: Number(portn || 8080)
});

server.route({
   path: '/foo/bar/baz/{filename}',
   method:'GET',
   handler: {
     directory: {
        path: "04-directories"
     }
   }
});

server.start();
