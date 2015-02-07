function duckCount() {
   if (!(arguments && arguments.length > 0)) return 0;

   result =
      Array.prototype.slice.call(arguments).filter(
         function(o) {
            return Object.prototype.hasOwnProperty.call(o, 'quack');
         }
      ).length;

   return result;
}

module.exports = duckCount