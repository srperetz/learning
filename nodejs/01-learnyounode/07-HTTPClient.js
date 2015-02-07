var http = require('http');

if (process.argv.length < 3) {
   return console.error("Specify URL");
}

httpGetFunc = function(response) {
   response.setEncoding("utf8");
   response.on("data", processHttpGetData);
   response.on("error", processHttpGetError);
}

processHttpGetData = function(chunk) {
   console.log(chunk);
}
processHttpGetError = function(err) {
   console.error(err);
}


try {
   http.get(process.argv[2], httpGetFunc);
} catch (err) {
   console.error(err);
}