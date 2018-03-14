import Vue from 'vue';
import iView from 'iview';
import Cookies from 'js-cookie';
import {router} from './router/index';
import store from './store';
import App from './app.vue';
import util from './libs/util'
import 'iview/dist/styles/iview.css';

Vue.use(iView);
Vue.prototype.$ajax = util.ajax;

new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App),
    mounted () {
        this.$store.commit('updateMenulist');
        this.$ajax.defaults.headers.common['Authorization'] = Cookies.get('token');
        this.$ajax.get("/profile").catch((error) => {
            this.$Message.warning("登录失效");
            this.$router.push({
                name: 'login'
            });
        })
    }
});
