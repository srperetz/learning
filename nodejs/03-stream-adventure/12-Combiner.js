var combiner = require('stream-combiner');
var split = require('split');
var through = require('through');
var zlib = require('zlib');

module.exports =
   function () {
      var currentGenre;

      function parseJSONrow(buf) {
         if (buf.length === 0) return;
         var row = JSON.parse(buf);
         if (row["name"]) {
            if (row["type"] === "genre") {
               if (currentGenre) {
                  this.queue(JSON.stringify(currentGenre) + "\n");
               }
               currentGenre = { "name": row.name, "books": [] };
            } else if (row["type"] === "book") {
               if (currentGenre) {
                  currentGenre.books.push(row.name);
               } else {
                  throw "No genres before book entry: " + row.name;
               }
            } else {
               throw "Invalid type: " + row["type"];
            }
         } else {
            throw "No name in input row: " + row;
         }
      }
      function outputJSON() {
         if (currentGenre) {
            this.queue(JSON.stringify(currentGenre) + "\n");
         }
         this.queue(null);
      }

      var jsonThrough = through(parseJSONrow, outputJSON);

      return combiner(
         split(),
         jsonThrough,
         zlib.createGzip()
      );
   }
