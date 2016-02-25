'use strict';

angular.module('onlinerrealtpagesApp')
	.controller('ApartmentSaleDeleteController', function($scope, $uibModalInstance, entity, ApartmentSale) {

        $scope.apartmentSale = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ApartmentSale.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
