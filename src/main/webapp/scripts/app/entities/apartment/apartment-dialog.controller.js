'use strict';

angular.module('onlinerrealtpagesApp').controller('ApartmentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Apartment',
        function($scope, $stateParams, $uibModalInstance, entity, Apartment) {

        $scope.apartment = entity;
        $scope.load = function(id) {
            Apartment.get({id : id}, function(result) {
                $scope.apartment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinerrealtpagesApp:apartmentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.apartment.id != null) {
                Apartment.update($scope.apartment, onSaveSuccess, onSaveError);
            } else {
                Apartment.save($scope.apartment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreated = {};

        $scope.datePickerForCreated.status = {
            opened: false
        };

        $scope.datePickerForCreatedOpen = function($event) {
            $scope.datePickerForCreated.status.opened = true;
        };
        $scope.datePickerForUpdated = {};

        $scope.datePickerForUpdated.status = {
            opened: false
        };

        $scope.datePickerForUpdatedOpen = function($event) {
            $scope.datePickerForUpdated.status.opened = true;
        };
}]);
