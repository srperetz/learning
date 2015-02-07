function countWords(inputWords) {
   result =
      inputWords.reduce(
         function(prevResult, curWord) {
            prevResult[curWord] = 1 + (prevResult[curWord] ? prevResult[curWord] : 0);
            return prevResult;
         },
         {}
      );

   return result;
}

module.exports = countWords;
