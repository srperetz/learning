var fs = require('fs');

if (process.argv.length < 3) {
   return console.error("Specify file path");
}

try {
   console.log(fs.readFileSync(process.argv[2], "utf8").toString().split("\n").length-1);
} catch (err) {
   console.error(err);
}