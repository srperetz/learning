var q = require('q');

var defered1 = q.defer();
var defered2 = q.defer();

function all(promise1, promise2) {
   var defered = q.defer();

   var i = 0;
   var res1, res2;

   promise1
      .then(
         function(resolution) {
            i++;
            res1 = resolution;
            if (i >= 2) {
               defered.resolve([res1, res2]);
            }
         })
      .catch(defered.reject);

   promise2
      .then(
         function(resolution) {
            i++;
            res2 = resolution;
            if (i >= 2) {
               defered.resolve([res1, res2]);
            }
         })
      .catch(defered.reject)
      .done();

   return defered.promise;
}

var promise = all(defered1.promise, defered2.promise);

promise.then(console.log).done();

setTimeout(
   function() {
      defered1.resolve("PROMISES");
      defered2.resolve("FTW");
   }, 200);
