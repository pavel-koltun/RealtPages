'use strict';

angular.module('onlinerrealtpagesApp')
    .controller('ApartmentRentDetailController', function ($scope, $rootScope, $stateParams, entity, ApartmentRent) {
        $scope.apartmentRent = entity;
        $scope.load = function (id) {
            ApartmentRent.get({id: id}, function(result) {
                $scope.apartmentRent = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinerrealtpagesApp:apartmentRentUpdate', function(event, result) {
            $scope.apartmentRent = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
