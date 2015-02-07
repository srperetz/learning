var _ = require("lodash");

function worker(places) {
   climates = {
      hot: [],
      warm: []
   };

   function isHot(temp) { return (Number(temp) >  19); }

   _.forEach(
      places,
      function(placetemps, key) {
         if (_.every(placetemps, isHot)) {
            climates["hot"].push(key);
         } else if (_.some(placetemps, isHot)) {
            climates["warm"].push(key);
         }
      });

   return climates;
};

module.exports = worker;