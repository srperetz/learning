var lodash = require("lodash");

function worker(places) {
   return lodash.forEach(
      places,
      function(place) {
         place.size =
            (place.population >= 1
               ? "big"
               : (place.population >= 0.5
                     ? "med"
                     : "small"));
      });
};

module.exports = worker;