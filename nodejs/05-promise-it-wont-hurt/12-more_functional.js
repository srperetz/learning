var qhttp = require('q-io/http');
var _ = require('lodash');

qhttp.read("http://localhost:7000")
   .then( _.flow(_.bind(String.prototype.concat, "http://localhost:7001/"), qhttp.read) )
   .then( _.flow(JSON.parse, console.log) )
   .catch(console.error)
   .done();
