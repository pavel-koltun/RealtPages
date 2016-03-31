(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('apartment-rent', {
            parent: 'entity',
            url: '/apartment-rent?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'onlinerRealtPagesApp.apartmentRent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apartment-rent/apartment-rents.html',
                    controller: 'ApartmentRentController',
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
                    $translatePartialLoader.addPart('apartmentRent');
                    $translatePartialLoader.addPart('rentType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('apartment-rent-detail', {
            parent: 'entity',
            url: '/apartment-rent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'onlinerRealtPagesApp.apartmentRent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apartment-rent/apartment-rent-detail.html',
                    controller: 'ApartmentRentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('apartmentRent');
                    $translatePartialLoader.addPart('rentType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApartmentRent', function($stateParams, ApartmentRent) {
                    return ApartmentRent.get({id : $stateParams.id});
                }]
            }
        })
        .state('apartment-rent.new', {
            parent: 'apartment-rent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment-rent/apartment-rent-dialog.html',
                    controller: 'ApartmentRentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                owner: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('apartment-rent', null, { reload: true });
                }, function() {
                    $state.go('apartment-rent');
                });
            }]
        })
        .state('apartment-rent.edit', {
            parent: 'apartment-rent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment-rent/apartment-rent-dialog.html',
                    controller: 'ApartmentRentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApartmentRent', function(ApartmentRent) {
                            return ApartmentRent.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('apartment-rent', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('apartment-rent.delete', {
            parent: 'apartment-rent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment-rent/apartment-rent-delete-dialog.html',
                    controller: 'ApartmentRentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApartmentRent', function(ApartmentRent) {
                            return ApartmentRent.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('apartment-rent', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
