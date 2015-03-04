module.exports = function(context) {
   var q = context.data.root.query
   return q.name + q.suffix;
}