var websocket = require('websocket-stream');

try {
   var wsStream = websocket('ws://localhost:8000');
   wsStream.end("hello\n");
} catch (err) {
   console.error(err);
}