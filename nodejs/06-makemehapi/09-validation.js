var Hapi = require("hapi");
var Joi = require("joi");

var server = new Hapi.Server();
var portn = Number(process.argv[2]);

server.connection({
   host: 'localhost',
   port: Number(portn || 8080)
});

server.route({
   path: '/chickens/{breed}',
   method: 'GET',
   handler: function(request, reply) {
      reply(request.params.breed)
   },
   config: {
      validate: {
         params: {
            breed: Joi.string().required()
         }
      }
   }
});

server.start();
