var Hapi = require("hapi");

var server = new Hapi.Server();
var portn = Number(process.argv[2]);

server.connection({
   host: 'localhost',
   port: Number(portn || 8080)
});

server.route({
   path: '/upload',
   method: 'POST',
   config: {
      payload: {
         output: 'stream',
         parse: true,
         allow: 'multipart/form-data'
      }
   },
   handler: function (request, reply) {
      var body = '';
      request.payload.file.on('data', function (data){
         body += data
      })
      request.payload.file.on('end', function (){
         reply(JSON.stringify({
           description:  request.payload.description,
           file: {
             data: body,
             filename: request.payload.file.hapi.filename,
             headers: request.payload.file.hapi.headers
           }
         }))
      })
   }
});

server.start();
