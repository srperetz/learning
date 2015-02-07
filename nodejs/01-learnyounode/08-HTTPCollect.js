var http = require('http');
var bufferList = require('bl');

if (process.argv.length < 3) {
   return console.err("Specify URL");
}

httpGetFunc = function(response) {
   response.on("error", processHttpGetError);
   response.pipe(bufferList(responseBufferFunc));
}

responseBufferFunc = function(err, bl) {
   if (err) return console.error(err);

   console.log(bl.length);
   console.log(bl.toString());
}

processHttpGetError = function(err) {
   console.error(err);
}

try {
   http.get(process.argv[2], httpGetFunc);
} catch (err) {
   console.err(err);
}