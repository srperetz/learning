var _ = require("lodash");

function worker(orders) {
   /* orders:
      [ { article: 1, quantity: 4 },
        { article: 2, quantity: 2 },
        { article: 1, quantity: 5 } ]
   */
   var orderTotals = [];

   var ordersGroupedByArticle = _.groupBy(orders, 'article');
   /* ordersGroupedByArticle:
      {
         1: [{ article: 1, quantity: 4 }, { article: 1, quantity: 5 } ],
         2: [{ article: 2, quantity: 2 }]
      }
   */

   _.forEach(
      ordersGroupedByArticle,
      function(articles, key) {
         orderTotals.push(
            {
               article: Number(key),
               total_orders: _.reduce(
                  articles, // [{ article: 1, quantity: 4 }, { article: 1, quantity: 5 } ]
                  function(total, order) {
                     return total + order.quantity;
                  },
                  0)
            });
      });
   /* orderTotals:
      [ { article: 1, total_orders: 9 },
        { article: 2, total_orders: 2 } ]
   */


   return _.sortBy(
      orderTotals,
      function(orderTotal) { return -orderTotal.total_orders; });
};

module.exports = worker;