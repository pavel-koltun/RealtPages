'use strict';

angular.module('onlinerrealtpagesApp').controller('ApartmentSaleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApartmentSale',
        function($scope, $stateParams, $uibModalInstance, entity, ApartmentSale) {

        $scope.apartmentSale = entity;
        $scope.load = function(id) {
            ApartmentSale.get({id : id}, function(result) {
                $scope.apartmentSale = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinerrealtpagesApp:apartmentSaleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.apartmentSale.id != null) {
                ApartmentSale.update($scope.apartmentSale, onSaveSuccess, onSaveError);
            } else {
                ApartmentSale.save($scope.apartmentSale, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
