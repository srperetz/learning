var Hapi = require("hapi");

var server = new Hapi.Server();
var portn = Number(process.argv[2]);

server.connection({
   host: 'localhost',
   port: Number(portn || 8080)
});

server.state('session', {
   ttl: 10,
   encoding: 'base64json',
   domain: 'localhost',
   path: '/{path*}'
})

server.route([{
   path: '/set-cookie',
   method: 'GET',
   config: {
      state: {
         parse: true,
         failAction: 'log'
      }
   },
   handler: function (request, reply) {
      reply('success').state('session', {key: 'makemehapi'})
   }
},
{
   path: '/check-cookie',
   method: 'GET',
   handler: function (request, reply) {
      var response;
      if (request.state.session) {
         response = {user : 'hapi'}
      } else {
         response = Boom.unauthorized("Invalid cookie value")
      }
      reply(response)
   }
}]);

server.start();
