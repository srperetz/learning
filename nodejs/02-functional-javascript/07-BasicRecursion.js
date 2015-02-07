function reduce(arr, fn, initial) {
   result =
      function reducefn(arr, fn, val, idx) {
         if (idx == arr.length) {
            return val;
         } else {
            return reducefn(arr, fn, fn(val, arr[idx], idx, arr), idx+1);
         }
      }(arr, fn, initial, 0);

   return result;
}

module.exports = reduce;
