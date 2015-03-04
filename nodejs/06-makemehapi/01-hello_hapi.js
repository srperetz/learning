var Hapi = require("hapi");

var server = new Hapi.Server();
var portn = Number(process.argv[2]);

server.connection({
   host: 'localhost',
   port: Number(portn || 8080)
});

server.route({
   path: '/',
   method:'GET',
   handler: function(request, reply) {
      reply("Hello Hapi");
   }
});

server.start();
