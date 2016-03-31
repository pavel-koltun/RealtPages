(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('PriceDialogController', PriceDialogController);

    PriceDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Price', 'Apartment'];

    function PriceDialogController ($scope, $stateParams, $uibModalInstance, entity, Price, Apartment) {
        var vm = this;
        vm.price = entity;
        vm.apartments = Apartment.query();
        vm.load = function(id) {
            Price.get({id : id}, function(result) {
                vm.price = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinerRealtPagesApp:priceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.price.id !== null) {
                Price.update(vm.price, onSaveSuccess, onSaveError);
            } else {
                Price.save(vm.price, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.created = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
