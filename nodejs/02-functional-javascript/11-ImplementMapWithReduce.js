function arrayMap(arr, fn) {
   result =
      arr.reduce(
         function(newArr, currVal, i, currArr) {
            newArr.push(fn(currVal));
            return newArr;
         },
         []
      );

   return result;
}

module.exports = arrayMap;

/* tests:
console.log(
   "map([1,2,3,4,5], function double(item) { return item * 2} ==>\n",
   arrayMap([1,2,3,4,5], function(item) { return item * 2; })
);
*/