var net = require('net');
var strftime = require('strftime');

if (process.argv.length < 3) {
   return console.error("Specify port");
}

tcplistener = function(socket) {
   socket.end(strftime('%F %H:%M%n', new Date()));
}

try {
   var server = net.createServer(tcplistener);
   server.listen(Number(process.argv[2]));
} catch (err) {
   console.error(err);
}