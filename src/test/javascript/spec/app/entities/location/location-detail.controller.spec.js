'use strict';

describe('Controller Tests', function() {

    describe('Location Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLocation, MockApartment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLocation = jasmine.createSpy('MockLocation');
            MockApartment = jasmine.createSpy('MockApartment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Location': MockLocation,
                'Apartment': MockApartment
            };
            createController = function() {
                $injector.get('$controller')("LocationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'onlinerrealtpagesApp:locationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
