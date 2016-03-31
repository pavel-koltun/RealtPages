(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('apartment', {
            parent: 'entity',
            url: '/apartment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'onlinerRealtPagesApp.apartment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apartment/apartments.html',
                    controller: 'ApartmentController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('apartment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('apartment-detail', {
            parent: 'entity',
            url: '/apartment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'onlinerRealtPagesApp.apartment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apartment/apartment-detail.html',
                    controller: 'ApartmentDetailController',
                    controllerAs: 'vm'
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
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment/apartment-dialog.html',
                    controller: 'ApartmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
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
                }).result.then(function() {
                    $state.go('apartment', null, { reload: true });
                }, function() {
                    $state.go('apartment');
                });
            }]
        })
        .state('apartment.edit', {
            parent: 'apartment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment/apartment-dialog.html',
                    controller: 'ApartmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Apartment', function(Apartment) {
                            return Apartment.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('apartment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('apartment.delete', {
            parent: 'apartment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment/apartment-delete-dialog.html',
                    controller: 'ApartmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Apartment', function(Apartment) {
                            return Apartment.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('apartment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
