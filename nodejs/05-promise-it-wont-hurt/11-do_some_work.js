var qhttp = require('q-io/http');

qhttp.read("http://localhost:7000")
   .then(
      function(response) {
         return response;
      }
   )
   .then(
      function(id) {
         qhttp.read("http://localhost:7001/" + id)
            .then(
               function(response) {
                  console.log(JSON.parse(response));

               }
            )
            .catch(console.error)
            .done();
      }
   )
   .catch(console.error)
   .done();
