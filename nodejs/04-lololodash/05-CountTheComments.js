var _ = require("lodash");

function worker(comments) {
   commentsGroupedByUser = _.groupBy(comments, 'username');
   commentCounts = [];

   _.forEach(
      commentsGroupedByUser,
      function(userComments, key) {
         commentCounts.push(
            {
               username: key,
               comment_count: _.size(userComments)
            });
      });

   return _.sortBy(commentCounts, function(commentCount) { return -commentCount.comment_count; });
};

module.exports = worker;