var q = require('q');

var defered = q.defer();

defered.promise.catch(
   function(reasonRejected) {
      console.log(
         reasonRejected && reasonRejected.message
            ? reasonRejected.message
            : reasonRejected);
   });

setTimeout(defered.reject, 300, "REJECTED!");