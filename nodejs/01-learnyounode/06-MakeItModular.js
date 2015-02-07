var filteredLs = require('./filteredLs');

if (process.argv.length < 3) {
   return console.error("Specify file path");
}
if (process.argv.length < 4) {
   return console.error("Specify file type");
}

logDirs = function (err, fileList) {
   if (err) {
      return console.error(err);
   }
   for (i = 0; i < fileList.length; i++) {
      console.log(fileList[i]);
   }
}

filteredLs(process.argv[2], process.argv[3], logDirs);
