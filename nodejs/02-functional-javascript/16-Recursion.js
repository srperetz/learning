function getDependencies(tree) {
   function _getDependencies(treebranch, parentName, res, isTopLevel) {
//console.log("treebranch="+treebranch)
//console.log("parentName="+parentName)
//console.log("isTopLevel="+isTopLevel)
      Object.keys(treebranch).forEach(
         function(element) {
            if (typeof(treebranch[element]) === 'object') {
               if (element == "dependencies") {
                  return _getDependencies(treebranch[element], parentName, res, false);
               } else {
                  return _getDependencies(treebranch[element], element, res, false);
               }
            } else if (!isTopLevel && element == "version") {
               entry = parentName + "@" + treebranch[element];
               if (res.indexOf(entry) < 0) res.push(entry);
               return res;
//console.log("results="+res)
            }
         }
      );
      return res;
   };

   result = _getDependencies(tree, null, [], true);
   result.sort();
   return result;
}

module.exports = getDependencies;

/*tests:
var loremIpsum = {
   "name": "lorem-ipsum",
   "version": "0.1.1",
   "dependencies": {
      "optimist": {
         "version": "0.3.7",
         "dependencies": {
            "wordwrap": {
               "version": "0.0.2"
            },
            "inflection": {
               "version": "1.2.5"
            }
         }
      },
      "inflection": {
         "version": "1.2.6"
      },
      "wordwrap": {
         "version": "0.0.2"
      },
   }
};
console.log(getDependencies(loremIpsum)); // => [ 'inflection@1.2.6', 'optimist@0.3.7', 'wordwrap@0.0.2' ]
*/