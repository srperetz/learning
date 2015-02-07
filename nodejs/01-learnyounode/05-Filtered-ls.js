var fs = require('fs');
var path = require('path');

filteredLs = function(pathName, extFilter, callback) {

   extFilter = "." + extFilter

   filterFunc = function(filename) {
      return path.extname(filename) == extFilter;
   }

   readDirCb = function (err, fileList) {
      if (err) {
         return callback(err);
      }
      callback(null, fileList.filter(filterFunc));
   }

   fs.readdir(pathName, readDirCb);
}

module.exports = filteredLs;
