'use strict';

angular.module('onlinerrealtpagesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('apartmentRent', {
                parent: 'entity',
                url: '/apartmentRents',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinerrealtpagesApp.apartmentRent.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/apartmentRent/apartmentRents.html',
                        controller: 'ApartmentRentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('apartment');
                        $translatePartialLoader.addPart('apartmentRent');
                        $translatePartialLoader.addPart('rentType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('apartmentRent.detail', {
                parent: 'entity',
                url: '/apartmentRent/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinerrealtpagesApp.apartmentRent.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/apartmentRent/apartmentRent-detail.html',
                        controller: 'ApartmentRentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('apartment');
                        $translatePartialLoader.addPart('apartmentRent');
                        $translatePartialLoader.addPart('rentType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ApartmentRent', function($stateParams, ApartmentRent) {
                        return ApartmentRent.get({id : $stateParams.id});
                    }]
                }
            })
            .state('apartmentRent.new', {
                parent: 'apartmentRent',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartmentRent/apartmentRent-dialog.html',
                        controller: 'ApartmentRentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    type: null,
                                    owner: false,
                                    apartmentId: null,
                                    created: null,
                                    updated: null,
                                    url: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('apartmentRent', null, { reload: true });
                    }, function() {
                        $state.go('apartmentRent');
                    })
                }]
            })
            .state('apartmentRent.edit', {
                parent: 'apartmentRent',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartmentRent/apartmentRent-dialog.html',
                        controller: 'ApartmentRentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ApartmentRent', function(ApartmentRent) {
                                return ApartmentRent.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('apartmentRent', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('apartmentRent.delete', {
                parent: 'apartmentRent',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartmentRent/apartmentRent-delete-dialog.html',
                        controller: 'ApartmentRentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ApartmentRent', function(ApartmentRent) {
                                return ApartmentRent.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('apartmentRent', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
