var http = require('http');
var fs = require('fs');

if (process.argv.length < 3) {
   return console.error("Specify port");
}

if (process.argv.length < 4) {
   return console.error("Specify file path");
}

portn = Number(process.argv[2]);
filepath = process.argv[3];

httplistener = function(request, response) {
   fs.createReadStream(filepath, {encoding: "utf8"}).pipe(response);
}

try {
   var server = http.createServer(httplistener);
   server.listen(portn);
} catch (err) {
   console.error(err);
}