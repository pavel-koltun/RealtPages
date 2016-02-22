'use strict';

angular.module('onlinerrealtpagesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('apartment', {
                parent: 'entity',
                url: '/apartments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinerrealtpagesApp.apartment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/apartment/apartments.html',
                        controller: 'ApartmentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('apartment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('apartment.detail', {
                parent: 'entity',
                url: '/apartment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinerrealtpagesApp.apartment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/apartment/apartment-detail.html',
                        controller: 'ApartmentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('apartment');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Apartment', function($stateParams, Apartment) {
                        return Apartment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('apartment.new', {
                parent: 'apartment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartment/apartment-dialog.html',
                        controller: 'ApartmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    apartmentId: null,
                                    created: null,
                                    updated: null,
                                    url: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('apartment', null, { reload: true });
                    }, function() {
                        $state.go('apartment');
                    })
                }]
            })
            .state('apartment.edit', {
                parent: 'apartment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartment/apartment-dialog.html',
                        controller: 'ApartmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Apartment', function(Apartment) {
                                return Apartment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('apartment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('apartment.delete', {
                parent: 'apartment',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartment/apartment-delete-dialog.html',
                        controller: 'ApartmentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Apartment', function(Apartment) {
                                return Apartment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('apartment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
