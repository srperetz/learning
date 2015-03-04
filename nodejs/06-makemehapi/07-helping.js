var Hapi = require("hapi");
var Path = require("path");

var server = new Hapi.Server();
var portn = Number(process.argv[2]);

server.connection({
   host: 'localhost',
   port: Number(portn || 8080)
});

server.views({
   engines: {
      html: require('handlebars')
   },
   path: Path.join("helpers", "templates"),
   helpersPath: Path.join("helpers", "js")
});

server.route({
   path: '/',
   method:'GET',
   handler: {
     view: "template.html"
   }
});

server.start();
