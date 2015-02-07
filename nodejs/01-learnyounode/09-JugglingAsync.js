var http = require('http');
var BufferList = require('bl');

if (process.argv.length < 5) {
   return console.error("Specify 3 URLs");
}

var strs = [];
var count = process.argv.length - 2;

printResults = function() {
   for (j = 0; j < strs.length; j++) {
      console.log(strs[j].substr(0, 400));
   }
}

for (i = 0; i < process.argv.length - 2; i++) {
   url = process.argv[i+2];
   strs.push("");
   (function (blcounter, urli) {
      try {
         http.get(
            urli,
            function(response) {
               response.on("error", processHttpGetError);
               response.pipe(
                  BufferList(
                     function(err, bl) {
                        count--;

                        if (err) return console.error(err);

                        strs[blcounter] = bl.toString();

                        if (count == 0) printResults();
                     }
                  )
               );
            });
      } catch (err) {
         console.error(err);
      }
   }(i, url));
}

processHttpGetError = function(err) {
   console.error(err);
}
