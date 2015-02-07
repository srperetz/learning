function repeat(operation, num) {
   //console.log("num="+num);
   if (num <= 0) return null;
   //console.log("calling operation");
   operation();
   return function() { return repeat(operation, num-1); };
}

function trampoline(fn) {
  count = 0;
  while (fn) {
     //console.log("calling fn=" + fn);
     fn = fn();
     //console.log("got back fn=" + fn);
     if (fn) count++;
  }
  return count;
}

function repeater(operation, num) {
   return trampoline(function() { return repeat(operation, num); });
};

module.exports = repeater;

/* tests:
i = 0;
repeater(function() { i++; }, 1000000);
console.log(i);
*/
