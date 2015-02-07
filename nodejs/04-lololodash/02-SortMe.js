var lodash = require("lodash");

function worker(items) {
   return lodash.sortBy(items, function(item) { return -item.quantity; });
};

module.exports = worker;