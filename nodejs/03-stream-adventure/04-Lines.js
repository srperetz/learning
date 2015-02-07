var through = require('through');
var split = require('split');

var i = 0;

var tr =
   through(
      function (buf) {
         this.queue(
            ((++i % 2) === 1
               ? buf.toString().toLowerCase()
               : buf.toString().toUpperCase()
            ) + "\n");
      }
   );

process.stdin.pipe(split()).pipe(tr).pipe(process.stdout);
