var crypto = require('crypto');

if (process.argv.length < 3) {
   throw "Specify passphrase";
}

process.stdin.pipe(crypto.createDecipher('aes256', process.argv[2])).pipe(process.stdout);
