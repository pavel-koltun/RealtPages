'use strict';

angular.module('onlinerrealtpagesApp')
    .controller('PriceDetailController', function ($scope, $rootScope, $stateParams, entity, Price, Apartment) {
        $scope.price = entity;
        $scope.load = function (id) {
            Price.get({id: id}, function(result) {
                $scope.price = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinerrealtpagesApp:priceUpdate', function(event, result) {
            $scope.price = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
