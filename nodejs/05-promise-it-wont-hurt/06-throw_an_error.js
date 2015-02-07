var q = require('q');

function parsePromised(jsonStr) {
   var defered = q.defer();
   var jsonO;

   try {
      jsonO = JSON.parse(jsonStr);
   } catch (err) {
      defered.reject(err);
   }

   defered.resolve(jsonO);

   return defered.promise;
}

parsePromised(process.argv[2]).catch(console.log);
