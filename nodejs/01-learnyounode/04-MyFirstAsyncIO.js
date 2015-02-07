var fs = require('fs');

if (process.argv.length < 3) {
   return console.error("Specify file path");
}

readFileCb = function (err, dataStr) {
   if (err) {
      console.error(err);
      return;
   }
   console.log(dataStr.split("\n").length-1);
}

fs.readFile(process.argv[2], "utf8", readFileCb);

