function Spy(targetObject, targetMethod) {
   if (Object.prototype.hasOwnProperty.call(targetObject, targetMethod)) {
      var spyObj = {
         count: 0,
         originalMethod: targetObject[targetMethod]
      };

      targetObject[targetMethod] =
         function () {
            spyObj.count++;
            return spyObj.originalMethod.apply(targetObject, arguments);
         };

      return spyObj;
   } else {
      return console.log(
         "Spy: ERROR: '" + Object.getPrototypeOf(targetObject)
            + "' does not have method '" + targetMethod);
   }
}

module.exports = Spy;

/* tests:
var spy = Spy(console, 'error')

console.error('calling console.error')
console.error('calling console.error')
console.error('calling console.error')

console.log(spy.count) // 3
*/