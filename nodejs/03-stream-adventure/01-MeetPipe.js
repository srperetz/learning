var fs = require('fs');
if (process.argv.length < 3) {
   return console.error("Specify file path");
}
fs.createReadStream(process.argv[2], "utf8").pipe(process.stdout);
