"use strict";

var _ = require("lodash");

var worker = function(toDoData) {
   if (!(toDoData)) return "";

   var isUrgentDate = function(dateStr) {
      return (new Date(dateStr).getTime() -  new Date().getTime()) < (2 * 24 * 60 * 60 * 1000);
   }

   _.forEach(toDoData,
         function(toDoList, person) {
            toDoData[person] = _.sortBy(toDoList, 'date');
         });

   var toDoHtml = "<ul>\n" +
      "<% _.forEach(toDoCollection, function(toDoList, personName) { %>" +
            "<li><%= personName %>\n" +
            "<ul>" +
            "<% _.forEach(toDoList, function(toDo) { %>" +
                  "<li>" +
                  "<% if (isUrgentDate(toDo.date)) { %><b>URGENT</b> <% } %>" +
                  "<%= toDo.todo %>" +
                  "</li>\n" +
               "<% }); %>" +
            "</ul>\n" +
            "</li>\n" +
         "<% }); %>" +
      "</ul>";

   return _.template(toDoHtml, { toDoCollection: toDoData }, { 'imports': { 'isUrgentDate': isUrgentDate } });
}

module.exports = worker;
