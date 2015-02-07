var lodash = require("lodash");

function worker(users) {
   return lodash.where(users, { 'active': true });
};

module.exports = worker;