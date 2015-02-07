var q = require('q');

var defered = q.defer();

function attachTitle(name) {
   return "DR. " + name;
}

defered.promise.then(attachTitle).then(console.log);

defered.resolve("MANHATTAN");
