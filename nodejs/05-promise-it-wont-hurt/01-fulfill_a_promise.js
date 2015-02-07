var q = require('q');

var defered = q.defer();

defered.promise.then(console.log);

setTimeout(defered.resolve, 300, "RESOLVED!");