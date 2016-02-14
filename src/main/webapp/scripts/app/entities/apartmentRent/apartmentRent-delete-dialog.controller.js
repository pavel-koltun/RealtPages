'use strict';

angular.module('onlinerrealtpagesApp')
	.controller('ApartmentRentDeleteController', function($scope, $uibModalInstance, entity, ApartmentRent) {

        $scope.apartmentRent = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ApartmentRent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
