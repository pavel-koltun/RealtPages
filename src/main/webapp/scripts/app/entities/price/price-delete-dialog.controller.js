'use strict';

angular.module('onlinerrealtpagesApp')
	.controller('PriceDeleteController', function($scope, $uibModalInstance, entity, Price) {

        $scope.price = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Price.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
