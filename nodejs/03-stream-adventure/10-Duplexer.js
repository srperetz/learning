var childproc = require('child_process');
var duplexer = require('duplexer');

module.exports =
   function (cmd, args) {
      proc = childproc.spawn(cmd, args);
      return duplexer(proc.stdin, proc.stdout);
   };
