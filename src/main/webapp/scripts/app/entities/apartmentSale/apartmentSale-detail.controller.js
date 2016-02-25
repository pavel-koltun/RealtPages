'use strict';

angular.module('onlinerrealtpagesApp')
    .controller('ApartmentSaleDetailController', function ($scope, $rootScope, $stateParams, entity, ApartmentSale) {
        $scope.apartmentSale = entity;
        $scope.load = function (id) {
            ApartmentSale.get({id: id}, function(result) {
                $scope.apartmentSale = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinerrealtpagesApp:apartmentSaleUpdate', function(event, result) {
            $scope.apartmentSale = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
