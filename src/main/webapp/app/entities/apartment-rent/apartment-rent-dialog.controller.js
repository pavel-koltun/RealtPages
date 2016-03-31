(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentRentDialogController', ApartmentRentDialogController);

    ApartmentRentDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApartmentRent'];

    function ApartmentRentDialogController ($scope, $stateParams, $uibModalInstance, entity, ApartmentRent) {
        var vm = this;
        vm.apartmentRent = entity;
        vm.load = function(id) {
            ApartmentRent.get({id : id}, function(result) {
                vm.apartmentRent = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinerRealtPagesApp:apartmentRentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.apartmentRent.id !== null) {
                ApartmentRent.update(vm.apartmentRent, onSaveSuccess, onSaveError);
            } else {
                ApartmentRent.save(vm.apartmentRent, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
