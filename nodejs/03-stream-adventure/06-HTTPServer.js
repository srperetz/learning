var through = require('through');
var http = require('http');

if (process.argv.length < 3) {
   return console.error("Specify port");
}

portn = Number(process.argv[2]);

try {
   var server =
      http.createServer(
         function (req, res) {
            if (req.method === 'POST') {
               req.pipe(
                  through(
                     function uppercaserer(buf) {
                        this.queue(buf.toString().toUpperCase());
                     },
                     function() { this.queue(null); }
                  )
               ).pipe(res);
            }
            else res.end();
         }
      );
   server.listen(portn);
} catch (err) {
   console.error(err);
}