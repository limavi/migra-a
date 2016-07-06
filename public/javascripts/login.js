var app = angular.module('myApp', ['ui.bootstrap', 'confirmDialogBoxModule']);
app.controller('loginCtrl', function($scope, $http, $timeout, $uibModal, EmpService) {

     $scope.loginAction = function() {
        alert("entro al funcion")
     }

});
