var duplexer = require('duplexer');
var through = require('through');

var counts = {};

module.exports =
   function (counter) {

      var inputStream =
         through(
            function(buf) {
               counts[buf.country] = 1 + (counts[buf.country] || 0);
            },
            function() {
               counter.setCounts(counts);
            }
         );

      return duplexer(inputStream, counter);
   };
