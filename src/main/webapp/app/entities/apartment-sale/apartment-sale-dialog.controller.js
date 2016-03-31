(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentSaleDialogController', ApartmentSaleDialogController);

    ApartmentSaleDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApartmentSale'];

    function ApartmentSaleDialogController ($scope, $stateParams, $uibModalInstance, entity, ApartmentSale) {
        var vm = this;
        vm.apartmentSale = entity;
        vm.load = function(id) {
            ApartmentSale.get({id : id}, function(result) {
                vm.apartmentSale = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinerRealtPagesApp:apartmentSaleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.apartmentSale.id !== null) {
                ApartmentSale.update(vm.apartmentSale, onSaveSuccess, onSaveError);
            } else {
                ApartmentSale.save(vm.apartmentSale, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
