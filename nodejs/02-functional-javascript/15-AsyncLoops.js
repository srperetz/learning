function loadUsers(userIds, load, done) {
   var users = [];
   numUsers = userIds.length;
   numLoaded = 0;
   userIds.forEach(
      function(userId, i) {
         load(
            userId,
            function(user) {
               users[i] = user;
               numLoaded++;
               if (numLoaded == numUsers) {
                  done(users);
               }
            }
         );
      }
   );
}

module.exports = loadUsers;

/* tests:
loadUsers(
   [7, 12, 34],
   function (id, loader) {
      var user = {
         userId: id,
         name: "User " + id
      };
      loader(user);
   },
   function (users) {
      console.log("done - users:");
      users.forEach(function(user) { console.log(user); });
   }
);
*/