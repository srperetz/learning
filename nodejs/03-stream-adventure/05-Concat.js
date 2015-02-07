var concat = require('concat-stream');

process.stdin.pipe(
   concat(
      function (b) {
         process.stdout.write(b.toString().split('').reverse().join(''));
      }
   )
);
