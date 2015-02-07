function curryN(fn, n) {
   expectedArgCount = n || fn.length;

   function currier(args) {
      if (expectedArgCount > args.length) {
         return (
            function(arg) {
               return currier(args.concat(arg));
            });
      } else {
         return fn.apply(this, args);
      }
   }

   return currier([]);
}

module.exports = curryN;

/*tests:
function add3(one, two, three) {
  return one + two + three;
}
function divide(one, two) {
  return one / two;
}

var curryC = curryN(add3);
var curryB = curryC(1);
var curryA = curryB(2);
console.log(curryA(3)); // => 6
console.log(curryA(10)); // => 13

console.log(curryN(add3)(1)(2)(3)); // => 6
console.log(curryN(divide)(20)(5)); // => 4
console.log(curryN(divide)(5)(25)); // => 0.2
*/