var request = require('request');

try {
   process.stdin.pipe(request.post("http://localhost:8000/")).pipe(process.stdout);
} catch (err) {
   console.error(err);
}