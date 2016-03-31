(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentDialogController', ApartmentDialogController);

    ApartmentDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Apartment', 'Location', 'Price'];

    function ApartmentDialogController ($scope, $stateParams, $uibModalInstance, entity, Apartment, Location, Price) {
        var vm = this;
        vm.apartment = entity;
        vm.locations = Location.query();
        vm.prices = Price.query();
        vm.load = function(id) {
            Apartment.get({id : id}, function(result) {
                vm.apartment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinerRealtPagesApp:apartmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.apartment.id !== null) {
                Apartment.update(vm.apartment, onSaveSuccess, onSaveError);
            } else {
                Apartment.save(vm.apartment, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.created = false;
        vm.datePickerOpenStatus.updated = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
