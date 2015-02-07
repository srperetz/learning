var q = require('q');

var defered = q.defer();

defered.promise.then(console.log, console.log);

defered.resolve("I FIRED");
defered.reject("I DID NOT FIRE");
