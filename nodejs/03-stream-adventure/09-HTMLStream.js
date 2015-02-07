var trumpet = require('trumpet');
var through = require('through');

var trumpeter = trumpet();

try {
   var loudStream = trumpeter.select('.loud').createStream();

   loudStream.pipe(
      through(
         function(buf) {
            this.queue(buf.toString().toUpperCase());
         }
      )
   ).pipe(loudStream);

   process.stdin.pipe(trumpeter).pipe(process.stdout);
} catch (err) {
   console.error(err);
}