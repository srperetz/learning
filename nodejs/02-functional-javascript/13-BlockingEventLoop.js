function repeat(operation, num) {
  // modify this so it can be interrupted
  if (num <= 0) return;
  setImmediate(operation);
  return setImmediate(repeat, operation, --num);
}

module.exports = repeat;

/* tests:
setTimeout(console.log, 1000, "done");
repeat(function() { console.log("operation"); }, 20);
*/
