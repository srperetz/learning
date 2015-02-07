var http = require('http');
var map = require('through2-map');

if (process.argv.length < 3) {
   return console.error("Specify port");
}

portn = Number(process.argv[2]);

uppercaserer = map({wantStrings: true}, function(chunk) {
   return chunk.toUpperCase();
});

httplistener = function(request, response) {
   if (request.method == "POST") {
      request.pipe(uppercaserer).pipe(response);
   }
}

try {
   var server = http.createServer(httplistener);
   server.listen(portn);
} catch (err) {
   console.error(err);
}