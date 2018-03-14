import Main from '@/views/Main.vue';

// 不作为Main组件的子页面展示的页面单独写，如下
export const loginRouter = {
    path: '/login',
    name: 'login',
    meta: {
        title: 'Login - 登录'
    },
    component: resolve => { require(['@/views/login.vue'], resolve); }
};

export const page404 = {
    path: '/*',
    name: 'error-404',
    meta: {
        title: '404-页面不存在'
    },
    component: resolve => { require(['@/views/error-page/404.vue'], resolve); }
};

export const page403 = {
    path: '/403',
    meta: {
        title: '403-权限不足'
    },
    name: 'error-403',
    component: resolve => { require(['@//views/error-page/403.vue'], resolve); }
};

export const page500 = {
    path: '/500',
    meta: {
        title: '500-服务端错误'
    },
    name: 'error-500',
    component: resolve => { require(['@/views/error-page/500.vue'], resolve); }
};

// 作为Main组件的子页面展示但是不在左侧菜单显示的路由写在otherRouter里
export const otherRouter = {
    path: '/',
    name: 'otherRouter',
    component: Main,
    /*children: [
        { path: 'home', title: {i18n: 'home'}, name: 'home_index', component: resolve => { require(['@/views/home/home.vue'], resolve); } }
    ],*/
    redirect: '/home/home'
};

// 作为Main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
export const appRouter = [
    {
        path: '/home',
        icon: 'ios-home',
        title: '首页',
        name: 'home',
        component: Main,
        children: [
            {
                path: 'home',
                title: '首页',
                name: 'home-page',
                component: resolve => { require(['@/views/home/home.vue'], resolve); }
            }
        ]
    },
    {
        path: '/account',
        icon: 'ios-person',
        name: 'account',
        title: '用户',
        component: Main,
        children: [
            {
                path: 'local-user',
                icon: 'ios-color-filter',
                name: 'local-user',
                title: '本地用户',
                component: resolve => { require(['@/views/account/local-user/local-user.vue'], resolve); }
            },
            {
                path: 'cloud-user',
                icon: 'ios-cloud',
                name: 'cloud-user',
                title: '云账号',
                component: resolve => { require(['@/views/account/cloud-user/cloud-user.vue'], resolve); }
            }/*,
            {
                path: 'permission',
                icon: 'ios-list',
                name: 'permission',
                title: '用户权限',
                component: resolve => { require(['@/views/account/permission/permission.vue'], resolve); }
            }*/
        ]
    },
    {
        path: '/instance',
        icon: 'ios-keypad',
        name: 'instance',
        title: '实例',
        component: Main,
        children: [
            {
                path: 'detail',
                icon: 'ios-pulse-strong',
                name: 'instance-detail',
                title: '详情',
                component: resolve => { require(['@/views/instance/detail/detail.vue'], resolve); }
            },
            {
                path: 'template',
                icon: 'ios-compose',
                name: 'instance-template',
                title: '模板',
                component: resolve => { require(['@/views/instance/template/template.vue'], resolve); }
            }
        ]
    },
    {
        path: '/action',
        icon: 'ios-lightbulb',
        name: 'action',
        title: '动作',
        component: Main,
        children: [
            {
                path: 'expand',
                icon: 'ios-plus',
                name: 'action-expand',
                title: '扩容',
                component: resolve => { require(['@/views/action/expand/expand.vue'], resolve); }
            },
            {
                path: 'shrink',
                icon: 'ios-minus',
                name: 'action-shrink',
                title: '缩容',
                component: resolve => { require(['@/views/action/shrink/shrink.vue'], resolve); }
            },
            {
                path: 'email',
                icon: 'ios-email',
                name: 'action-email',
                title: '邮件',
                component: resolve => { require(['@/views/action/email/email.vue'], resolve); }
            }
        ]
    },
    {
        path: '/strategy',
        icon: 'ios-analytics',
        title: '策略',
        name: 'strategy',
        component: Main,
        children: [
            {
                path: 'strategy',
                title: '策略',
                name: 'strategy-page',
                component: resolve => { require(['@/views/strategy/strategy.vue'], resolve); }
            }
        ]
    },
    {
        path: '/monitor',
        icon: 'ios-speedometer',
        name: 'monitor',
        title: '监控',
        component: Main,
        children: [
            {
                path: 'metric',
                icon: 'ios-pricetag',
                name: 'monitor-metric',
                title: '监测项',
                component: resolve => { require(['@/views/monitor/metric/metric.vue'], resolve); }
            },
            {
                path: 'monitor',
                icon: 'ios-eye',
                name: 'monitor-monitor',
                title: '监视器',
                component: resolve => { require(['@/views/monitor/monitor/monitor.vue'], resolve); }
            }
        ]
    }
];

// 所有上面定义的路由都要写在下面的routers里
export const routers = [
    loginRouter,
    otherRouter,
    ...appRouter,
    page500,
    page403,
    page404
];
