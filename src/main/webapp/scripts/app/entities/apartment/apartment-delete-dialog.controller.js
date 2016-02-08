'use strict';

angular.module('onlinerrealtpagesApp')
	.controller('ApartmentDeleteController', function($scope, $uibModalInstance, entity, Apartment) {

        $scope.apartment = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Apartment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
