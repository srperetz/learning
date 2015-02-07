var http = require('http');
var url = require('url');

PARSEROUTE = "/api/parsetime";
UNIXROUTE = "/api/unixtime";

if (process.argv.length < 3) {
   return console.error("Specify port");
}

portn = Number(process.argv[2]);

httplistener = function(request, response) {
   if (request.method == "GET") {
      requrl = url.parse(request.url, true);
      if (requrl.pathname == PARSEROUTE && requrl.query.iso) {
         dt = new Date(requrl.query.iso);
         response.writeHead(200, { 'Content-Type': 'application/json' });
         response.end(
            JSON.stringify(
               {hour: dt.getHours(), minute: dt.getMinutes(), second: dt.getSeconds()}
            )
         );
      } else if (requrl.pathname == UNIXROUTE && requrl.query.iso) {
         dt = new Date(requrl.query.iso);
         response.writeHead(200, { 'Content-Type': 'application/json' });
         response.end(
            JSON.stringify({unixtime: dt.getTime()})
         );
      }
      else return console.error("Invalid path or query string");
   }
}

try {
   var server = http.createServer(httplistener);
   server.listen(portn);
} catch (err) {
   console.error(err);
}