var tar = require('tar');
var zlib = require('zlib');
var crypto = require('crypto');
var through = require('through');

if (process.argv.length < 3) {
   throw "Specify cipher name";
}
if (process.argv.length < 4) {
   throw "Specify passphrase";
}

var tarParser = tar.Parse();
tarParser.on(
   'entry',
   function (entry) {
      if (entry.type == "File") {
         var tr =
            through(
               null,
               function() {
                  this.queue(" " + entry.path + "\n");
                  this.queue(null);
               });

         entry
            .pipe(crypto.createHash('md5', { encoding: 'hex' }))
            .pipe(tr)
            .pipe(process.stdout);
      }
   }
);

process.stdin
   .pipe(crypto.createDecipher(process.argv[2], process.argv[3]))
   .pipe(zlib.createGunzip())
   .pipe(tarParser);
