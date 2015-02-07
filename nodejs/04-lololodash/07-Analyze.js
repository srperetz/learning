var _ = require("lodash");

function worker(people) {
   /* people:
   [ { name: "mike",  income: 2563 },
     { name: "kim",   income: 1587 },
     { name: "liz",   income: 3541 },
     { name: "tom",   income: 2475 },
     { name: "bello", income: 987  },
     { name: "frank", income: 2975 } ]
   */

   var avgIncome =
      people && _.size(people) > 0
         ?  _.reduce(
               people,
               function(total, person) {
                  return total + person.income;
               },
               0
            ) / _.size(people)
         : 0;

   var incomeSummary = {
      "average": avgIncome,
      "underperform": _.filter(people, function(person) { return person.income <= avgIncome; }),
      "overperform": _.filter(people, function(person) { return person.income > avgIncome; })
   };

   incomeSummary.underperform = _.sortBy(incomeSummary.underperform, 'income');
   incomeSummary.overperform = _.sortBy(incomeSummary.overperform, 'income');

   return incomeSummary;
};

module.exports = worker;