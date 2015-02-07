var qhttp = require('q-io/http');

qhttp.read("http://localhost:1337")
   .then(
      function(response) {
         console.log(JSON.parse(response));
      }
   )
   .catch(console.error)
   .done();

