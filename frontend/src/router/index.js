import Vue from 'vue';
import VueRouter from 'vue-router';
// import AssetDashboard from '../views/AssetDashboard.vue';
import AssetCollection from '@/views/AssetCollection.vue';
import CategoryAssetPage from "@/views/CategoryAssetPage.vue";
import UserLogin from '@/views/UserLogin.vue';
import SuperTeacherDashboard from "@/views/SuperTeacherDashboard.vue";

Vue.use(VueRouter);

const routes = [
    {
        path: '/login',
        name: 'UserLogin',
        component: UserLogin,
        meta: { requiresAuth: false } // No authentication required for login
    },
    // {
    //     path: '/dashboard',
    //     name: 'AssetDashboard',
    //     component: AssetDashboard,
    //     meta: { requiresAuth: true } // Requires login
    // },
    {
        path: '/AdminDashboard',
        name: 'AdminDashboard',
        component: () => import(/* webpackChunkName: "admin" */ '../views/AdminDashboard.vue'),
        meta: { requiresAuth: true }, // Requires login
    },
    {
        path: '/SuperTeacherDashboard',
        name: 'SuperTeacherDashboard',
        component: SuperTeacherDashboard,
        meta: { requiresAuth: true }, // Requires login
    },
    {
        path: '/SubteacherDashboard',
        name: 'SubteacherDashboard',
        component: () => import(/* webpackChunkName: "sub-teacher" */ '../views/SubteacherDashboard.vue'),
        meta: { requiresAuth: true }, // Requires login
    },
    {
        path: '/assetCollection',
        name: 'AssetCollection',
        component: AssetCollection,
        meta: { requiresAuth: true } // Requires login
    },
    {
        path: '/assetInfo/:id',
        name: 'AssetInfo',
        component: () => import(/* webpackChunkName: "about" */ '../views/AssetInfo.vue'),
        props: true,
        meta: { requiresAuth: true } // Requires login
    },
    {
        path: '/assetAddition',
        name: 'assetAddition',
        component: () => import(/* webpackChunkName: "about" */ '../views/CreateAsset.vue'),
        meta: { requiresAuth: true } // Requires login
    },
    {
        path: '/assetUpdate/:id',
        name: 'assetUpdate',
        component: () => import(/* webpackChunkName: "about" */ '../views/EditAsset.vue'),
        props: true,
        meta: { requiresAuth: true } // Requires login
    },
    {
        path: '/categoryAssets/:categoryId',
        name: 'CategoryAssetPage',
        component: CategoryAssetPage,
        props: true,
        meta: { requiresAuth: true } // Requires login
    },
    {
        path: '/assetFetch',
        name: 'assetFetch',
        component: () => import(/* webpackChunkName: "about" */ '../views/AssetFetch.vue'),
        meta: { requiresAuth: true } // Requires login
    }
];

const router = new VueRouter({
    mode: 'history',
    routes
});

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        const token = localStorage.getItem('token');

        if (!token) {
            next({
                path: '/login',
                query: { redirect: to.fullPath } // when user login successfully then go back.
            });
        } else {
            if (!isValidToken(token)) {
                localStorage.removeItem('token'); // remove invalid token
                next({
                    path: '/login',
                    query: { redirect: to.fullPath }
                });
            } else {
                next();
            }
        }
    } else {
        next();
    }
});


function isValidToken(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1])); // 解码 JWT
        const currentTime = Date.now() / 1000;
        return payload.exp > currentTime;
    } catch (error) {
        return false;
    }
}


export default router;
