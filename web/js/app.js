/*global $ */
/*jshint unused:false */
var app = app || {};
var ENTER_KEY = 13;
var ESC_KEY = 27;

$(function () {
  'use strict';
  // Create our global collection of **Todos**.
  app.todos = new app.Todos();
  app.todos.url = window.location.origin + "/todos";
  app.TodoRouter = new app.TodoRouter();
  Backbone.history.start();
  // kick things off by creating the `App`
  new app.AppView();
});
