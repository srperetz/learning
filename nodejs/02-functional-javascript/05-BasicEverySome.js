function checkUsersValid(goodUsers) {
   return
      function(submittedUsers) {
         return submittedUsers.every(
            function(subUser) {
               return goodUsers.some(function(goodUser) { return goodUser.id == subUser.id; });
            }
         );
      };
}

module.exports = checkUsersValid;
