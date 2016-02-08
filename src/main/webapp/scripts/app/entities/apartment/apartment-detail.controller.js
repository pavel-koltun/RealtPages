'use strict';

angular.module('onlinerrealtpagesApp')
    .controller('ApartmentDetailController', function ($scope, $rootScope, $stateParams, entity, Apartment) {
        $scope.apartment = entity;
        $scope.load = function (id) {
            Apartment.get({id: id}, function(result) {
                $scope.apartment = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinerrealtpagesApp:apartmentUpdate', function(event, result) {
            $scope.apartment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
