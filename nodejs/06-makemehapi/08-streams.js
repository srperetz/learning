var Hapi = require("hapi");
var fs = require("fs");
var Path = require("path");
var rot13 = require("rot13-transform");

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
      reply(fs.createReadStream(Path.join("08-streams", "input.txt"), { encoding: "utf8" }).pipe(rot13()));
   }
});

server.start();
