var Hapi = require("hapi");
var Joi = require("joi");

var server = new Hapi.Server();
var portn = Number(process.argv[2]);

server.connection({
   host: 'localhost',
   port: Number(portn || 8080)
});

server.route({
   path: '/login',
   method: 'POST',
   handler: function(request, reply) {
      reply("login successful" )
   },
   config: {
      validate: {
         payload: Joi.object({
            isGuest: Joi.boolean(),
            username: Joi.string().when('isGuest', { is: false, then: Joi.required() }),
            accessToken: Joi.string().alphanum(),
            password: Joi.string().alphanum()
         })
         .options({allowUnknown: true})
         .without('password', 'accessToken')
      }
   }
});

server.start();
