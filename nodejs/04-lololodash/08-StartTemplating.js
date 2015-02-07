var _ = require("lodash");

function worker(userData) {
   /* userData:
    { name: "Foo",
      login: [ 1407574431, 140753421 ]
    }
   */
   if (!(userData && userData.name && userData.login)) return "";

   userData["logins"] = _.size(userData.login);

   var templateFn = _.template("Hello <%= name %> (logins: <%= logins %>)");

   return templateFn(userData);
};

module.exports = worker;
